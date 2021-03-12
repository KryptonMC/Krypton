package org.kryptonmc.krypton.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.*
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.encryption.Encryption.Companion.SHARED_SECRET_ALGORITHM
import org.kryptonmc.krypton.encryption.toDecryptingCipher
import org.kryptonmc.krypton.encryption.toEncryptingCipher
import org.kryptonmc.krypton.entity.Abilities
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.toArea
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.*
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.*
import org.kryptonmc.krypton.packet.out.play.chat.*
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

class SessionManager(private val server: KryptonServer) {

    val sessions: MutableSet<Session> = ConcurrentHashMap.newKeySet()

    private val handler = PacketHandler(this, server)

    private val keepAliveExecutor = Executors.newScheduledThreadPool(8, NamedThreadFactory("Keep Alive Thread #%d"))

    fun handle(session: Session, packet: Packet) = handler.handle(session, packet)

    fun beginPlayState(session: Session) {
        session.player = KryptonPlayer(session.id)
        session.player.name = session.profile.name
        session.player.uuid = session.profile.uuid

        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))
        session.currentState = PacketState.PLAY

        val world = server.worldManager.worlds.getValue(server.config.world.name)
        world.gamemode = server.config.world.gamemode
        session.player.gamemode = world.gamemode
        val spawnLocation = world.spawnLocation
        session.player.location = spawnLocation

        val joinPacket = PacketOutChat(
            translatable {
                key("multiplayer.player.joined")
                color(NamedTextColor.YELLOW)
                args(text { content(session.profile.name) })
            },
            ChatPosition.SYSTEM_MESSAGE,
            SERVER_UUID
        )

        session.sendPacket(PacketOutJoinGame(
            session.id,
            server.config.world.hardcore,
            world,
            world.gamemode,
            server.registryManager.dimensions,
            server.registryManager.biomes,
            server.config.status.maxPlayers,
            server.config.world.viewDistance
        ))
        session.sendPacket(PacketOutPluginMessage(NamespacedKey(value = "brand"), "Krypton"))
        session.sendPacket(PacketOutServerDifficulty(server.config.world.difficulty, true))

        val abilities = when (world.gamemode) {
            Gamemode.SURVIVAL, Gamemode.ADVENTURE -> Abilities()
            Gamemode.CREATIVE -> Abilities(isInvulnerable = true, isFlyingAllowed = true, isCreativeMode = true)
            Gamemode.SPECTATOR -> Abilities(isInvulnerable = true, canFly = true, isFlyingAllowed = true)
        }

        session.sendPacket(PacketOutAbilities(abilities))
        session.sendPacket(PacketOutHeldItemChange(0))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags(server.registryManager, server.tagManager))
        session.sendPacket(PacketOutEntityStatus(session.id))
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location, teleportId = session.teleportId))
        session.sendPacket(joinPacket)

        val playerInfos = sessions.filter { it.currentState == PacketState.PLAY }.map {
            PlayerInfo(0, it.player.gamemode, it.profile, text { content(it.profile.name) })
        }
        session.sendPacket(PacketOutPlayerInfo(PlayerAction.ADD_PLAYER, playerInfos))

        GlobalScope.launch(Dispatchers.IO) { handlePlayStateBegin(session, joinPacket) }

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                session.sendPacket(PacketOutSpawnPlayer(it.player))
                session.sendPacket(PacketOutEntityMetadata(it.id, PlayerMetadata))
                session.sendPacket(PacketOutEntityProperties(it.id, DEFAULT_PLAYER_ATTRIBUTES))
                session.sendPacket(PacketOutEntityHeadLook(it.id, it.player.location.yaw.toAngle()))
            }

        val centerChunk = Vector(floor(spawnLocation.x / 16.0), 0.0, floor(spawnLocation.z / 16.0))
        val region = server.worldManager.loadRegionFromChunk(centerChunk)

        session.sendPacket(PacketOutUpdateViewPosition(centerChunk))

        GlobalScope.launch(Dispatchers.IO) {
            var chunkRegion = region
            for (i in 0 until server.config.world.viewDistance.toArea()) {
                val chunkPosition = server.worldManager.chunkInSpiral(i, centerChunk.x, centerChunk.z)

                val regionX = floor(chunkPosition.x / 32.0).toInt()
                val regionZ = floor(chunkPosition.z / 32.0).toInt()
                if (chunkRegion.x != regionX || chunkRegion.z != regionZ) {
                    chunkRegion = server.worldManager.loadRegionFromChunk(chunkPosition)
                }

                val chunk = chunkRegion.chunks[chunkPosition] ?: continue
                session.sendPacket(PacketOutUpdateLight(chunk))
                session.sendPacket(PacketOutChunkData(chunk))
            }
        }

        session.sendPacket(PacketOutEntityMetadata(session.id, PlayerMetadata))
        session.sendPacket(PacketOutWorldBorder(BorderAction.INITIALIZE, world.border))
        session.sendPacket(PacketOutTimeUpdate(world.time, world.dayTime))
        session.sendPacket(PacketOutSpawnPosition(spawnLocation))
        session.sendPacket(PacketOutEntityProperties(session.id, DEFAULT_PLAYER_ATTRIBUTES))

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

        val destroyPacket = PacketOutEntityDestroy(listOf(session.id))
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.REMOVE_PLAYER,
            listOf(PlayerInfo(profile = session.profile))
        )
        val leavePacket = PacketOutChat(
            translatable {
                key("multiplayer.player.left")
                color(NamedTextColor.YELLOW)
                args(text { content(session.profile.name) })
            },
            ChatPosition.SYSTEM_MESSAGE,
            SERVER_UUID
        )

        sendPackets(destroyPacket, infoPacket, leavePacket) { it != session && it.currentState == PacketState.PLAY }
        ServerStorage.PLAYER_COUNT.getAndDecrement()
    }

    fun updateLatency(session: Session, latency: Int) {
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

    companion object {

        private val LOGGER = logger<SessionManager>()
    }
}