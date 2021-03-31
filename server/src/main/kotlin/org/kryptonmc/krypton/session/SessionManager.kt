package org.kryptonmc.krypton.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.*
import org.kryptonmc.krypton.api.event.events.login.JoinEvent
import org.kryptonmc.krypton.api.event.events.play.QuitEvent
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.encryption.Encryption.Companion.SHARED_SECRET_ALGORITHM
import org.kryptonmc.krypton.encryption.toDecryptingCipher
import org.kryptonmc.krypton.encryption.toEncryptingCipher
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.event.events.play.PluginMessageEvent
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.entity.entities.data.PlayerData
import org.kryptonmc.krypton.entity.metadata.MovementFlags
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.toArea
import org.kryptonmc.krypton.extension.toProtocol
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.*
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.*
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityDestroy
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityHeadLook
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.entity.spawn.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.packet.transformers.*
import org.kryptonmc.krypton.space.Angle
import org.kryptonmc.krypton.space.toAngle
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey
import kotlin.math.floor
import kotlin.random.Random

class SessionManager(private val server: KryptonServer) {

    val sessions: MutableSet<Session> = ConcurrentHashMap.newKeySet()

    private val handler = PacketHandler(this, server)

    private val keepAliveExecutor = Executors.newScheduledThreadPool(8, NamedThreadFactory("Keep Alive Thread #%d"))

    init {
        Runtime.getRuntime().addShutdownHook(Thread { keepAliveExecutor.shutdown() })
    }

    fun handle(session: Session, packet: Packet) = handler.handle(session, packet)

