package org.kryptonmc.krypton.packet

import me.bardy.komponent.colour.Color
import me.bardy.komponent.dsl.textComponent
import me.bardy.komponent.dsl.translationComponent
import me.bardy.komponent.event.Entity
import me.bardy.komponent.event.showEntity
import me.bardy.komponent.event.suggestCommand
import org.kryptonmc.krypton.Server
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.Session
import org.kryptonmc.krypton.SessionStorage
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.encryption.hexDigest
import org.kryptonmc.krypton.entity.Abilities
import org.kryptonmc.krypton.entity.Attribute
import org.kryptonmc.krypton.entity.AttributeKey
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.entity.cardinal.Angle
import org.kryptonmc.krypton.entity.cardinal.toAngle
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.packet.`in`.PacketInChat
import org.kryptonmc.krypton.packet.`in`.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.PacketInPlayerMovement.*
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.data.*
import org.kryptonmc.krypton.packet.out.*
import org.kryptonmc.krypton.packet.out.entity.*
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.world.Difficulty
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class PacketHandler(private val session: Session, private val server: Server) {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    fun handle(packet: Packet) {
        when (packet) {
            is PacketInPing -> handlePing(packet)
            is PacketInStatusRequest -> handleStatusPacket()
            is PacketInLoginStart -> handleLoginStart(packet)
            is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
            is PacketInClientSettings -> handleClientSettings(packet)
            is PacketInPlayerPosition -> handlePositionUpdate(packet)
            is PacketInPlayerRotation -> handleRotationUpdate(packet)
            is PacketInPlayerPositionAndRotation -> handlePositionAndRotationUpdate(packet)
            is PacketInChat -> handleChat(packet)
        }
    }

    private fun handleClientSettings(packet: PacketInClientSettings) {
        session.settings = packet.settings

        session.sendPacket(PacketOutEntityMetadata(
            session.id,
            PlayerMetadata(skinFlags = packet.settings.skinFlags)
        ))
    }

    private fun handleStatusPacket() {
        val players = SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .map { PlayerInfo(it.profile.name, it.profile.uuid) }
            .toSet()

        session.sendPacket(PacketOutStatusResponse(StatusResponse(
            ServerVersion("1.16.5", 754),
            Players(
                MAX_PLAYERS,
                ServerStorage.playerCount.get(),
                players
            ),
            textComponent("MineKraft is a Minecraft Server written in Kotlin!") {
                color = Color.random()
            }
        )))
    }

    private fun handlePing(packet: PacketInPing) {
        session.sendPacket(PacketOutPong(packet.payload))
    }

    private fun handleLoginStart(packet: PacketInLoginStart) {
        session.player = Player(ServerStorage.nextEntityId.getAndIncrement())
        session.player.name = packet.name

        session.sendPacket(PacketOutEncryptionRequest(
            server.encryption.publicKey,
            verifyToken
        ))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        session.verifyToken(verifyToken, packet.verifyToken)

        val sharedSecret = server.encryption.decryptWithPrivateKey(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        session.commenceEncryption(secretKey)

        authenticateUser(session.player.name, sharedSecret)

        beginPlayState()
    }

    private fun authenticateUser(username: String, sharedSecret: ByteArray) {
        val cachedProfile = SessionStorage.profiles.getIfPresent(username)
        if (cachedProfile != null) session.profile = cachedProfile

        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(SERVER_ID_ASCII)
        shaDigest.update(sharedSecret)
        shaDigest.update(server.encryption.publicKey.encoded)
        val serverId = shaDigest.hexDigest()

        val response = SessionService.hasJoined(username, serverId, ServerStorage.SERVER_IP.hostAddress).execute()
        if (response.code() != 200 || !response.isSuccessful || response.errorBody() != null) {
            session.sendPacket(PacketOutDisconnect(
                translationComponent("")
            ))
            return
        }

        val profile = response.body()
        session.profile = requireNotNull(profile)
        SessionStorage.profiles.put(profile.name, profile)
    }

    private fun beginPlayState() {
        session.player = Player(session.id)
        session.player.name = session.profile.name
        session.player.uuid = session.profile.uuid

        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))
        session.currentState = PacketState.PLAY

        session.sendPacket(PacketOutJoinGame(session.id, Gamemode.CREATIVE, MAX_PLAYERS))
        session.sendPacket(PacketOutPluginMessage(NamespacedKey(value = "brand"), "MineKraft"))
        session.sendPacket(PacketOutServerDifficulty(Difficulty.PEACEFUL, true))
        session.sendPacket(PacketOutAbilities(Abilities(
            isInvulnerable = true,
            canFly = true,
            isFlyingAllowed = true,
            isCreativeMode = true
        )))
        session.sendPacket(PacketOutHeldItemChange(0))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags(server.registryManager, server.tagManager))
        session.sendPacket(PacketOutEntityStatus(session.id))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location, teleportId = session.lastTeleportId))
        session.sendPacket(PacketOutChat(
            textComponent("${session.profile.name} joined the game"),
            ChatPosition.SYSTEM_MESSAGE,
            UUID.fromString("00000000-0000-0000-0000-000000000000")
        ))

        val playerInfos = SessionStorage.sessions.filter { it.currentState == PacketState.PLAY }.map {
            PacketOutPlayerInfo.PlayerInfo(
                Random.Default.nextInt(1000),
                Gamemode.CREATIVE,
                it.profile,
                textComponent(it.profile.name)
            )
        }

        session.sendPacket(PacketOutPlayerInfo(
            PacketOutPlayerInfo.PlayerAction.ADD_PLAYER,
            playerInfos
        ))

        SessionStorage.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(PacketOutChat(
                    textComponent("${session.profile.name} joined the game"),
                    ChatPosition.SYSTEM_MESSAGE,
                    UUID.fromString("00000000-0000-0000-0000-000000000000")
                ))

                it.sendPacket(PacketOutPlayerInfo(
                    PacketOutPlayerInfo.PlayerAction.ADD_PLAYER,
                    listOf(PacketOutPlayerInfo.PlayerInfo(
                        Random.Default.nextInt(1000),
                        Gamemode.CREATIVE,
                        session.profile,
                        textComponent(session.profile.name)
                    ))
                ))

                it.sendPacket(PacketOutSpawnPlayer(session.player))

                it.sendPacket(PacketOutEntityMetadata(
                    session.player.id,
                    PlayerMetadata.Default
                ))

                it.sendPacket(PacketOutEntityProperties(
                    session.player.id,
                    listOf(
                        Attribute(AttributeKey.GENERIC_MAX_HEALTH, 20.0),
                        Attribute(AttributeKey.GENERIC_MOVEMENT_SPEED, 0.1)
                    )
                ))

                it.sendPacket(PacketOutEntityHeadLook(
                    session.player.id,
                    Angle(0u)
                ))

                session.sendPacket(PacketOutSpawnPlayer(it.player))

                session.sendPacket(PacketOutEntityMetadata(
                    it.player.id,
                    PlayerMetadata.Default
                ))

                session.sendPacket(PacketOutEntityProperties(
                    it.player.id,
                    listOf(
                        Attribute(AttributeKey.GENERIC_MAX_HEALTH, 20.0),
                        Attribute(AttributeKey.GENERIC_MOVEMENT_SPEED, 0.1)
                    )
                ))

                session.sendPacket(PacketOutEntityHeadLook(
                    it.player.id,
                    Angle(0u)
                ))
            }

        session.sendPacket(PacketOutChunkData())
        session.sendPacket(PacketOutTimeUpdate(0L, 6000L))
        ServerStorage.playerCount.getAndIncrement()

        executor.scheduleAtFixedRate({
            session.sendPacket(PacketOutKeepAlive(System.currentTimeMillis()))
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
        val headLookPacket = PacketOutEntityHeadLook(
            session.id,
            packet.yaw.toAngle()
        )

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
        val headLookPacket = PacketOutEntityHeadLook(
            session.id,
            newLocation.yaw.toAngle()
        )

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
            translationComponent("chat.type.text") {
                text(session.profile.name) {
                    insertion = session.profile.name
                    clickEvent = suggestCommand("/msg ${session.profile.name}")
                    hoverEvent = showEntity(Entity(
                        session.profile.uuid,
                        "minecraft:player",
                        session.profile.name
                    ))
                }
                text(packet.message)
            },
            ChatPosition.CHAT_BOX,
            session.profile.uuid
        )

        SessionStorage.sessions.asSequence()
            .filter { it.currentState == PacketState.PLAY }
            .filter { it.settings.chatMode == ChatMode.ENABLED }
            .forEach { it.sendPacket(chatPacket) }
    }

    companion object {

        private const val MAX_PLAYERS = 200

        private val SERVER_ID_ASCII = "".toByteArray(Charsets.US_ASCII)
    }
}