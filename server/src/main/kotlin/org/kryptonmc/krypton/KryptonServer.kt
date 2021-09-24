/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import kotlinx.coroutines.launch
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.event.server.TickEndEvent
import org.kryptonmc.api.event.server.TickStartEvent
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.item.KryptonItemManager
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.plugin.KryptonEventManager
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.util.tryCreateDirectory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.fluid.KryptonFluidManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.net.InetSocketAddress
import java.nio.file.Path
import java.security.SecureRandom
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.math.max
import kotlin.system.measureTimeMillis

class KryptonServer(
    val config: KryptonConfig,
    val useDataConverter: Boolean,
    override val profileCache: KryptonProfileCache,
    private val configPath: Path,
    worldFolder: Path
) : Server {

    override val platform = KryptonPlatform
    override val maxPlayers = config.status.maxPlayers
    override val motd = config.status.motd
    override val isOnline = config.server.onlineMode
    override val address = InetSocketAddress(config.server.ip, config.server.port)

    val playerManager = PlayerManager(this)
    override val players = playerManager.players

    override val channels: MutableSet<Key> = ConcurrentHashMap.newKeySet()
    override val console = KryptonConsole(this)
    override var scoreboard: KryptonScoreboard? = null
        private set

    private val nettyProcess = NettyProcess(this)
    val random = SecureRandom()

    override val worldManager = KryptonWorldManager(this, worldFolder)
    override val commandManager = KryptonCommandManager()
    override val pluginManager = KryptonPluginManager(this)
    override val eventManager = KryptonEventManager(pluginManager)
    override val servicesManager = KryptonServicesManager
    override val registryManager = KryptonRegistryManager
    override val blockManager = KryptonBlockManager
    override val itemManager = KryptonItemManager
    override val fluidManager = KryptonFluidManager
    override val scheduler = KryptonScheduler(pluginManager)
    override val factoryProvider = KryptonFactoryProvider

    @Volatile
    var isRunning = true
        private set
    @Volatile
    private var lastTickTime = 0L

    private var lastOverloadWarning = 0L
    private var tickCount = 0
    private var oversleepFactor = 0L

    fun start() {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        // Load the packets
        LOGGER.debug("Loading packets...")
        PacketRegistry.bootstrap()

        // Register the built-in commands
        LOGGER.debug("Registering commands...")
        commandManager.registerBuiltins()

        //Create server config lists
        LOGGER.debug("Loading server config lists...")
        playerManager.bannedPlayers.validatePath()
        playerManager.whitelist.validatePath()
        playerManager.bannedIps.validatePath()
        playerManager.ops.validatePath()
        playerManager.whitlistedIps.validatePath()

        // Start the metrics system
        LOGGER.debug("Starting bStats metrics")
        System.setProperty("bstats.relocatecheck", "false") // Avoid relocating bStats since we want to expose it later
        KryptonMetrics.initialize(this, config.other.metrics)

        // Warn about piracy being unsupported if proxy forwarding is not enabled
        if (!config.server.onlineMode && config.proxy.mode == ForwardingMode.NONE) {
            LOGGER.warn("-----------------------------------------------------------------------------------")
            LOGGER.warn("THIS SERVER IS IN OFFLINE MODE! NO ATTEMPTS WILL BE MADE TO AUTHENTICATE USERS!")
            LOGGER.warn("While this may allow players without full Minecraft accounts to connect," +
                    "it also allows hackers to connect with any username they choose! Beware!")
            LOGGER.warn("Please beware that connections made to the server will not be encrypted, meaning hackers " +
                    "could potentially intercept sensitive data!")
            LOGGER.warn("To get rid of this message, change \"online-mode\" to true in the configuration file")
            LOGGER.warn("-----------------------------------------------------------------------------------")
        }

        LOGGER.info("Preparing world ${config.world.name}...")
        worldManager.init()

        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part
        // of their lifecycle, and we call it sync so plugins can finish initialising before we do
        eventManager.fireAndForgetSync(ServerStartEvent)

        // Initialize console permissions after plugins are loaded
        console.setupPermissions()

        // Set the last tick time (avoids initial check sending an overload warning every time)
        lastTickTime = System.currentTimeMillis()

        // Start accepting connections
        LOGGER.debug("Starting Netty...")
        IOScope.launch { nettyProcess.run() }

        // Add the shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(Thread(::stop, "Shutdown Handler").apply { isDaemon = false })

        LOGGER.info("Done (${"%.3fs".format(Locale.ROOT, (System.nanoTime() - startTime) / 1.0E9)})! Type " +
                "\"help\" for help.")

        // Start up the console handler
        LOGGER.debug("Starting console handler")
        Thread(console::start, "Console Handler").apply {
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, exception ->
                KryptonConsole.LOGGER.error("Caught previously unhandled exception ", exception)
            }
            isDaemon = true
        }.start()

        run()
    }

    fun run() = try {
        while (isRunning) {
            val nextTickTime = System.currentTimeMillis() - lastTickTime
            if (nextTickTime > 2000L && lastTickTime - lastOverloadWarning >= 15_000L) {
                LOGGER.warn("Can't keep up! Running $nextTickTime ms (${nextTickTime / 50} ticks) behind!")
                lastOverloadWarning = lastTickTime
            }
            // tick
            eventManager.fireAndForgetSync(TickStartEvent(tickCount))
            val tickTime = measureTimeMillis(::tick)

            // end tick
            val finishTime = System.currentTimeMillis()
            eventManager.fireAndForgetSync(TickEndEvent(tickCount, tickTime, finishTime))
            lastTickTime = finishTime

            val sleepTime = measureTimeMillis {
                Thread.sleep(max(0, MILLISECONDS_PER_TICK - tickTime - oversleepFactor))
            }
            oversleepFactor = sleepTime - (MILLISECONDS_PER_TICK - tickTime)
        }
    } catch (exception: Throwable) {
        LOGGER.error("Encountered an unexpected exception", exception)
    } finally {
        restart()
    }

    private fun loadPlugins() {
        LOGGER.info("Loading plugins...")

        try {
            val pluginPath = Path.of("plugins")
            if (!pluginPath.exists()) pluginPath.tryCreateDirectory()
            if (!pluginPath.isDirectory()) {
                LOGGER.warn("Plugin path $pluginPath is not a directory! Plugins will not be loaded!")
                return
            }

            pluginManager.loadPlugins(pluginPath)
        } catch (exception: Exception) {
            LOGGER.error("Failed to load plugins!", exception)
        }

        pluginManager.plugins.forEach {
            val instance = it.instance ?: return@forEach
            try {
                eventManager.registerUnchecked(it, instance)
            } catch (exception: Exception) {
                LOGGER.error(
                    "Unable to register plugin listener for plugin ${it.description.name}!",
                    exception
                )
            }
        }

        LOGGER.info("Finished plugin loading! Loaded ${pluginManager.plugins.size} plugins.")
    }

    private fun tick() {
        tickCount++

        val time = System.currentTimeMillis()
        playerManager.tick(time)
        if (playerManager.players.isEmpty()) return // don't tick if there are no players on the server

        worldManager.worlds.forEach { (_, world) ->
            if (tickCount % 20 == 0) {
                playerManager.sendToAll(PacketOutTimeUpdate(
                    world.data.time,
                    world.data.dayTime,
                    world.data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]
                ), world)
            }
            world.tick()
        }
        if (config.world.autosaveInterval > 0 && tickCount % config.world.autosaveInterval == 0) {
            LOGGER.info("Auto save started.")
            worldManager.saveAll()
            LOGGER.info("Auto save finished.")
        }
        if (tickCount % SAVE_PROFILE_CACHE_INTERVAL == 0) {
            LOGGER.debug("Saving authenticated user cache...")
            profileCache.queueSave()
        }
    }

    fun updateConfig() {
        val config = HoconConfigurationLoader.builder()
            .path(configPath)
            .defaultOptions(KryptonConfig.OPTIONS)
            .build()
        val node = config.load().set(this.config)
        config.save(node)
    }

    override fun player(uuid: UUID) = playerManager.playersByUUID[uuid]

    override fun player(name: String) = playerManager.playersByName[name]

    override fun registerChannel(channel: Key) {
        require(channel !in RESERVED_CHANNELS) {
            "Cannot register reserved channel with name \"minecraft:register\" or \"minecraft:unregister\"!"
        }
        channels.add(channel)
    }

    override fun unregisterChannel(channel: Key) {
        channels.remove(channel)
        require(channel !in RESERVED_CHANNELS) {
            "Cannot unregister reserved channels with name \"minecraft:register\" or \"minecraft:unregister\"!"
        }
    }

    override fun sendMessage(message: Component, permission: String) {
        playerManager.players.filter { it.hasPermission(permission) }.forEach { it.sendMessage(message) }
        console.sendMessage(message)
        return
    }

    override fun audiences() = players + console

    fun getPermissionLevel(profile: KryptonGameProfile): Int {
        if (playerManager.ops.contains(profile)) return playerManager.ops[profile]?.permissionLevel ?: 1
        return 1
    }

    fun stop(halt: Boolean = true) {
        if (!isRunning) return // Ensure we cannot accidentally run this twice

        // Stop server and shut down session manager (disconnecting all players)
        LOGGER.info("Starting shutdown for Krypton version ${KryptonPlatform.version}...")
        isRunning = false
        playerManager.disconnectAll()

        // Save data
        LOGGER.info("Saving player, world, and region data...")
        worldManager.saveAll()
        playerManager.saveAll()

        // Shut down plugins and unregister listeners
        LOGGER.info("Shutting down plugins and unregistering listeners...")
        eventManager.fireAndForgetSync(ServerStopEvent)
        eventManager.shutdown()

        // Shut down scheduler
        scheduler.shutdown()
        LOGGER.info("Goodbye")

        // Manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
        LogManager.shutdown()

        // Finally, halt the JVM if we should (avoids shutdown hooks running)
        if (halt) Runtime.getRuntime().halt(0)
    }

    fun restart() {
        stop(false) // avoid halting there because we halt here
        val split = config.other.restartScript.split(" ")
        if (split.isNotEmpty()) {
            if (!Path.of(split[0]).isRegularFile()) {
                println("Unable to find restart script ${split[0]}! Refusing to restart!")
                Runtime.getRuntime().halt(0)
            }
            println("Attempting to restart the server with script ${split[0]}...")
            val os = System.getProperty("os.name").lowercase()
            Runtime.getRuntime().exec(
                (if ("win" in os) "cmd /c start " else "sh ") + config.other.restartScript
            )
        }
        Runtime.getRuntime().halt(0)
    }

    companion object {

        private val RESERVED_CHANNELS = setOf(key("register"), key("unregister"))
        private const val MILLISECONDS_PER_TICK = 50L // milliseconds in a tick
        private const val SAVE_PROFILE_CACHE_INTERVAL = 600
        val LOGGER = logger<KryptonServer>()
    }
}