    fun beginPlayState(session: Session) {
        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))

        val event = JoinEvent(session.player)
        server.eventBus.call(event)
        if (event.isCancelled) {
            session.sendPacket(PacketOutDisconnect(event.cancelledReason))
            session.disconnect()
            return
        }

        val playerData = server.worldManager.loadPlayerData(session.profile.uuid)

        val world = server.worldManager.worlds.getValue(server.config.world.name)
        world.gamemode = server.config.world.gamemode
        world.players += session.player
        server.players += session.player
        session.player.world = world

        session.player.gamemode = playerData?.gamemode ?: world.gamemode

        val pitch = playerData?.rotationY ?: 0.0F
        val yaw = playerData?.rotationX ?: 0.0F
        val spawnLocation = playerData?.position?.toLocation(world, yaw, pitch) ?: world.spawnLocation
        session.player.location = spawnLocation

        session.currentState = PacketState.PLAY

        session.sendPacket(PacketOutJoinGame(
            session.id,
            server.config.world.hardcore,
            world,
            session.player.gamemode,
            playerData?.previousGamemode,
            playerData?.dimension,
            server.registryManager.dimensions,
            server.registryManager.biomes,
            server.config.status.maxPlayers,
            server.config.world.viewDistance
        ))
        val pluginMessage = NamespacedKey(value = "brand") to "Krypton".toProtocol()
        session.sendPacket(PacketOutPluginMessage(pluginMessage.first, pluginMessage.second))
        server.eventBus.call(PluginMessageEvent(session.player, pluginMessage.first, pluginMessage.second))

        session.sendPacket(PacketOutServerDifficulty(server.config.world.difficulty, true))

        val abilities = when (world.gamemode) {
            Gamemode.SURVIVAL, Gamemode.ADVENTURE -> Abilities()
            Gamemode.CREATIVE -> Abilities(isInvulnerable = true, isFlyingAllowed = true, isCreativeMode = true)
            Gamemode.SPECTATOR -> Abilities(isInvulnerable = true, canFly = true, isFlyingAllowed = true)
        }
        session.player.abilities = abilities

        val metadata = PlayerMetadata(
            MovementFlags(isFlying = playerData?.abilities?.isFlying ?: false),
            playerData?.air?.toInt() ?: PlayerMetadata.airTicks,
            PlayerMetadata.customName,
            PlayerMetadata.isCustomNameVisible,
            PlayerMetadata.isSilent,
            PlayerMetadata.hasNoGravity,
            PlayerMetadata.pose,
            PlayerMetadata.handFlags,
            playerData?.health ?: PlayerMetadata.health,
            PlayerMetadata.potionEffectColor,
            PlayerMetadata.isPotionEffectAmbient,
            PlayerMetadata.arrowsInEntity,
            PlayerMetadata.absorptionHealth,
            Optional(null), // setting this to null tells the client it's not sleeping
            PlayerMetadata.additionalHearts,
            playerData?.score ?: PlayerMetadata.score,
            PlayerMetadata.skinFlags,
            PlayerMetadata.mainHand,
            PlayerMetadata.leftShoulderEntityData,
            PlayerMetadata.rightShoulderEntityData
        )

        session.sendPacket(PacketOutAbilities(abilities))
        session.sendPacket(PacketOutHeldItemChange(playerData?.selectedItemSlot ?: 0))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags(server.registryManager, server.tagManager))
        session.sendPacket(PacketOutEntityStatus(session.id))
        session.sendPacket(PacketOutDeclareCommands(server.commandManager.dispatcher.root))
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutPlayerPositionAndLook(spawnLocation, teleportId = Random.nextInt(1000)))

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

        GlobalScope.launch(Dispatchers.IO) { handlePlayStateBegin(session) }

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                session.sendPacket(joinPacket)
                session.sendPacket(PacketOutSpawnPlayer(it.player))
                session.sendPacket(PacketOutEntityMetadata(it.id, metadata))
                session.sendPacket(PacketOutEntityProperties(it.id, playerData?.attributes ?: DEFAULT_PLAYER_ATTRIBUTES))
                session.sendPacket(PacketOutEntityHeadLook(it.id, it.player.location.yaw.toAngle()))
            }

        val centerChunk = Vector(floor(spawnLocation.x / 16.0), 0.0, floor(spawnLocation.z / 16.0))

        session.sendPacket(PacketOutUpdateViewPosition(centerChunk))

        GlobalScope.launch(Dispatchers.IO) {
            val positionsToLoad = mutableListOf<Vector>()
            repeat(server.config.world.viewDistance.toArea()) {
                positionsToLoad += server.worldManager.chunkInSpiral(it, centerChunk.x, centerChunk.z)
            }

            server.worldManager.loadChunks(positionsToLoad).forEach { (_, value) ->
                session.sendPacket(PacketOutUpdateLight(value))
                session.sendPacket(PacketOutChunkData(value))
            }
        }

        session.sendPacket(PacketOutEntityMetadata(session.id, metadata))
        session.sendPacket(PacketOutWorldBorder(BorderAction.INITIALIZE, world.border))
        session.sendPacket(PacketOutTimeUpdate(world.time, world.dayTime))
        session.sendPacket(PacketOutSpawnPosition(spawnLocation))
        session.sendPacket(PacketOutEntityProperties(session.id, playerData?.attributes ?: DEFAULT_PLAYER_ATTRIBUTES))
        if (world.isRaining) session.sendPacket(PacketOutChangeGameState(GameState.BEGIN_RAINING))

        session.player.data = playerData ?: createPlayerData(session.player)
        ServerStorage.PLAYER_COUNT.getAndIncrement()

        keepAliveExecutor.scheduleAtFixedRate({
            val keepAliveId = System.currentTimeMillis()
            session.lastKeepAliveId = keepAliveId
            session.sendPacket(PacketOutKeepAlive(keepAliveId))
        }, 0, 20, TimeUnit.SECONDS)
    }

    fun verifyToken(session: Session, expected: ByteArray, actual: ByteArray) {
        val decryptedActual = server.encryption.decrypt(actual)
        require(decryptedActual.contentEquals(expected)) {
            LOGGER.warn("${session.player.name} failed verification! Expected ${expected.contentToString()}, received ${decryptedActual.contentToString()}")
        }
    }

    fun enableEncryption(session: Session, key: SecretKey) {
        val encrypter = PacketEncrypter(key.toEncryptingCipher(SHARED_SECRET_ALGORITHM))
        val decrypter = PacketDecrypter(key.toDecryptingCipher(SHARED_SECRET_ALGORITHM))

        session.channel.pipeline().addBefore(
            SizeDecoder.NETTY_NAME,
            PacketDecrypter.NETTY_NAME,
            decrypter
        )
        session.channel.pipeline().addBefore(
            SizeEncoder.NETTY_NAME,
            PacketEncrypter.NETTY_NAME,
            encrypter
        )
    }

    fun enableCompression(session: Session, threshold: Int) {
        val compressor = session.channel.pipeline()[PacketCompressor.NETTY_NAME]
        val decompressor = session.channel.pipeline()[PacketDecompressor.NETTY_NAME]

        if (threshold > 0) {
            session.sendPacket(PacketOutSetCompression(threshold))
            if (decompressor is PacketDecompressor) {
                decompressor.threshold = threshold
            } else {
                session.channel.pipeline().addBefore(
                    PacketDecoder.NETTY_NAME,
                    PacketDecompressor.NETTY_NAME,
                    PacketDecompressor(threshold)
                )
            }
            if (compressor is PacketCompressor) {
                compressor.threshold = threshold
            } else {
                session.channel.pipeline().addBefore(
                    PacketEncoder.NETTY_NAME,
                    PacketCompressor.NETTY_NAME,
                    PacketCompressor(threshold)
                )
            }
        }
    }

    private fun handlePlayStateBegin(session: Session) {
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
//                it.sendPacket(joinPacket)
                it.sendPacket(infoPacket)
                it.sendPacket(spawnPlayerPacket)
                it.sendPacket(metadataPacket)
                it.sendPacket(propertiesPacket)
                it.sendPacket(headLookPacket)
            }
    }

    fun handleDisconnection(session: Session) {
        if (session.currentState != PacketState.PLAY) return

        GlobalScope.launch { server.eventBus.call(QuitEvent(session.player)) }
        server.worldManager.savePlayerData(session.player)

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
        val disconnectPacket = PacketOutPlayDisconnect(translatable { key("multiplayer.disconnect.server_shutdown") })
        sessions.forEach {
            it.sendPacket(disconnectPacket)
            it.disconnect()
        }
    }

    private fun createPlayerData(player: KryptonPlayer) = PlayerData(
        isInvulnerable = player.abilities.isInvulnerable,
        spawnPosition = player.world.spawnLocation.toVector(),
        position = player.location.toVector(),
        gamemode = player.gamemode,
        spawnDimension = NamespacedKey(value = "overworld"),
        dimension = NamespacedKey(value = "overworld"),
        rotationX = player.location.yaw,
        rotationY = player.location.pitch
    )

    companion object {

        private val LOGGER = logger<SessionManager>()
    }
}