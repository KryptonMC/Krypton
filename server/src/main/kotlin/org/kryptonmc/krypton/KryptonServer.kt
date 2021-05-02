package org.kryptonmc.krypton

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.event.events.ticking.TickEndEvent
import org.kryptonmc.krypton.api.event.events.ticking.TickStartEvent
import org.kryptonmc.krypton.api.status.StatusInfo
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.console.ConsoleSender
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.event.KryptonEventBus
import org.kryptonmc.krypton.packet.PacketLoader
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.packet.session.SessionManager
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.server.KryptonConfig
import org.kryptonmc.krypton.server.gui.KryptonServerGUI
import org.kryptonmc.krypton.server.query.GS4QueryHandler
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.util.TranslationRegister
import org.kryptonmc.krypton.util.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.util.copyTo
import org.kryptonmc.krypton.util.isRegularFile
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.monitoring.jmx.KryptonStatistics
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.file.Path
import java.security.SecureRandom
import java.util.Locale
import java.util.Properties
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

class KryptonServer(private val disableGUI: Boolean) : Server {

    override val info = KryptonServerInfo

    internal val config = loadConfig()

    override val status = KryptonStatusInfo(config.status.maxPlayers, config.status.motd)

    override val isOnline = config.server.onlineMode
    override val isHardcore = config.world.hardcore
    override val difficulty = config.world.difficulty
    override val gamemode = config.world.gamemode

    override val address = InetSocketAddress(config.server.ip, config.server.port)

    override val players = mutableSetOf<KryptonPlayer>()

    override fun player(uuid: UUID) = players.firstOrNull { it.uuid == uuid }
    override fun player(name: String) = players.firstOrNull { it.name == name }

    override val console = ConsoleSender(this)

    override var scoreboard: KryptonScoreboard? = null; private set

    private val nettyProcess = NettyProcess(this)
    internal val random: SecureRandom = SecureRandom()

    val sessionManager = SessionManager(this)

    override val worldManager = KryptonWorldManager(this, config.world.name, config.advanced.synchronizeChunkWrites)
    val playerDataManager = PlayerDataManager(File(worldManager.folder, "/playerdata").apply { mkdir() })

    override val commandManager = KryptonCommandManager(this)
    override val eventBus = KryptonEventBus()

    override val scheduler = KryptonScheduler

    override val pluginManager = KryptonPluginManager(this)
    override val servicesManager = KryptonServicesManager()

    @Volatile internal var lastTickTime = 0L; private set
    private var lastOverloadWarning = 0L
    private var tickCount = 0

    internal val tickTimes = LongArray(100)
    internal var averageTickTime = 0F; private set

    lateinit var mainThread: Thread private set
    private val tickScheduler = Executors.newSingleThreadScheduledExecutor { Thread(it, "Tick Scheduler").apply { mainThread = this } }
    val tickables = mutableListOf<Runnable>()

    @Volatile internal var isRunning = true; private set

    private var gs4QueryHandler: GS4QueryHandler? = null
    private var watchdog: WatchdogProcess? = null

    internal fun start() {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()
        // loading these here avoids loading them when the first player joins
        Class.forName("org.kryptonmc.krypton.registry.Registries")
        Class.forName("org.kryptonmc.krypton.registry.tags.TagManager")
        Class.forName("org.kryptonmc.krypton.world.block.palette.GlobalPalette")

        LOGGER.debug("Starting console handler")
        Thread(KryptonConsole(this)::start, "Console Handler").apply {
            uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(logger("CONSOLE"))
            isDaemon = true
        }.start()

        LOGGER.debug("Loading packets...")
        PacketLoader.loadAll()

        LOGGER.debug("Registering commands and console translations...")
        commandManager.registerBuiltins()
        TranslationRegister.initialize()

        LOGGER.debug("Starting Netty...")
        GlobalScope.launch(Dispatchers.IO) {
            nettyProcess.run()
        }

        LOGGER.debug("Starting bStats metrics")
        KryptonMetrics.initialize(this, config.other.metrics)

        if (!config.server.onlineMode) {
            LOGGER.warn("-----------------------------------------------------------------------------------")
            LOGGER.warn("SERVER IS IN OFFLINE MODE! THIS SERVER WILL MAKE NO ATTEMPTS TO AUTHENTICATE USERS!")
            LOGGER.warn("While this may allow players without full Minecraft accounts to connect, it also allows hackers to connect with any username they choose! Beware!")
            LOGGER.warn("To get rid of this message, change online-mode to true in config.conf")
            LOGGER.warn("-----------------------------------------------------------------------------------")
        }
        pluginManager.initialize()

        lastTickTime = System.currentTimeMillis()
        if (config.advanced.enableJmxMonitoring) KryptonStatistics.register(this)

        if (config.other.timeoutTime > 0) {
            LOGGER.debug("Starting watchdog")
            watchdog = WatchdogProcess(this)
            watchdog?.start()
        }

        if (config.query.enabled) {
            LOGGER.info("Starting GS4 status listener")
            gs4QueryHandler = GS4QueryHandler.create(this)
        }
        if (!disableGUI && !GraphicsEnvironment.isHeadless()) KryptonServerGUI.open(this)

        Runtime.getRuntime().addShutdownHook(Thread(::stop, "Shutdown Handler").apply { isDaemon = false })

        LOGGER.info("Done (${"%.3fs".format(Locale.ROOT, (System.nanoTime() - startTime) / 1.0E9)})! Type \"help\" for help.")
        watchdog?.tick(System.currentTimeMillis())

        tickScheduler.scheduleAtFixedRate({
            if (!isRunning) return@scheduleAtFixedRate

            val nextTickTime = System.currentTimeMillis() - lastTickTime
            if (nextTickTime > 2000L && lastTickTime - lastOverloadWarning >= 15000L) {
                LOGGER.warn("Woah there! Can't keep up! Running ${nextTickTime}ms (${nextTickTime / 50} ticks) behind!")
                lastOverloadWarning = lastTickTime
            }
            // start tick
            eventBus.call(TickStartEvent(tickCount))
            val tickTime = measureTimeMillis(::tick)
            val finishTime = System.currentTimeMillis()

            // store historical tick time and update average
            tickTimes[tickCount % 100] = tickTime
            averageTickTime = averageTickTime * 0.8F + tickTime * 0.19999999F

            // end tick
            eventBus.call(TickEndEvent(tickCount, tickTime, finishTime))
            lastTickTime = finishTime
            watchdog?.tick(finishTime)
        }, 0, TICK_INTERVAL, TimeUnit.MILLISECONDS)
    }

