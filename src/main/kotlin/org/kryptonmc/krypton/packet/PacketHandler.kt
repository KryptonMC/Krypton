package org.kryptonmc.krypton.packet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.event.ClickEvent.suggestCommand
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import org.kryptonmc.krypton.*
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.encryption.hexDigest
import org.kryptonmc.krypton.entity.Abilities
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.extension.toArea
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.*
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.data.*
import org.kryptonmc.krypton.packet.out.login.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.*
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.*
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.entity.spawn.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.space.Angle
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.crypto.spec.SecretKeySpec
import kotlin.math.floor
import kotlin.math.max
import kotlin.random.Random

class PacketHandler(private val session: Session, private val server: Server) {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private var teleportId = Random.nextInt(1000)

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    fun handle(packet: Packet) {
        when (packet) {
            is PacketInHandshake -> handleHandshake(packet)
            is PacketInPing -> handlePing(packet)
            is PacketInStatusRequest -> handleStatusPacket()
            is PacketInLoginStart -> handleLoginStart(packet)
            is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
            is PacketInClientSettings -> handleClientSettings(packet)
            is PacketInPlayerPosition -> handlePositionUpdate(packet)
            is PacketInPlayerRotation -> handleRotationUpdate(packet)
            is PacketInPlayerPositionAndRotation -> handlePositionAndRotationUpdate(packet)
            is PacketInChat -> handleChat(packet)
            is PacketInKeepAlive -> verifyKeepAlive(packet)
        }
    }

    private fun handleHandshake(packet: PacketInHandshake) {
        when (val nextState = packet.data.nextState) {
            PacketState.LOGIN -> {
                session.currentState = PacketState.LOGIN
                if (packet.data.protocol != ServerInfo.PROTOCOL) {
                    val reason = if (packet.data.protocol < ServerInfo.PROTOCOL) {
                        translatable {
                            key("multiplayer.disconnect.outdated_client")
                            args(text { content(ServerInfo.VERSION) })
                        }
                    } else {
                        translatable {
                            key("multiplayer.disconnect.incompatible")
                            args(text { content(ServerInfo.VERSION) })
                        }
                    }
                    session.sendPacket(PacketOutDisconnect(reason))
                    session.disconnect()
                }
            }
            PacketState.STATUS -> session.currentState = PacketState.STATUS
            else -> throw UnsupportedOperationException("Invalid next state $nextState")
        }
    }

    private fun handleClientSettings(packet: PacketInClientSettings) {
        session.settings = packet.settings
        session.sendPacket(PacketOutEntityMetadata(session.id, PlayerMetadata(skinFlags = packet.settings.skinFlags)))
    }

    private fun handleStatusPacket() {
        val players = SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .map { PlayerInfo(it.profile.name, it.profile.uuid) }
            .toSet()

        session.sendPacket(PacketOutStatusResponse(StatusResponse(
            ServerVersion(ServerInfo.VERSION, ServerInfo.PROTOCOL),
            Players(server.config.status.maxPlayers, ServerStorage.PLAYER_COUNT.get(), players),
            server.config.status.motd
        )))
    }

    private fun handlePing(packet: PacketInPing) {
        session.sendPacket(PacketOutPong(packet.payload))
    }

    private fun handleLoginStart(packet: PacketInLoginStart) {
        session.player = Player(ServerStorage.NEXT_ENTITY_ID.getAndIncrement())
        session.player.name = packet.name

        if (!server.config.server.onlineMode) {
            val offlineUUID = UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
            session.profile = GameProfile(offlineUUID, packet.name, emptyList())
            beginPlayState()
            return
        }

        session.sendPacket(PacketOutEncryptionRequest(server.encryption.publicKey, verifyToken))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        session.verifyToken(verifyToken, packet.verifyToken)

        val sharedSecret = server.encryption.decrypt(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        session.commenceEncryption(secretKey)

        authenticateUser(session.player.name, sharedSecret)

        if (server.config.server.compressionThreshold > 0) {
            session.sendPacket(PacketOutSetCompression(server.config.server.compressionThreshold))
            session.setupCompression(server.config.server.compressionThreshold)
        }

        beginPlayState()
    }

    private fun authenticateUser(username: String, sharedSecret: ByteArray) {
        val cachedProfile = SessionStorage.profiles.getIfPresent(username)
        if (cachedProfile != null) {
            session.profile = cachedProfile
            return
        }

        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(ServerInfo.SERVER_ID.toByteArray(Charsets.US_ASCII))
        shaDigest.update(sharedSecret)
        shaDigest.update(server.encryption.publicKey.encoded)
        val serverId = shaDigest.hexDigest()

        val response = SessionService.hasJoined(username, serverId, server.config.server.ip).execute()
        if (!response.isSuccessful) {
            LOGGER.error("Failed to verify username $username!")
            LOGGER.debug("Error code: ${response.code()}, was successful: ${response.isSuccessful}, error body: ${response.errorBody()}")
            session.sendPacket(PacketOutDisconnect(translatable { key("multiplayer.disconnect.unverified_username") }))
            session.disconnect()
            return
        }

        val profile = requireNotNull(response.body())
        LOGGER.info("UUID of player ${profile.name} is ${profile.uuid}")
        session.profile = profile
        SessionStorage.profiles.put(profile.name, profile)
    }

    private fun beginPlayState() {
        session.player = Player(session.id)
        session.player.name = session.profile.name
        session.player.uuid = session.profile.uuid

        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))
        session.currentState = PacketState.PLAY

