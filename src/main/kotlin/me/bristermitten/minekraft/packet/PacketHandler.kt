package me.bristermitten.minekraft.packet

import me.bardy.komponent.colour.Color
import me.bardy.komponent.dsl.textComponent
import me.bardy.komponent.dsl.translationComponent
import me.bardy.komponent.event.Entity
import me.bardy.komponent.event.showEntity
import me.bardy.komponent.event.suggestCommand
import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.ServerStorage
import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.SessionStorage
import me.bristermitten.minekraft.auth.requests.SessionService
import me.bristermitten.minekraft.encryption.hexDigest
import me.bristermitten.minekraft.entity.*
import me.bristermitten.minekraft.entity.cardinal.Angle
import me.bristermitten.minekraft.entity.entities.Player
import me.bristermitten.minekraft.entity.metadata.PlayerMetadata
import me.bristermitten.minekraft.packet.`in`.*
import me.bristermitten.minekraft.packet.`in`.PacketInPlayerMovement.*
import me.bristermitten.minekraft.packet.`in`.login.PacketInEncryptionResponse
import me.bristermitten.minekraft.packet.`in`.status.PacketInPing
import me.bristermitten.minekraft.packet.`in`.status.PacketInStatusRequest
import me.bristermitten.minekraft.packet.data.PlayerInfo
import me.bristermitten.minekraft.packet.data.Players
import me.bristermitten.minekraft.packet.data.ServerVersion
import me.bristermitten.minekraft.packet.data.StatusResponse
import me.bristermitten.minekraft.packet.out.*
import me.bristermitten.minekraft.packet.out.PacketOutAbilities
import me.bristermitten.minekraft.packet.out.entity.*
import me.bristermitten.minekraft.packet.out.status.PacketOutPong
import me.bristermitten.minekraft.packet.out.status.PacketOutStatusResponse
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.registry.NamespacedKey
import me.bristermitten.minekraft.world.Difficulty
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
            is PacketInClientSettings -> Unit //handleClientSettings()
            is PacketInPlayerPosition -> handlePositionUpdate(packet)
            is PacketInChat -> handleChat(packet)
        }
    }

    // we'll deal with this some time later
//    private fun handleClientSettings() {
//        session.sendPacket(PacketOutHeldItemChange())
//        session.sendPacket(PacketOutDeclareRecipes())
//        session.sendPacket(PacketOutTags())
//        session.sendPacket(PacketOutEntityStatus())
//        session.sendPacket(PacketOutDeclareCommands())
//        session.sendPacket(PacketOutUnlockRecipes())
//        session.sendPacket(PacketOutPlayerPositionAndLook())
//        session.sendPacket(PacketOutPlayerInfo())
//    }

    private fun handleStatusPacket() {
        val players = SessionStorage.sessions.filter { it != session && it.currentState == PacketState.PLAY }.map {
            PlayerInfo(it.profile.name, it.profile.uuid)
        }.toSet()

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

        SessionStorage.sessions.filter { it != session && it.currentState == PacketState.PLAY }.forEach {
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
                PlayerMetadata()
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
        }

        SessionStorage.sessions.filter { it != session && it.currentState == PacketState.PLAY }.forEach {
            session.sendPacket(PacketOutSpawnPlayer(it.player))

            session.sendPacket(PacketOutEntityMetadata(
                it.player.id,
                PlayerMetadata()
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

        SessionStorage.sessions.filter { it != session && it.currentState == PacketState.PLAY }.forEach {
            it.sendPacket(PacketOutEntityPosition(
                session.id,
                ((newLocation.x * 32 - oldLocation.x * 32) * 128).toInt().toShort(),
                ((newLocation.y * 32 - oldLocation.y * 32) * 128).toInt().toShort(),
                ((newLocation.z * 32 - oldLocation.z * 32) * 128).toInt().toShort(),
                packet.onGround
            ))
        }
    }

    private fun handleChat(packet: PacketInChat) {
        SessionStorage.sessions.forEach {
            it.sendPacket(PacketOutChat(
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
            ))
        }
    }

    companion object {

        private const val MAX_PLAYERS = 200

        private val SERVER_ID_ASCII = "".toByteArray(Charsets.US_ASCII)
    }
}