    private fun tick() {
        tickCount++
        if (players.isEmpty()) return // don't tick if there are no players on the server

        worldManager.worlds.forEach { (_, world) ->
            if (tickCount % 20 == 0) {
                val timePacket = PacketOutTimeUpdate(world.time, world.dayTime)
                sessionManager.sessions.asSequence()
                    .filter { it.currentState == PacketState.PLAY }
                    .filter { it.player.world == world }
                    .forEach { it.sendPacket(timePacket) }
            }
            world.tick()
        }
        if (config.world.autosaveInterval > 0 && tickCount % config.world.autosaveInterval == 0) {
            LOGGER.info("Autosave started")
            GlobalScope.launch(Dispatchers.IO) { worldManager.saveAll(true) }
        }
        tickables.forEach { it.run() }
    }

    override fun broadcast(message: Component, permission: String?) {
        if (permission != null) {
            players.filter { it.hasPermission(permission) }.forEach { it.sendMessage(message) }
            console.sendMessage(message)
            return
        }
        sendMessage(message)
    }

    override fun audiences() = players + console

    private fun loadConfig(): KryptonConfig {
        val configFile = File(CURRENT_DIRECTORY, "config.conf")
        if (!configFile.exists()) {
            val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("config.conf")
                ?: throw IOException("Config file not in classpath! Something has gone horribly wrong!")
            inputStream.copyTo(configFile)
        }

        return HOCON.decodeFromConfig(ConfigFactory.parseFile(configFile))
    }

    internal fun stop(halt: Boolean = true) {
        if (!isRunning) return // Ensure we cannot accidentally run this twice

        // stop server and shut down session manager (disconnecting all players)
        LOGGER.info("Stopping Krypton...")
        isRunning = false
        sessionManager.shutdown()
        nettyProcess.shutdown()
        gs4QueryHandler?.stop()
        watchdog?.shutdown()

        // save player, world and region data
        LOGGER.info("Saving player, world and region data...")
        worldManager.saveAll()
        players.forEach { playerDataManager.save(it) }

        // shut down plugins and unregister listeners
        LOGGER.info("Shutting down plugins...")
        pluginManager.shutdown()
        eventBus.unregisterAll()

        // shut down schedulers
        scheduler.shutdown()
        tickScheduler.shutdownNow()
        LOGGER.info("Goodbye")

        // manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
        LogManager.shutdown()

        // Finally, halt the JVM if we should (avoids shutdown hooks running)
        if (halt) Runtime.getRuntime().halt(0)
    }

    internal fun restart() {
        stop(false) // avoid halting there because we halt here
        val split = config.other.restartScript.split(" ")
        if (split.isNotEmpty()) {
            if (!Path.of(split[0]).isRegularFile) {
                println("Unable to find restart script ${split[0]}! Refusing to restart.")
                return
            }
            println("Attempting to restart the server with script ${split[0]}...")
            val os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH)
            Runtime.getRuntime().exec((if ("win" in os) "cmd /c start " else "sh ") + config.other.restartScript)
        }
        Runtime.getRuntime().halt(0)
    }

    object KryptonServerInfo : Server.ServerInfo {

        override val name = "Krypton"
        override val version: String
        override val minecraftVersion: String

        init {
            val infoProperties = Properties().apply {
                load(Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/krypton.properties"))
            }
            version = infoProperties.getProperty("krypton.version")
            minecraftVersion = infoProperties.getProperty("minecraft.version")
        }
    }

    companion object {

        private const val TICK_INTERVAL = 1000L / 20L // milliseconds in a tick
        private val HOCON = Hocon {}
        private val LOGGER = logger<KryptonServer>()
    }
}

data class KryptonStatusInfo(
    override val maxPlayers: Int,
    override val motd: Component
) : StatusInfo

val CURRENT_DIRECTORY: File = Path.of("").toAbsolutePath().toFile()
