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

import com.google.gson.Gson
import kotlinx.coroutines.launch
import me.bardy.gsonkt.newBuilder
import me.bardy.gsonkt.registerTypeAdapter
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.event.server.ServerStopEvent
import org.kryptonmc.api.event.server.TickEndEvent
import org.kryptonmc.api.event.server.TickStartEvent
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileCache
import org.kryptonmc.krypton.auth.MojangUUIDTypeAdapter
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.config.KryptonConfig
import org.kryptonmc.krypton.console.KryptonConsole
import org.kryptonmc.krypton.item.KryptonItemManager
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.locale.MetadataResponse
import org.kryptonmc.krypton.locale.TranslationRepository
import org.kryptonmc.krypton.network.PacketLoader
import org.kryptonmc.krypton.pack.repository.PackRepository
import org.kryptonmc.krypton.packet.out.play.PacketOutServerDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.plugin.KryptonEventManager
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.registry.json.RegistryBlock
import org.kryptonmc.krypton.registry.json.RegistryBlockState
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.serializers.DifficultySerializer
import org.kryptonmc.krypton.serializers.GamemodeSerializer
import org.kryptonmc.krypton.server.PlayerManager
import org.kryptonmc.krypton.server.ServerResources
import org.kryptonmc.krypton.service.KryptonServicesManager
import org.kryptonmc.krypton.util.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.util.createDirectory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorldManager
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.kryptonmc.krypton.world.storage.WorldDataAccess
import org.kryptonmc.nbt.Tag
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
    val registryHolder: RegistryHolder,
    val dataAccess: WorldDataAccess,
    val worldData: PrimaryWorldData,
    val packRepository: PackRepository,
    val resources: ServerResources,
    val config: KryptonConfig,
    override val profileCache: KryptonProfileCache,
    ops: RegistryReadOps<Tag>,
    private val configPath: Path,
    worldFolder: Path
) : Server {

    override val platform = KryptonPlatform
    override val maxPlayers = config.status.maxPlayers
    override val motd = config.status.motd

    override val isOnline = config.server.onlineMode
    override var isHardcore: Boolean
        get() = worldData.isHardcore
        set(value) { worldData.isHardcore = value }
    override var difficulty: Difficulty
        get() = worldData.difficulty
        set(value) {
            worldData.difficulty = value
            // TODO: Update mob spawning flags
            playerManager.players.forEach { it.session.sendPacket(PacketOutServerDifficulty(it.world.difficulty)) }
        }
    override var gamemode: Gamemode
        get() = worldData.gamemode
        set(value) { worldData.gamemode = value }

    override val address = InetSocketAddress(config.server.ip, config.server.port)

    val playerManager = PlayerManager(this)
    override val players = playerManager.players

    override fun player(uuid: UUID) = playerManager.playersByUUID[uuid]
    override fun player(name: String) = playerManager.playersByName[name]

    override val channels: MutableSet<Key> = ConcurrentHashMap.newKeySet()
    override val console = KryptonConsole(this)
    override var scoreboard: KryptonScoreboard? = null
        private set

    private val nettyProcess = NettyProcess(this)
    val random = SecureRandom()

    override val worldManager = KryptonWorldManager(this, dataAccess, worldData, ops, worldFolder)
    override val commandManager = KryptonCommandManager()
    override val pluginManager = KryptonPluginManager(this)
    override val eventManager = KryptonEventManager(pluginManager)
    override val servicesManager = KryptonServicesManager
    override val registryManager = KryptonRegistryManager
    override val blockManager = KryptonBlockManager
    override val itemManager = KryptonItemManager
    override val scheduler = KryptonScheduler(pluginManager)

    @Volatile
    var isRunning = true
        private set
    @Volatile
    private var lastTickTime = 0L

    private var lastOverloadWarning = 0L
    private var tickCount = 0
    private var oversleepFactor = 0L

    fun start() {
        Messages.START.INITIAL.info(LOGGER, config.server.ip, config.server.port)
        val startTime = System.nanoTime()

        // Load the packets
        LOGGER.debug("Loading packets...")
        PacketLoader.loadAll()

        // Register the commands and console translations, and schedule a refresh of the translation repository
        // (query data.kryptonmc.org for translation metadata and download the new translations)
        LOGGER.debug("Registering commands and translations...")
        commandManager.registerBuiltins()
        TranslationRepository.scheduleRefresh()

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

        // Warn about piracy being unsupported and then load plugins
        if (!config.server.onlineMode) {
            LOGGER.info("-----------------------------------------------------------------------------------")
            Messages.PIRACY_WARNING.info(LOGGER)
            LOGGER.info("-----------------------------------------------------------------------------------")
        }

        LOGGER.info("Preparing world ${config.world.name}...")
        worldManager.init()

        loadPlugins()

        // Fire the event that signals the server starting. We fire it here so that plugins can listen to it as part
        // of their lifecycle, and we call it sync so plugins can finish initialising before we do
        eventManager.fireAndForgetSync(ServerStartEvent())

        // Initialize console permissions after plugins are loaded
        console.setupPermissions()

        // Set the last tick time (avoids initial check sending an overload warning every time)
        lastTickTime = System.currentTimeMillis()

        // Start accepting connections
        LOGGER.debug("Starting Netty...")
        IOScope.launch { nettyProcess.run() }

        // Add the shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(Thread(::stop, "Shutdown Handler").apply { isDaemon = false })

        Messages.START.DONE.info(LOGGER, "%.3fs".format(Locale.ROOT, (System.nanoTime() - startTime) / 1.0E9))

        // Start up the console handler
        LOGGER.debug("Starting console handler")
        Thread(console::start, "Console Handler").apply {
            uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(KryptonConsole.LOGGER)
            isDaemon = true
        }.start()

        run()
    }

    fun run() = try {
        while (isRunning) {
            val nextTickTime = System.currentTimeMillis() - lastTickTime
            if (nextTickTime > 2000L && lastTickTime - lastOverloadWarning >= 15_000L) {
                Messages.TICK_OVERLOAD_WARNING.warn(LOGGER, nextTickTime, nextTickTime / 50)
                lastOverloadWarning = lastTickTime
            }
            // tick
            eventManager.fireAndForgetSync(TickStartEvent(tickCount))
            val tickTime = measureTimeMillis(::tick)

            // end tick
            val finishTime = System.currentTimeMillis()
            eventManager.fireAndForgetSync(TickEndEvent(tickCount, tickTime, finishTime))
            lastTickTime = finishTime

            val sleepTime = measureTimeMillis { Thread.sleep(max(0, TICK_INTERVAL - tickTime - oversleepFactor)) }
            oversleepFactor = sleepTime - (TICK_INTERVAL - tickTime)
        }
    } catch (exception: Throwable) {
        LOGGER.error("Encountered an unexpected exception", exception)
    } finally {
        restart()
    }

    private fun loadPlugins() {
        Messages.PLUGIN.LOAD.INITIAL.info(LOGGER)

        try {
            val pluginPath = Path.of("plugins")
            if (!pluginPath.exists()) pluginPath.createDirectory()
            if (!pluginPath.isDirectory()) {
                Messages.PLUGIN.NOT_DIRECTORY.warn(LOGGER, pluginPath)
                return
            }

            pluginManager.loadPlugins(pluginPath)
        } catch (exception: Exception) {
            Messages.PLUGIN.LOAD.FAIL.error(LOGGER, exception)
        }

        pluginManager.plugins.forEach {
            val instance = it.instance ?: return@forEach
            try {
                eventManager.registerUnchecked(it, instance)
            } catch (exception: Exception) {
                LOGGER.error("Unable to register plugin listener for plugin ${it.description.name}!", exception)
            }
        }

        Messages.PLUGIN.LOAD.DONE.info(LOGGER, pluginManager.plugins.size)
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
            Messages.AUTOSAVE.STARTED.info(LOGGER)
            worldManager.saveAll()
            Messages.AUTOSAVE.FINISHED.info(LOGGER)
        }
    }

    fun updateConfig() {
        val config = HoconConfigurationLoader.builder()
            .path(configPath)
            .defaultOptions(KryptonConfig.OPTIONS)
            .build()
        val node = config.load()
            .set(this.config)
        config.save(node)
    }

    fun overworld() = worldManager.worlds[World.OVERWORLD]!!

    fun worldResource(resource: WorldResource) = dataAccess.resourcePath(resource)

    override fun registerChannel(channel: Key) {
        require(channel !in RESERVED_CHANNELS) { "Cannot register reserved channel with name \"minecraft:register\" or \"minecraft:unregister\"!" }
        channels += channel
    }

    override fun unregisterChannel(channel: Key) {
        channels -= channel
        require(channel !in RESERVED_CHANNELS) { "Cannot unregister reserved channels with name \"minecraft:register\" or \"minecraft:unregister\"!" }
    }

    override fun sendMessage(message: Component, permission: String) {
        playerManager.players.filter { it.hasPermission(permission) }.forEach { it.sendMessage(message) }
        console.sendMessage(message)
        return
    }

    override fun audiences() = players + console

    fun getPermissionLevel(profile: KryptonGameProfile) = if (playerManager.ops.contains(profile)) playerManager.ops[profile]?.permissionLevel ?: 1 else 1

    fun stop(halt: Boolean = true) {
        if (!isRunning) return // Ensure we cannot accidentally run this twice

        // stop server and shut down session manager (disconnecting all players)
        Messages.STOP.INITIAL.info(LOGGER)
        isRunning = false
        playerManager.disconnectAll()

        // save data
        Messages.STOP.SAVE.info(LOGGER)
        worldManager.saveAll()
        resources.close()
        dataAccess.close()
        playerManager.saveAll()

        // shut down plugins and unregister listeners
        Messages.STOP.PLUGINS.info(LOGGER)
        eventManager.fireAndForgetSync(ServerStopEvent())
        eventManager.shutdown()

        // shut down scheduler
        scheduler.shutdown()
        Messages.STOP.GOODBYE.info(LOGGER)

        // manually shut down Log4J 2 here so it doesn't shut down before we've finished logging
        LogManager.shutdown()

        // Finally, halt the JVM if we should (avoids shutdown hooks running)
        if (halt) Runtime.getRuntime().halt(0)
    }

    fun restart() {
        stop(false) // avoid halting there because we halt here
        val split = config.other.restartScript.split(" ")
        if (split.isNotEmpty()) {
            if (!Path.of(split[0]).isRegularFile()) {
                Messages.RESTART.NO_SCRIPT.print(split[0])
                Runtime.getRuntime().halt(0)
            }
            Messages.RESTART.ATTEMPT.print(split[0])
            val os = System.getProperty("os.name").lowercase()
            Runtime.getRuntime().exec((if ("win" in os) "cmd /c start " else "sh ") + config.other.restartScript)
        }
        Runtime.getRuntime().halt(0)
    }

    companion object {

        private val REGISTER_CHANNEL_KEY = key("register")
        private val UNREGISTER_CHANNEL_KEY = key("unregister")
        private val RESERVED_CHANNELS = setOf(REGISTER_CHANNEL_KEY, UNREGISTER_CHANNEL_KEY)

        private const val TICK_INTERVAL = 1000L / 20L // milliseconds in a tick
        private val LOGGER = logger<KryptonServer>()
    }
}

val CURRENT_DIRECTORY: Path = Path.of("").toAbsolutePath()

val GSON: Gson = GsonComponentSerializer.gson().serializer().newBuilder {
    registerTypeAdapter<UUID>(MojangUUIDTypeAdapter)
    registerTypeAdapter<RegistryBlock>(RegistryBlock.Companion)
    registerTypeAdapter<RegistryBlockState>(RegistryBlockState.Companion)
    registerTypeAdapter<Gamemode>(GamemodeSerializer)
    registerTypeAdapter<Difficulty>(DifficultySerializer)
    registerTypeAdapter<MetadataResponse>(MetadataResponse)
}
