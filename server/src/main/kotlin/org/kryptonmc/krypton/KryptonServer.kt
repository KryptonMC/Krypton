/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.Server
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.KryptonEventManager
import org.kryptonmc.krypton.event.server.KryptonServerStartEvent
import org.kryptonmc.krypton.event.server.KryptonServerStopEvent
import org.kryptonmc.krypton.event.server.KryptonTickEndEvent
import org.kryptonmc.krypton.event.server.KryptonTickStartEvent
import org.kryptonmc.krypton.network.SessionManager
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.user.KryptonUserManager
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.util.crypto.YggdrasilSessionKey
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.util.Collections
import java.util.Locale
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

/**
 * This is the centre of operations here at Krypton Inc. Everything stems from
 * this class.
 */
class KryptonServer(
    val config: KryptonConfig,
    override val profileCache: KryptonProfileCache,
    private val configPath: Path,
    worldFolder: Path
) : Server, PacketGroupingAudience {

    override val platform: KryptonPlatform = KryptonPlatform
    override val maxPlayers: Int = config.status.maxPlayers
    override val motd: Component = config.status.motd
    override val isOnline: Boolean = config.server.onlineMode
    override val address: InetSocketAddress = InetSocketAddress(config.server.ip, config.server.port)

    val playerManager: PlayerManager = PlayerManager(this)
    override val players: List<KryptonPlayer> = Collections.unmodifiableList(playerManager.players)
    override val sessionManager: SessionManager = SessionManager(this)

    override val console: KryptonConsole = KryptonConsole(this)
    override val scoreboard: KryptonScoreboard = KryptonScoreboard(this)

    override val worldManager: KryptonWorldManager = KryptonWorldManager(this, worldFolder)
    override val commandManager: KryptonCommandManager = KryptonCommandManager()
    override val pluginManager: KryptonPluginManager = KryptonPluginManager()
    override val eventManager: KryptonEventManager = KryptonEventManager(pluginManager)
    override val servicesManager: KryptonServicesManager = KryptonServicesManager(this)
    override val registryManager: KryptonRegistryManager = KryptonRegistryManager
    override val tagManager: KryptonTagManager = KryptonTagManager
    override val scheduler: KryptonScheduler = KryptonScheduler(pluginManager)
    override val factoryProvider: KryptonFactoryProvider = KryptonFactoryProvider
    override val userManager: KryptonUserManager = KryptonUserManager(this)

    @Volatile
    var isRunning: Boolean = true
        private set

    // These help us keep track of how fast the server is running and how far
    // behind it is at any one time.
    private var lastTickTime = 0L
    private var lastOverloadWarning = 0L
    private var tickCount = 0
    private var oversleepFactor = 0L

    init {
        instance = this
        YggdrasilSessionKey.get()
    }

    // The order of loading here is pretty important, as some things depend on
    // others to function properly.
    fun start() {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        LOGGER.debug("Loading packets...")
        PacketRegistry.bootstrap()
        LOGGER.debug("Registering commands...")
        commandManager.registerBuiltins()

        // Create server config lists here before we allow any players in.
        LOGGER.debug("Loading server config lists...")
        playerManager.bannedPlayers.validatePath()
        playerManager.whitelist.validatePath()
        playerManager.bannedIps.validatePath()
        playerManager.whitelistedIps.validatePath()

        // Start the metrics system.
        LOGGER.debug("Starting bStats metrics")
        KryptonMetrics.initialize(this, config.other.metrics)

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

        // Load plugins here because most of everything they need is available now.
        servicesManager.bootstrap()
        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part of their lifecycle,
        // and we call it sync so plugins can finish initialising before we do.
        eventManager.fireAndForgetSync(KryptonServerStartEvent)

        // Initialize console permissions after plugins are loaded. We do this here so plugins have had a chance to register their listeners
        // so they can actually catch this event and set up their own permission providers for the console.
        console.setupPermissions()

        // Start accepting connections
        LOGGER.debug("Starting Netty...")
        NettyProcess.run(this)

        Runtime.getRuntime().addShutdownHook(Thread({ stop(false) }, "Shutdown Handler").apply { isDaemon = false })
        val doneTime = String.format(Locale.ROOT, "%.3fs", (System.nanoTime() - startTime) / 1.0E9)
        LOGGER.info("Done ($doneTime)! Type \"help\" for help.")

        LOGGER.debug("Starting console handler")
        console.run()

        run()
    }

    fun run() {
        try {
            // Set the last tick time early (avoids initial check sending an overload warning every time)
            lastTickTime = System.currentTimeMillis()

            while (isRunning) {
                val nextTickTime = System.currentTimeMillis() - lastTickTime
                if (nextTickTime > SLOW_TICK_THRESHOLD && lastTickTime - lastOverloadWarning >= OVERLOAD_WARNING_INTERVAL) {
                    LOGGER.warn("Can't keep up! Running $nextTickTime ms (${nextTickTime / MILLISECONDS_PER_TICK} ticks) behind!")
                    lastOverloadWarning = lastTickTime
                }

                eventManager.fireAndForgetSync(KryptonTickStartEvent(tickCount))
                val tickTime = measureTimeMillis(::tick)

                val finishTime = System.currentTimeMillis()
                eventManager.fireAndForgetSync(KryptonTickEndEvent(tickCount, tickTime, finishTime))
                lastTickTime = finishTime

                // This logic ensures that ticking isn't delayed by the overhead from Thread.sleep, which seems to be up to 10 ms,
                // which is enough that the average TPS would be reporting at around 15-16, rather than 20.
                val sleepTime = measureTimeMillis { Thread.sleep(max(0, MILLISECONDS_PER_TICK - tickTime - oversleepFactor)) }
                oversleepFactor = sleepTime - (MILLISECONDS_PER_TICK - tickTime)
            }
        } catch (exception: Throwable) { // This is hacky, but ensures that we catch absolutely everything that may be thrown here.
            LOGGER.error("Encountered an unexpected exception", exception)
            stop()
        }
    }

    private fun loadPlugins() {
        LOGGER.info("Loading plugins...")
        try {
            val pluginPath = Path.of("plugins")
            if (!Files.exists(pluginPath)) {
                try {
                    Files.createDirectory(pluginPath)
                } catch (exception: Exception) {
                    LOGGER.warn("Failed to create the plugins directory! Plugins will not be loaded!", exception)
                    return
                }
            }
            if (!Files.isDirectory(pluginPath)) {
                LOGGER.warn("Plugin path $pluginPath is not a directory! Plugins will not be loaded!")
                return
            }

            pluginManager.loadPlugins(pluginPath, this)
        } catch (exception: Exception) {
            LOGGER.error("Failed to load plugins!", exception)
        }

        // Register all of the plugin instances as event listeners, so that plugins can listen for events such as
        // ServerStartEvent and ServerStopEvent in their main class.
        for (container in pluginManager.plugins) {
            val instance = container.instance ?: continue
            try {
                eventManager.registerUnchecked(container, instance)
            } catch (exception: Exception) {
                LOGGER.error("Unable to register plugin listener for plugin ${container.description.name}!", exception)
            }
        }

        LOGGER.info("Finished plugin loading! Loaded ${pluginManager.plugins.size} plugins.")
    }

    private fun tick() {
        tickCount++

        sessionManager.update(System.currentTimeMillis())
        if (playerManager.players.isEmpty()) return // don't tick if there are no players on the server

        worldManager.worlds.forEach { (_, world) ->
            if (tickCount % TICKS_PER_SECOND == 0) {
                val packet = PacketOutUpdateTime(world.data.time, world.data.dayTime, world.data.gameRules.get(GameRules.DO_DAYLIGHT_CYCLE))
                sessionManager.sendGrouped(packet) { it.world === world }
            }
            world.tick()
        }
        if (config.world.autosaveInterval > 0 && tickCount % config.world.autosaveInterval == 0) {
            LOGGER.info("Auto save started.")
            worldManager.saveAll(false)
            LOGGER.info("Auto save finished.")
        }
        if (tickCount % SAVE_PROFILE_CACHE_INTERVAL == 0) profileCache.save()
    }

    fun updateConfig(path: String, value: Any?) {
        val config = HoconConfigurationLoader.builder().path(configPath).defaultOptions(KryptonConfig.OPTIONS).build()
        config.save(config.load().node(path.split(".")).set(value))
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

    override fun player(uuid: UUID): KryptonPlayer? = playerManager.playersByUUID.get(uuid)

    override fun player(name: String): KryptonPlayer? = playerManager.playersByName.get(name)

    override fun audiences(): Iterable<Audience> = players.asSequence().plus(console).asIterable()

    override fun sendMessage(source: Identified, message: Component, type: MessageType) {
        super<PacketGroupingAudience>.sendMessage(source, message, type)
        console.sendMessage(source, message, type)
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        super<PacketGroupingAudience>.sendMessage(source, message, type)
        console.sendMessage(source, message, type)
    }

    fun stop(explicitExit: Boolean = true) {
        if (!isRunning) return // Ensure we cannot accidentally run this twice

        val shutdownProcess = Runnable {
            // Stop server and shut down session manager (disconnecting all players)
            LOGGER.info("Starting shutdown for Krypton version ${KryptonPlatform.version}...")
            isRunning = false
            playerManager.disconnectAll()
            NettyProcess.shutdown()

            // Save data
            LOGGER.info("Saving player, world, and region data...")
            worldManager.saveAll(true)
            playerManager.saveAll()

            // Shut down plugins and unregister listeners
            LOGGER.info("Shutting down plugins and unregistering listeners...")
            eventManager.fireAndForgetSync(KryptonServerStopEvent)
            eventManager.shutdown()

            // Shut down scheduler
            scheduler.shutdown()
            LOGGER.info("Goodbye")

            // Manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
            LogManager.shutdown()
            if (explicitExit) exitProcess(0)
        }

        if (explicitExit) Thread(shutdownProcess).start() else shutdownProcess.run()
    }

    fun restart() {
        stop()
        val split = config.other.restartScript.split(" ")
        if (split.isNotEmpty()) {
            if (!Files.isRegularFile(Path.of(split[0]))) {
                println("Unable to find restart script ${split[0]}! Refusing to restart!")
                return
            }
            println("Attempting to restart the server with script ${split[0]}...")
            val os = System.getProperty("os.name").lowercase()
            val runCommand = if (os.contains("win")) "cmd /c start " else "sh "
            Runtime.getRuntime().exec(runCommand + config.other.restartScript)
        }
    }

    companion object {

        private const val MILLISECONDS_PER_TICK = 50L // milliseconds in a tick
        private const val TICKS_PER_SECOND = 20
        private const val SAVE_PROFILE_CACHE_INTERVAL = 600
        private const val SLOW_TICK_THRESHOLD = 2000L
        private const val OVERLOAD_WARNING_INTERVAL = 15000L

        private val LOGGER = logger<KryptonServer>()

        @Volatile
        private var instance: KryptonServer? = null

        // This is a massive hack, and even writing this makes me cringe, but we need static access in places.
        // This is internal to stop dependents using it from Kotlin, and synthetic to stop dependents using it from Java.
        // Seriously, don't use this.
        @JvmStatic
        @JvmSynthetic
        internal fun get(): KryptonServer = requireNotNull(instance) { "KryptonServer.get() called before server was loaded!" }
    }
}
