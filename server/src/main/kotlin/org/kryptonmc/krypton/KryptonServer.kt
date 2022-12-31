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

import io.netty.channel.epoll.Epoll
import io.netty.channel.kqueue.KQueue
import io.netty.channel.unix.DomainSocketAddress
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.world.World
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
import org.kryptonmc.krypton.network.ConnectionManager
import org.kryptonmc.krypton.network.ConnectionInitializer
import org.kryptonmc.krypton.packet.PacketRegistry
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.user.KryptonUserManager
import org.kryptonmc.krypton.network.PacketFraming
import org.kryptonmc.krypton.util.crypto.YggdrasilSessionKey
import org.kryptonmc.krypton.util.executor.ReentrantBlockableEventLoop
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicReference
import java.util.function.BooleanSupplier
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.max

/**
 * This is the centre of operations here at Krypton Inc. Everything stems from
 * this class.
 */
class KryptonServer(
    private val serverThread: Thread,
    override val config: KryptonConfig,
    override val profileCache: KryptonProfileCache,
    worldFolder: Path
) : ReentrantBlockableEventLoop<TickTask>("Server"), BaseServer {

    override val playerManager: PlayerManager = PlayerManager(this)
    override val connectionManager: ConnectionManager = ConnectionManager(playerManager, config.status.motd, config.status.maxPlayers)

    override val console: KryptonConsole = KryptonConsole(this)
    override val scoreboard: KryptonScoreboard = KryptonScoreboard(this)

    override val worldManager: KryptonWorldManager = KryptonWorldManager(this, worldFolder)
    override val commandManager: KryptonCommandManager = KryptonCommandManager()
    override val pluginManager: KryptonPluginManager = KryptonPluginManager()
    override val eventManager: KryptonEventManager = KryptonEventManager(pluginManager)
    override val servicesManager: KryptonServicesManager = KryptonServicesManager(this)
    override val scheduler: KryptonScheduler = KryptonScheduler(pluginManager)
    override val userManager: KryptonUserManager = KryptonUserManager(this)
    private val random = RandomSource.create()

    @Volatile
    private var running = true
    private var stopped = false
    // These help us keep track of how fast the server is running and how far
    // behind it is at any one time.
    private var tickCount = 0
    private var lastOverloadWarning = 0L
    private var nextTickTime = 0L
    private var lastTick = 0L
    private var mayHaveDelayedTasks = false
    private var delayedTasksMaxNextTickTime = 0L

    init {
        PacketFraming.setCompressionThreshold(config.server.compressionThreshold)
        YggdrasilSessionKey.get()
    }

    override fun isRunning(): Boolean = running

    fun isStopped(): Boolean = stopped

    // The order of loading here is pretty important, as some things depend on
    // others to function properly.
    private fun initialize(): Boolean {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        LOGGER.debug("Loading packets...")
        PacketRegistry.bootstrap()
        LOGGER.debug("Registering commands...")
        commandManager.registerBuiltins()

        // Load bans and whitelists here before we allow any players in.
        LOGGER.debug("Loading ban list and whitelist...")
        playerManager.load()

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
        LOGGER.debug("Loading built-in services...")
        servicesManager.bootstrap()
        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part of their lifecycle,
        // and we call it sync so plugins can finish initialising before we do.
        LOGGER.debug("Notifying plugins that the server is ready")
        eventManager.fireAndForgetSync(KryptonServerStartEvent)

        // Initialize console permissions after plugins are loaded. We do this here so plugins have had a chance to register their listeners
        // so they can actually catch this event and set up their own permission providers for the console.
        LOGGER.debug("Setting up console permissions...")
        console.setupPermissions()

        // Determine the correct bind address (UNIX domain socket or standard internet socket).
        val bindAddress = if (config.server.ip.startsWith("unix:")) {
            if (!Epoll.isAvailable() && !KQueue.isAvailable()) {
                LOGGER.error("UNIX domain sockets are not supported on this operating system!")
                return false
            }
            if (config.proxy.mode == ProxyCategory.Mode.NONE) {
                LOGGER.error("UNIX domain sockets require IPs to be forwarded from a proxy!")
                return false
            }
            LOGGER.info("Using UNIX domain socket ${config.server.ip}")
            DomainSocketAddress(config.server.ip.substring("unix:".length))
        } else {
            InetSocketAddress(InetAddress.getByName(config.server.ip), config.server.port)
        }

        // Start accepting connections
        LOGGER.debug("Starting Netty...")
        try {
            ConnectionInitializer.run(this, bindAddress)
        } catch (exception: IOException) {
            LOGGER.error("FAILED TO BIND TO PORT ${config.server.port}!", exception)
            return false
        }

        val doneTime = String.format(Locale.ROOT, "%.3fs", (System.nanoTime() - startTime) / 1.0E9)
        LOGGER.info("Done ($doneTime)! Type \"help\" for help.")
        return true
    }

    override fun run() {
        try {
            check(initialize()) { "Failed to initialize server!" }

            // Set the next tick time early (avoids initial check sending an overload warning every time)
            nextTickTime = System.currentTimeMillis()
            lastTick = System.nanoTime() - NORMAL_TICK_TIME_NANOS

            while (running) {
                val nanoTime = System.nanoTime()
                val tickDifference = nanoTime / NANOS_TO_MILLIS - nextTickTime
                if (tickDifference > SLOW_TICK_THRESHOLD && nextTickTime - lastOverloadWarning >= OVERLOAD_WARNING_INTERVAL) {
                    val differenceTicks = tickDifference / MILLISECONDS_PER_TICK
                    LOGGER.warn("Can't keep up! Running $tickDifference ms ($differenceTicks ticks) behind!")
                    nextTickTime += differenceTicks * MILLISECONDS_PER_TICK
                    lastOverloadWarning = nextTickTime
                }

                lastTick = nanoTime
                nextTickTime += MILLISECONDS_PER_TICK
                tick(::haveTime)
                mayHaveDelayedTasks = true
                delayedTasksMaxNextTickTime = max(System.currentTimeMillis() + MILLISECONDS_PER_TICK, nextTickTime)
                waitUntilNextTick()
            }
        } catch (exception: Throwable) { // This is hacky, but ensures that we catch absolutely everything that may be thrown here.
            LOGGER.error("Encountered an unexpected exception", exception)
        } finally {
            // This may seem weird, but this will be called when running is no longer true, which will cause the server to stop.
            try {
                stopped = true
                stopServer()
            } catch (exception: Throwable) {
                LOGGER.error("Error whilst attempting to stop the server!", exception)
            }
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

    private fun tick(hasTimeLeft: BooleanSupplier) {
        val startTime = System.currentTimeMillis()
        eventManager.fireAndForgetSync(KryptonTickStartEvent(tickCount + 1))

        ++tickCount
        tickChildren(hasTimeLeft)
        connectionManager.tick(startTime)
        playerManager.tick(startTime)

        if (config.world.autosaveInterval > 0 && tickCount % config.world.autosaveInterval == 0) {
            LOGGER.info("Auto save started.")
            saveEverything(true, false, false)
            LOGGER.info("Auto save finished.")
        }
        if (tickCount % SAVE_PROFILE_CACHE_INTERVAL == 0) profileCache.saveIfNeeded()

        val endTime = System.nanoTime()
        val remaining = NORMAL_TICK_TIME_NANOS - (endTime - lastTick)
        eventManager.fireAndForgetSync(KryptonTickEndEvent(tickCount, (endTime - lastTick) / NANOS_TO_MILLIS, remaining))
    }

    private fun tickChildren(hasTimeLeft: BooleanSupplier) {
        worldManager.worlds.forEach { (_, world) ->
            if (tickCount % TICKS_PER_SECOND == 0) {
                connectionManager.sendGroupedPacket(PacketOutUpdateTime.create(world.data)) { it.world === world }
            }
            world.tick(hasTimeLeft)
        }
    }

    private fun haveTime(): Boolean = runningTask() ||
            System.currentTimeMillis() < if (mayHaveDelayedTasks) delayedTasksMaxNextTickTime else nextTickTime

    private fun waitUntilNextTick() {
        runAllTasks()
        managedBlock { !haveTime() }
    }

    override fun wrapRunnable(runnable: Runnable): TickTask = TickTask(tickCount, runnable)

    override fun shouldRun(task: TickTask): Boolean = task.tick + 3 < tickCount || haveTime()

    override fun pollTask(): Boolean {
        val hasTasks = pollTaskInternal()
        mayHaveDelayedTasks = hasTasks
        return hasTasks
    }

    private fun pollTaskInternal(): Boolean {
        // TODO: We may want to try polling other tasks in here in the future, which is why this is like this.
        return super.pollTask()
    }

    override fun scheduleExecutables(): Boolean = super.scheduleExecutables() && !isStopped()

    override fun executeIfPossible(task: Runnable) {
        if (isStopped()) throw RejectedExecutionException("Cannot execute tasks while the server is shutting down!")
        super.executeIfPossible(task)
    }

    override fun runningThread(): Thread = serverThread

    private fun saveEverything(suppressLog: Boolean, flush: Boolean, forced: Boolean): Boolean {
        playerManager.saveAll()
        return worldManager.saveAllChunks(suppressLog, flush, forced)
    }

    override fun stop(waitForServer: Boolean) {
        running = false
        if (waitForServer) {
            try {
                serverThread.join()
            } catch (exception: InterruptedException) {
                LOGGER.error("Error while shutting down!", exception)
            }
        }
    }

    private fun stopServer() {
        // Stop server and shut down session manager (disconnecting all players)
        LOGGER.info("Starting shutdown for Krypton version ${KryptonPlatform.version}...")
        running = false
        ConnectionInitializer.shutdown()

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

        // Save bans and whitelist
        LOGGER.info("Saving ban list and whitelist...")
        playerManager.banManager.saveIfNeeded()
        playerManager.whitelistManager.saveIfNeeded()

        // Shut down plugins and unregister listeners
        LOGGER.info("Shutting down plugins and unregistering listeners...")
        eventManager.fireAndForgetSync(KryptonServerStopEvent)
        eventManager.shutdown()

        // Shut down scheduler
        scheduler.shutdown()
        LOGGER.info("Goodbye")

        // Manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
        LogManager.shutdown()
    }

    override fun close() {
        stopServer()
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

        private const val MILLISECONDS_PER_TICK = 50L // milliseconds in a tick
        private const val TICKS_PER_SECOND = 20
        private const val SAVE_PROFILE_CACHE_INTERVAL = 600
        private const val SLOW_TICK_THRESHOLD = 2000L
        private const val OVERLOAD_WARNING_INTERVAL = 15000L
        private const val NORMAL_TICK_TIME_NANOS = 1_000_000_000 / TICKS_PER_SECOND // 1,000,000,000 = 1 second in nanoseconds
        private const val NANOS_TO_MILLIS = 1000L * 1000L

        private val LOGGER = LogManager.getLogger()

        // This logic comes from vanilla. We should probably just use the main thread, though this may greater ensure parity
        // with vanilla's buggy mess. Not sure if this is actually the case though.
        @JvmStatic
        fun createAndRun(threadFunction: Function<Thread, KryptonServer>): KryptonServer {
            val reference = AtomicReference<KryptonServer>()
            val thread = Thread({ reference.get().run() }, "Server Thread")
            thread.setUncaughtExceptionHandler { _, exception -> LOGGER.error("Uncaught exception in server thread!", exception) }
            if (Runtime.getRuntime().availableProcessors() > 4) thread.priority = 8
            val server = threadFunction.apply(thread)
            reference.set(server)
            thread.start()
            return server
        }
    }
}