        val world = server.worldManager.worlds[0]
        world.gameType = server.config.world.gamemode
        session.player.gamemode = world.gameType
        val spawnPosition = world.spawnPosition
        session.player.location = spawnPosition.toLocation()

        val joinPacket = PacketOutChat(
            translatable {
                key("multiplayer.player.joined")
                args(text { content(session.profile.name) })
            },
            ChatPosition.SYSTEM_MESSAGE,
            SERVER_UUID
        )

        session.sendPacket(PacketOutJoinGame(
            session.id,
            server.config.world.hardcore,
            world,
            world.gameType,
            server.registryManager.dimensions,
            server.registryManager.biomes,
            server.config.status.maxPlayers,
            server.config.world.viewDistance
        ))
        session.sendPacket(PacketOutPluginMessage(NamespacedKey(value = "brand"), "Krypton"))
        session.sendPacket(PacketOutServerDifficulty(server.config.world.difficulty, true))

        val abilities = when (world.gameType) {
            Gamemode.SURVIVAL, Gamemode.ADVENTURE -> Abilities()
            Gamemode.CREATIVE -> Abilities(isInvulnerable = true, isFlyingAllowed = true, isCreativeMode = true)
            Gamemode.SPECTATOR -> Abilities(isInvulnerable = true, canFly = true, isFlyingAllowed = true)
        }

