/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton

import org.apache.commons.lang3.SystemUtils
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.event.GlobalEventNode
import org.kryptonmc.api.scheduling.ExecutionType
import org.kryptonmc.api.scheduling.TaskTime
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.auth.GameProfileCache
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.KryptonGlobalEventNode
import org.kryptonmc.krypton.event.server.KryptonServerStartEvent
import org.kryptonmc.krypton.event.server.KryptonServerStopEvent
import org.kryptonmc.krypton.server.StatusManager
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.user.KryptonUserManager
import org.kryptonmc.krypton.network.PacketFraming
import org.kryptonmc.krypton.network.socket.NetworkServer
import org.kryptonmc.krypton.plugin.loader.PluginLoader
import org.kryptonmc.krypton.ticking.TickDispatcher
import org.kryptonmc.krypton.ticking.TickThreadProvider
import org.kryptonmc.krypton.util.crypto.YggdrasilSessionKey
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.UnixDomainSocketAddress
import java.nio.file.Path
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

/**
 * This is the centre of operations here at Krypton Inc. Everything stems from
 * this class.
 */
class KryptonServer(
    override val config: KryptonConfig,
    val profileCache: GameProfileCache,
    worldFolder: Path
) : BaseServer {

    private val networkServer = NetworkServer(this)
    // TODO: Use a better registry access that is dynamically populated from data packs.
    override val playerManager: PlayerManager = PlayerManager(this)
    val statusManager: StatusManager = StatusManager(playerManager, config.status.motd, config.status.maxPlayers)

    override val console: KryptonConsole = KryptonConsole(this)
    override val scoreboard: KryptonScoreboard = KryptonScoreboard(this)

    override val worldManager: KryptonWorldManager = KryptonWorldManager(this, worldFolder)
    override val commandManager: KryptonCommandManager = KryptonCommandManager()
    override val pluginManager: KryptonPluginManager = KryptonPluginManager()
    override val eventNode: GlobalEventNode = KryptonGlobalEventNode
    override val servicesManager: KryptonServicesManager = KryptonServicesManager(this)
    override val scheduler: KryptonScheduler = KryptonScheduler()
    override val userManager: KryptonUserManager = KryptonUserManager(this)
    private val random = RandomSource.create()

    private val tickDispatcher = TickDispatcher<KryptonChunk>(TickThreadProvider.counter(), 1)
    @Volatile
    private var running = true
    private var stopped = false

    init {
        PacketFraming.setCompressionThreshold(config.server.compressionThreshold)
        YggdrasilSessionKey.get()
    }

    fun tickDispatcher(): TickDispatcher<KryptonChunk> = tickDispatcher

    override fun isRunning(): Boolean = running

    fun isStopped(): Boolean = stopped

    // The order of loading here is pretty important, as some things depend on
    // others to function properly.
    fun initialize(): Boolean {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        LOGGER.debug("Loading packets...")
        PacketRegistry.bootstrap()
        LOGGER.debug("Registering commands...")
        commandManager.registerBuiltins()

        // Start the metrics system.
        LOGGER.debug("Starting bStats metrics")
        KryptonMetrics.initialize(this, config.advanced.metrics)

        // Warn about piracy being unsupported if proxy forwarding is not enabled, because video game piracy is bad.
        if (!config.server.onlineMode && config.proxy.mode == ProxyCategory.Mode.NONE) {
            LOGGER.warn("-----------------------------------------------------------------------------------")
            LOGGER.warn("THIS SERVER IS IN OFFLINE MODE! NO ATTEMPTS WILL BE MADE TO AUTHENTICATE USERS!")
            LOGGER.warn("While this may allow players without full Minecraft accounts to connect, it also allows hackers to connect with any " +
                    "username they choose! Beware!")
            LOGGER.warn("Please beware that connections made to the server will not be encrypted, meaning hackers could potentially intercept " +
                    "sensitive data!")
            LOGGER.warn("To get rid of this message, change \"online-mode\" to true in the configuration file")
            LOGGER.warn("-----------------------------------------------------------------------------------")
        }

        LOGGER.info("Preparing world ${config.world.name}...")
        worldManager.init()

        // Load the console after the world. This ensures we don't get errors from trying to use the default world before it's ready.
        // TODO: See if there is something we can do better than this, like trying to accept the world when it's null.
        LOGGER.debug("Starting console handler...")
        console.run()

        // Load plugins here because most of everything they need is available now.
        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part of their lifecycle,
        // and we call it sync so plugins can finish initialising before we do.
        LOGGER.debug("Notifying plugins that the server is ready")
        eventNode.fire(KryptonServerStartEvent)

        // Initialize console permissions after plugins are loaded. We do this here so plugins have had a chance to register their listeners
        // so they can actually catch this event and set up their own permission providers for the console.
        LOGGER.debug("Setting up console permissions...")
        console.setupPermissions()

        // Determine the correct bind address (UNIX domain socket or standard internet socket).
        val bindAddress = if (config.server.ip.startsWith("unix:")) {
            if (!SystemUtils.IS_OS_UNIX) {
                LOGGER.error("UNIX domain sockets are only supported on UNIX-like systems!")
                return false
            }
            if (config.proxy.mode == ProxyCategory.Mode.NONE) {
                LOGGER.error("UNIX domain sockets require IPs to be forwarded from a proxy!")
                return false
            }
            LOGGER.info("Using UNIX domain socket ${config.server.ip}")
            UnixDomainSocketAddress.of(config.server.ip)
        } else {
            InetSocketAddress(InetAddress.getByName(config.server.ip), config.server.port)
        }

        // Try binding to the port and start accepting connections
        LOGGER.debug("Starting Network Server...")
        try {
            networkServer.initialize(bindAddress)
        } catch (exception: IOException) {
            LOGGER.error("FAILED TO BIND TO PORT ${config.server.port}!", exception)
            return false
        }
        networkServer.start()

        setupAutosaveTasks()

        val doneTime = String.format(Locale.ROOT, "%.3fs", (System.nanoTime() - startTime) / 1.0E9)
        LOGGER.info("Done ($doneTime)! Type \"help\" for help.")
        return true
    }

    private fun setupAutosaveTasks() {
        if (config.world.autosaveInterval > 0) {
            val task = Runnable {
                LOGGER.info("Auto save started.")
                saveEverything(true, false, false)
                LOGGER.info("Auto save finished.")
            }
            scheduler.buildTask(task)
                .delay(TaskTime.ticks(config.world.autosaveInterval))
                .period(TaskTime.ticks(config.world.autosaveInterval))
                .executionType(ExecutionType.SYNCHRONOUS)
                .schedule()
        }
        scheduler.buildTask { profileCache.saveIfNeeded() }
            .delay(TaskTime.ticks(SAVE_PROFILE_CACHE_INTERVAL))
            .period(TaskTime.ticks(SAVE_PROFILE_CACHE_INTERVAL))
            .executionType(ExecutionType.SYNCHRONOUS)
            .schedule()
    }

    private fun loadPlugins() {
        LOGGER.info("Loading modules and plugins...")
        try {
            PluginLoader.createDefault(this).loadPlugins(this)
        } catch (exception: Exception) {
            LOGGER.error("Failed to load plugins and modules!", exception)
        }

        // Register all of the plugin instances as event listeners, so that plugins can listen for events such as
        // ServerStartEvent and ServerStopEvent in their main class.
        for (container in pluginManager.plugins) {
            val instance = container.instance ?: continue
            val eventNode = pluginManager.getEventNode(container)
            try {
                eventNode.registerListeners(instance)
            } catch (exception: Exception) {
                LOGGER.error("Unable to register plugin listener for plugin ${container.description.name}!", exception)
            }
        }

        LOGGER.info("Finished plugin loading! Loaded ${pluginManager.plugins.size} plugins.")
    }

    fun tick(startTime: Long) {
        scheduler.process()
        statusManager.tick(startTime)

        worldManager.worlds.values.forEach { it.tick() }
        tickDispatcher.updateAndAwait(startTime)

        val tickTime = System.currentTimeMillis() - startTime
        tickDispatcher.refreshThreads(tickTime)
    }

    private fun saveEverything(suppressLog: Boolean, flush: Boolean, forced: Boolean): Boolean {
        playerManager.saveAll()
        return worldManager.saveAllChunks(suppressLog, flush, forced)
    }

    override fun stop() {
        try {
            running = false
            stopped = true
            stopServer()
        } catch (exception: Throwable) {
            LOGGER.error("Error whilst attempting to stop the server!", exception)
        }
    }

    private fun stopServer() {
        // Stop server and shut down session manager (disconnecting all players)
        LOGGER.info("Starting shutdown for Krypton version ${KryptonPlatform.version}...")
        running = false
        networkServer.stop()
        tickDispatcher.shutdown()

        // Save data
        LOGGER.info("Saving and disconnecting players...")
        playerManager.saveAll()
        playerManager.disconnectAll()

        LOGGER.info("Saving worlds...")
        worldManager.worlds.values.forEach { it.doNotSave = false }
        worldManager.saveAllChunks(false, true, false)
        worldManager.worlds.values.forEach {
            try {
                it.close()
            } catch (exception: IOException) {
                LOGGER.error("Error whilst trying to close world ${it.data.name}!", exception)
            }
        }

        // Shut down plugins and unregister listeners
        LOGGER.info("Shutting down plugins and unregistering listeners...")
        eventNode.fire(KryptonServerStopEvent)

        // Shut down scheduler
        LOGGER.info("Goodbye")

        // Manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
        LogManager.shutdown()
    }

    fun isProtected(world: KryptonWorld, x: Int, z: Int, player: KryptonPlayer): Boolean {
        if (world.dimension !== World.OVERWORLD) return false
        if (player.hasPermission(KryptonPermission.BYPASS_SPAWN_PROTECTION.node)) return false
        if (config.world.spawnProtectionRadius <= 0) return false
        // TODO: This isn't right. This should use the heightmap pos if not in world bounds, but it'll do for now.
        val distanceX = abs(x - world.data.spawnX)
        val distanceZ = abs(z - world.data.spawnZ)
        val maxDistance = max(distanceX, distanceZ)
        return maxDistance <= config.world.spawnProtectionRadius
    }

    override fun generateSoundSeed(): Long = random.nextLong()

    companion object {

        private const val SAVE_PROFILE_CACHE_INTERVAL = 600

        private val LOGGER = LogManager.getLogger()
    }
}
