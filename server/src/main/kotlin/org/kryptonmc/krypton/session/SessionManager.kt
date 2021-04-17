package org.kryptonmc.krypton.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.SERVER_UUID
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.api.event.events.login.JoinEvent
import org.kryptonmc.krypton.api.event.events.play.QuitEvent
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.entity.metadata.MovementFlags
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.toArea
import org.kryptonmc.krypton.extension.toProtocol
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.handlers.PlayHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.play.*
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerAction
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerInfo
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityDestroy
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityHeadLook
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.entity.spawn.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.space.Angle
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.random.Random

class SessionManager(private val server: KryptonServer) {

    val sessions: MutableSet<Session> = ConcurrentHashMap.newKeySet()

    private val keepAliveExecutor = Executors.newScheduledThreadPool(8, NamedThreadFactory("Keep Alive Thread #%d"))

    init {
        Runtime.getRuntime().addShutdownHook(Thread { keepAliveExecutor.shutdown() })
    }

    fun beginPlayState(session: Session) {
        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))
        session.handler = PlayHandler(server, this, session)

        val event = JoinEvent(session.player)
        server.eventBus.call(event)
        if (event.isCancelled) {
            session.disconnect(event.cancelledReason)
            return
        }

        val world = server.worldManager.default
        if (server.config.world.forceDefaultGamemode) world.gamemode = server.config.world.gamemode
        world.players += session.player
        server.players += session.player

        // load the player's data and populate the provided player with their loaded data
        server.playerDataManager.loadAndPopulate(world, session.player)
        if (server.config.world.forceDefaultGamemode) session.player.gamemode = server.config.world.gamemode
        val spawnLocation = session.player.location
        session.player.updateAbilities()

        session.currentState = PacketState.PLAY
        session.sendPacket(PacketOutJoinGame(
            session.id,
            server.config.world.hardcore,
            world,
            session.player.gamemode,
            null,
            session.player.dimension,
            server.config.status.maxPlayers,
            server.config.world.viewDistance
        ))
        session.sendPacket(PacketOutPluginMessage(BRAND_MESSAGE.first, BRAND_MESSAGE.second))
        session.sendPacket(PacketOutServerDifficulty(server.config.world.difficulty, true))

        val metadata = PlayerMetadata(
            MovementFlags(isFlying = session.player.abilities.isFlying),
            PlayerMetadata.airTicks,
            PlayerMetadata.customName,
            PlayerMetadata.isCustomNameVisible,
            PlayerMetadata.isSilent,
            PlayerMetadata.hasNoGravity,
            PlayerMetadata.pose,
            PlayerMetadata.handFlags,
            PlayerMetadata.health,
            PlayerMetadata.potionEffectColor,
            PlayerMetadata.isPotionEffectAmbient,
            PlayerMetadata.arrowsInEntity,
            PlayerMetadata.absorptionHealth,
            Optional(null), // setting this to null tells the client it's not sleeping
            PlayerMetadata.additionalHearts,
            PlayerMetadata.score,
            PlayerMetadata.skinFlags,
            PlayerMetadata.mainHand,
            PlayerMetadata.leftShoulderEntityData,
            PlayerMetadata.rightShoulderEntityData
        )

        session.sendPacket(PacketOutAbilities(session.player.abilities))
        session.sendPacket(PacketOutHeldItemChange(session.player.inventory.heldSlot))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags(server.tagManager))
        session.sendPacket(PacketOutEntityStatus(session.id))
        session.sendPacket(PacketOutDeclareCommands(server.commandManager.dispatcher.root))
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location))

        val joinMessage = translatable {
            key("multiplayer.player.joined")
            color(NamedTextColor.YELLOW)
            args(text { content(session.profile.name) })
        }
        val joinPacket = PacketOutChat(joinMessage, MessageType.SYSTEM, SERVER_UUID)
        session.sendPacket(joinPacket)
        server.console.sendMessage(joinMessage)

        session.sendPacket(PacketOutPlayerInfo(
            PlayerAction.UPDATE_LATENCY,
            listOf(PlayerInfo(latency = session.latency, profile = session.profile))
        ))

        val playerInfos = sessions.filter { it.currentState == PacketState.PLAY }.map {
            PlayerInfo(0, it.player.gamemode, it.profile, text { content(it.profile.name) })
        }
        if (playerInfos.isNotEmpty()) session.sendPacket(PacketOutPlayerInfo(PlayerAction.ADD_PLAYER, playerInfos))

        GlobalScope.launch(Dispatchers.IO) { handlePlayStateBegin(session, joinPacket) }

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                session.sendPacket(PacketOutSpawnPlayer(it.player))
                session.sendPacket(PacketOutEntityMetadata(it.id, metadata))
                session.sendPacket(PacketOutEntityProperties(it.id, session.player.attributes))
                session.sendPacket(PacketOutEntityHeadLook(it.id, it.player.location.yaw.toAngle()))
            }

        val centerChunk = ChunkPosition(floor(spawnLocation.x / 16.0).toInt(), floor(spawnLocation.z / 16.0).toInt())
        session.sendPacket(PacketOutUpdateViewPosition(centerChunk))

        GlobalScope.launch(Dispatchers.IO) {
            val positionsToLoad = mutableListOf<ChunkPosition>()
            repeat(server.config.world.viewDistance.toArea()) {
                positionsToLoad += server.worldManager.chunkInSpiral(it, centerChunk.x, centerChunk.z)
            }

            server.worldManager.loadChunks(world, positionsToLoad).forEach { chunk ->
                session.sendPacket(PacketOutUpdateLight(chunk))
                session.sendPacket(PacketOutChunkData(chunk))
            }

            if (session.player.inventory.items.isNotEmpty()) {
                session.sendPacket(PacketOutWindowItems(session.player.inventory))
            }
        }

        session.sendPacket(PacketOutEntityMetadata(session.id, metadata))
        session.sendPacket(PacketOutWorldBorder(BorderAction.INITIALIZE, world.border))
        session.sendPacket(PacketOutTimeUpdate(world.time, world.dayTime))
        session.sendPacket(PacketOutSpawnPosition(spawnLocation))
        session.sendPacket(PacketOutEntityProperties(session.id, session.player.attributes))
        if (world.isRaining) session.sendPacket(PacketOutChangeGameState(GameState.BEGIN_RAINING))

        ServerStorage.PLAYER_COUNT.getAndIncrement()

        keepAliveExecutor.scheduleAtFixedRate({
            val keepAliveId = System.currentTimeMillis()
            session.lastKeepAliveId = keepAliveId
            session.sendPacket(PacketOutKeepAlive(keepAliveId))
        }, 0, 20, TimeUnit.SECONDS)
    }

    private fun handlePlayStateBegin(session: Session, joinPacket: PacketOutChat) {
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.ADD_PLAYER,
            listOf(
                PlayerInfo(
                    0,
                    session.player.gamemode,
                    session.profile,
                    text { content(session.profile.name) }
                )
            )
        )
        val spawnPlayerPacket = PacketOutSpawnPlayer(session.player)
        val metadataPacket = PacketOutEntityMetadata(session.id, PlayerMetadata)
        val propertiesPacket = PacketOutEntityProperties(session.id, DEFAULT_PLAYER_ATTRIBUTES)
        val headLookPacket = PacketOutEntityHeadLook(session.id, Angle.ZERO)

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(joinPacket)
                it.sendPacket(infoPacket)
                it.sendPacket(spawnPlayerPacket)
                it.sendPacket(metadataPacket)
                it.sendPacket(propertiesPacket)
                it.sendPacket(headLookPacket)
            }
    }

    fun handleDisconnection(session: Session) {
        if (session.currentState != PacketState.PLAY) return

        GlobalScope.launch {
            server.eventBus.call(QuitEvent(session.player))
            server.playerDataManager.save(session.player)
        }

        val destroyPacket = PacketOutEntityDestroy(listOf(session.id))
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.REMOVE_PLAYER,
            listOf(PlayerInfo(profile = session.profile))
        )

        sendPackets(destroyPacket, infoPacket) { it != session && it.currentState == PacketState.PLAY }
        server.sendMessage(translatable {
            key("multiplayer.player.left")
            color(NamedTextColor.YELLOW)
            args(text { content(session.profile.name) })
        })
        ServerStorage.PLAYER_COUNT.getAndDecrement()
    }

    fun updateLatency(session: Session, latency: Int) {
        session.latency = latency
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.UPDATE_LATENCY,
            listOf(PlayerInfo(latency, profile = session.profile))
        )

        sessions.asSequence()
            .filter { it.currentState == PacketState.PLAY }
            .forEach { it.sendPacket(infoPacket) }
    }

    fun sendPackets(vararg packets: Packet, predicate: (Session) -> Boolean = { true }) {
        sessions.asSequence().filter(predicate).forEach { session -> packets.forEach(session::sendPacket) }
    }

    fun shutdown() {
        if (sessions.isEmpty()) return
        val reason = translatable { key("multiplayer.disconnect.server_shutdown") }
        sessions.forEach { it.disconnect(reason) }
    }

    companion object {

        private val BRAND_MESSAGE = NamespacedKey(value = "brand") to "Krypton".toProtocol()
        private val LOGGER = logger<SessionManager>()
    }
}