        session.sendPacket(PacketOutAbilities(abilities))
        session.sendPacket(PacketOutHeldItemChange(0))
        session.sendPacket(PacketOutDeclareRecipes()) // TODO: actually load and send some recipes
        session.sendPacket(PacketOutTags(server.registryManager, server.tagManager))
        session.sendPacket(PacketOutEntityStatus(session.id))
        session.sendPacket(PacketOutDeclareRecipes()) // TODO: should be unlock recipes
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location, teleportId = teleportId))
        session.sendPacket(joinPacket)

        val playerInfos = SessionStorage.sessions.filter { it.currentState == PacketState.PLAY }.map {
            PacketOutPlayerInfo.PlayerInfo(
                Random.nextInt(1000),
                it.player.gamemode,
                it.profile,
                text { content(it.profile.name) }
            )
        }

        session.sendPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, playerInfos))

        val infoPacket = PacketOutPlayerInfo(
            PacketOutPlayerInfo.PlayerAction.ADD_PLAYER,
            listOf(
                PacketOutPlayerInfo.PlayerInfo(
                    Random.nextInt(1000),
                    session.player.gamemode,
                    session.profile,
                    text { content(session.profile.name) }
                )
            )
        )
        val spawnPlayerPacket = PacketOutSpawnPlayer(session.player)
        val metadataPacket = PacketOutEntityMetadata(session.player.id, PlayerMetadata)
        val propertiesPacket = PacketOutEntityProperties(session.player.id, DEFAULT_PLAYER_ATTRIBUTES)
        val headLookPacket = PacketOutEntityHeadLook(session.player.id, Angle.ZERO)

        SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(joinPacket)
                it.sendPacket(infoPacket)
                it.sendPacket(spawnPlayerPacket)
                it.sendPacket(metadataPacket)
                it.sendPacket(propertiesPacket)
                it.sendPacket(headLookPacket)

                session.sendPacket(PacketOutSpawnPlayer(it.player))
                session.sendPacket(PacketOutEntityMetadata(it.player.id, PlayerMetadata))
                session.sendPacket(PacketOutEntityProperties(it.player.id, DEFAULT_PLAYER_ATTRIBUTES))
                session.sendPacket(PacketOutEntityHeadLook(it.player.id, Angle.ZERO))
            }

        val centerChunk = ChunkPosition(floor(spawnPosition.x / 16.0).toInt(), floor(spawnPosition.z / 16.0).toInt())
        val region = server.regionManager.loadRegionFromChunk(centerChunk)

        session.sendPacket(PacketOutUpdateViewPosition(centerChunk))

        GlobalScope.launch(Dispatchers.IO) {
            for (i in 0 until server.config.world.viewDistance.toArea()) {
                val chunk = region.chunks.singleOrNull {
                    it.position == server.regionManager.chunkInSpiral(i, centerChunk.x, centerChunk.z)
                } ?: continue
                session.sendPacket(PacketOutUpdateLight(chunk))
                session.sendPacket(PacketOutChunkData(chunk))
                Thread.sleep(20L)
            }
        }

        session.sendPacket(PacketOutEntityMetadata(session.player.id, PlayerMetadata))

        session.sendPacket(PacketOutWorldBorder(BorderAction.INITIALIZE, world.border))

        session.sendPacket(PacketOutTimeUpdate(world.time, world.dayTime))
        session.sendPacket(PacketOutSpawnPosition(spawnPosition))

        session.sendPacket(PacketOutEntityProperties(session.player.id, DEFAULT_PLAYER_ATTRIBUTES))

        ServerStorage.PLAYER_COUNT.getAndIncrement()

        executor.scheduleAtFixedRate({
            val keepAliveId = System.currentTimeMillis()
            session.lastKeepAliveId = keepAliveId
            session.sendPacket(PacketOutKeepAlive(keepAliveId))
        }, 0, 20, TimeUnit.SECONDS)
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = session.player.location
        val newLocation = packet.location
        session.player.location = newLocation

        val positionPacket = PacketOutEntityPosition(
            session.id,
            ((newLocation.x * 32 - oldLocation.x * 32) * 128).toInt().toShort(),
            ((newLocation.y * 32 - oldLocation.y * 32) * 128).toInt().toShort(),
            ((newLocation.z * 32 - oldLocation.z * 32) * 128).toInt().toShort(),
            packet.onGround
        )

        SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach { it.sendPacket(positionPacket) }
    }

    private fun handleRotationUpdate(packet: PacketInPlayerRotation) {
        val rotationPacket = PacketOutEntityRotation(
            session.id,
            packet.yaw.toAngle(),
            packet.pitch.toAngle(),
            packet.onGround
        )
        val headLookPacket = PacketOutEntityHeadLook(session.id, packet.yaw.toAngle())

        SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(rotationPacket)
                it.sendPacket(headLookPacket)
            }
    }

    private fun handlePositionAndRotationUpdate(packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = session.player.location
        val newLocation = packet.location
        session.player.location = newLocation

        val positionAndRotationPacket = PacketOutEntityPositionAndRotation(
            session.id,
            ((newLocation.x * 32 - oldLocation.x * 32) * 128).toInt().toShort(),
            ((newLocation.y * 32 - oldLocation.y * 32) * 128).toInt().toShort(),
            ((newLocation.z * 32 - oldLocation.z * 32) * 128).toInt().toShort(),
            newLocation.yaw.toAngle(),
            newLocation.pitch.toAngle(),
            packet.onGround
        )
        val headLookPacket = PacketOutEntityHeadLook(session.id, newLocation.yaw.toAngle())

        SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(positionAndRotationPacket)
                it.sendPacket(headLookPacket)
            }
    }

    private fun handleChat(packet: PacketInChat) {
        val chatPacket = PacketOutChat(
            translatable {
                key("chat.type.text")
                args(text {
                    content(session.profile.name)
                    insertion(session.profile.name)
                    clickEvent(suggestCommand("/msg ${session.profile.name}"))
                    hoverEvent(showEntity(ShowEntity.of(
                        Key.key("minecraft", "player"),
                        session.profile.uuid,
                        text { content(session.profile.name) }
                    )))
                }, text { content(packet.message) })
            },
            ChatPosition.CHAT_BOX,
            session.profile.uuid
        )

        SessionStorage.sessions.asSequence()
            .filter { it.currentState == PacketState.PLAY }
            .filter { it.settings.chatMode == ChatMode.ENABLED }
            .forEach { it.sendPacket(chatPacket) }
    }

    private fun verifyKeepAlive(packet: PacketInKeepAlive) {
        if (session.lastKeepAliveId == packet.keepAliveId) {
            updateLatency(max((packet.keepAliveId - session.lastKeepAliveId), 0L).toInt())
            return
        }
        session.sendPacket(PacketOutPlayDisconnect(translatable { key("disconnect.timeout") }))
        session.disconnect()
    }

    private fun updateLatency(latency: Int) {
        val infoPacket = PacketOutPlayerInfo(
            PacketOutPlayerInfo.PlayerAction.UPDATE_LATENCY,
            listOf(PacketOutPlayerInfo.PlayerInfo(latency, profile = session.profile))
        )

        SessionStorage.sessions.asSequence()
            .filter { it.currentState == PacketState.PLAY }
            .forEach { it.sendPacket(infoPacket) }
    }

    companion object {

        private val LOGGER = logger<PacketHandler>()
    }
}