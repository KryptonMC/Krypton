package org.kryptonmc.krypton

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.*
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.status.StatusInfo
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.console.ConsoleScope
import org.kryptonmc.krypton.console.ConsoleSender
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.encryption.Encryption
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.event.KryptonEventBus
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.PacketLoader
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.RegistryManager
import org.kryptonmc.krypton.registry.tags.TagManager
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.session.SessionManager
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.security.SecureRandom
import java.util.*

class KryptonServer : Server {

    override val info = KryptonServerInfo

    internal val config = loadConfig()

    override val status = KryptonStatusInfo(config.status.maxPlayers, config.status.motd)

    override val isOnline = config.server.onlineMode
    override val isHardcore = config.world.hardcore
    override val difficulty = config.world.difficulty
    override val gamemode = config.world.gamemode

    override lateinit var address: InetSocketAddress

    override val players = mutableSetOf<KryptonPlayer>()

    override fun player(uuid: UUID) = players.firstOrNull { it.uuid == uuid }
    override fun player(name: String) = players.firstOrNull { it.name == name }

    override val console = ConsoleSender(this)

    override var scoreboard: KryptonScoreboard? = null

    internal val encryption = Encryption()
    private val nettyProcess = NettyProcess(this)
    internal val random: SecureRandom = SecureRandom()

    val sessionManager = SessionManager(this)

    val registryManager = RegistryManager()
    val tagManager = TagManager()

    override val worldManager = KryptonWorldManager(this, config.world)

    override val commandManager = KryptonCommandManager(this)
    override val eventBus = KryptonEventBus()

    override val scheduler = KryptonScheduler

    override lateinit var pluginManager: KryptonPluginManager

    fun start() = runBlocking(Dispatchers.IO) {
        LOGGER.info("Starting Krypton server on ${config.server.ip}:${config.server.port}...")
        val startTime = System.nanoTime()

        ConsoleScope.launch {
            LOGGER.debug("Starting console handler")
            KryptonConsole(this@KryptonServer).start()
        }

        LOGGER.debug("Loading packets...")
        PacketLoader.loadAll()

        LOGGER.debug("Starting Netty...")
        val nettyJob = GlobalScope.launch(Dispatchers.IO) {
            nettyProcess.run()
        }

        if (!config.server.onlineMode) {
            LOGGER.warn("-----------------------------------------------------------------------------------")
            LOGGER.warn("SERVER IS IN OFFLINE MODE! THIS SERVER WILL MAKE NO ATTEMPTS TO AUTHENTICATE USERS!")
            LOGGER.warn("While this may allow players without full Minecraft accounts to connect, it also allows hackers to connect with any username they choose! Beware!")
            LOGGER.warn("To get rid of this message, change online_mode to true in config.conf")
            LOGGER.warn("-----------------------------------------------------------------------------------")
        }

        GlobalScope.launch(Dispatchers.IO) {
            LOGGER.info("Loading plugins...")
            pluginManager = KryptonPluginManager(this@KryptonServer)
            pluginManager.initialise()
            LOGGER.info("Plugin loading done!")
            LOGGER.info("Done (${"%.3fs".format(Locale.ROOT, (System.nanoTime() - startTime) / 1.0E9)})! Type \"help\" for help.")
        }

        Runtime.getRuntime().addShutdownHook(Thread({
            LOGGER.info("Stopping Krypton...")
            LOGGER.debug("Shutting down scheduler...")
            scheduler.shutdown()
            LOGGER.info("Shutting down plugins...")
            pluginManager.shutdown()
            LOGGER.info("Goodbye")
        }, "Shutdown Handler"))

        nettyJob.join()
    }

    override fun broadcast(message: Component, permission: String?) {
        if (permission != null) players.filter { it.hasPermission(permission) }.forEach { it.sendMessage(message) }
        else sendMessage(message)
    }

    override fun audiences() = players

    private fun loadConfig(): KryptonConfig {
        val configFile = File(CURRENT_DIRECTORY, "config.conf")
        if (!configFile.exists()) {
            val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("config.conf")
                ?: throw IOException("Config file not in classpath! Something has gone horribly wrong!")
            Files.copy(inputStream, configFile.toPath())
        }

        return HOCON.decodeFromConfig(ConfigFactory.parseFile(configFile))
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

        private val HOCON = Hocon {}

        private val LOGGER = logger<KryptonServer>()
    }
}

data class KryptonStatusInfo(
    override val maxPlayers: Int,
    override val motd: Component
) : StatusInfo

val CURRENT_DIRECTORY: File = Path.of("").toAbsolutePath().toFile()