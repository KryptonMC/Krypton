package me.bristermitten.minekraft.packet

import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.SessionStorage
import me.bristermitten.minekraft.entity.Gamemode
import me.bristermitten.minekraft.lang.Color
import me.bristermitten.minekraft.packet.`in`.*
import me.bristermitten.minekraft.packet.`in`.login.PacketInEncryptionResponse
import me.bristermitten.minekraft.packet.`in`.status.PacketInPing
import me.bristermitten.minekraft.packet.`in`.status.PacketInStatusRequest
import me.bristermitten.minekraft.packet.data.*
import me.bristermitten.minekraft.packet.out.*
import me.bristermitten.minekraft.packet.out.entity.PacketOutEntityPosition
import me.bristermitten.minekraft.packet.out.status.PacketOutPong
import me.bristermitten.minekraft.packet.out.status.PacketOutStatusResponse
import me.bristermitten.minekraft.packet.state.PacketState
import net.kyori.adventure.text.Component
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class PacketHandler(private val session: Session, private val server: Server) {

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val numberOfPlayers = AtomicInteger(1)

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    fun handle(packet: Packet) {
        when (packet) {
            is PacketInPing -> handlePing(packet)
            is PacketInStatusRequest -> handleStatusPacket()
            is PacketInLoginStart -> beginPlayState(packet)
            is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
            is PacketInClientSettings -> Unit //handleClientSettings()
            is PacketInPlayerPosition -> handlePositionUpdate(packet)
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

    fun handleStatusPacket() {
        session.sendPacket(
            PacketOutStatusResponse(
                StatusResponse(
                    ServerVersion("1.16.5", 754),
                    Players(
                        100, 2,
                        setOf(
                            PlayerInfo(Chat("Hello", bold = true, color = Color.RED).toChatString()),
                            PlayerInfo(Chat("World!", bold = true, color = Color.BLUE).toChatString())
                        )
                    ),
                    Chat(
                        "MineKraft is a Minecraft Server written in Kotlin!",
                        bold = true,
                        color = Color.values().random()
                    )
                )
            )
        )
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        session.verifyToken(verifyToken, packet.verifyToken)


        val sharedSecret = server.encryption.decryptWithPrivateKey(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        session.commenceEncryption(secretKey)

        //beginPlayState()
    }


    private fun beginPlayState(packet: PacketInLoginStart) {
        val uuid = UUID.randomUUID()
        session.sendPacket(PacketOutLoginSuccess(uuid, packet.name))

        session.currentState = PacketState.PLAY

        val entityId = numberOfPlayers.getAndIncrement()

        session.sendPacket(PacketOutJoinGame(entityId, Gamemode.CREATIVE, MAX_PLAYERS))
        session.sendPacket(PacketOutPluginMessage())
        session.sendPacket(PacketOutServerDifficulty())
        session.sendPacket(PacketOutAbilities())
        session.sendPacket(PacketOutHeldItemChange())
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags(server.registryManager, server.tagManager))
        session.sendPacket(PacketOutEntityStatus(entityId))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location))
        session.sendPacket(PacketOutChat(
            Component.text("${packet.name} joined the game"),
            ChatPosition.SYSTEM_MESSAGE,
            UUID.fromString("00000000-0000-0000-0000-000000000000"))
        )
        SessionStorage.sessions.forEach {
            it.sendPacket(PacketOutPlayerInfo(
                PacketOutPlayerInfo.PlayerAction.ADD_PLAYER,
                1,
                uuid,
                packet.name,
                listOf(Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTU5NDkyNzk1OTc2OSwKICAicHJvZmlsZUlkIiA6ICI4NzZjZTQ2ZGRjNTY0YTE3OTY0NDBiZTY3ZmU3YzdmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCcmlzdGVyTWl0dGVuIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVjY2VjM2ZjMDZhMWI2YmY5NjJkMjMwNzA0YjY2MjNiYmZiNDMwNDNmMDY2Nzk2NmMyMGM4OWMzMGM0YTMzMCIKICAgIH0KICB9Cn0=")),
                Gamemode.CREATIVE,
                Random.Default.nextInt(1000),
                true,
                Component.text(packet.name)
            ))
        }
        session.sendPacket(PacketOutChunkData())

        executor.scheduleAtFixedRate({
            session.sendPacket(PacketOutKeepAlive())
        }, 0, 20, TimeUnit.SECONDS)
//        session.sendPacket(PacketOutPluginMessage())
    }

    // we'll also deal with encryption later
    // TODO: implement AES encryption
//    private fun sendEncryptionRequest() {
//        beginPlayState()
//        session.sendPacket(
//                PacketOutEncryptionRequest(
//                        server.encryption.public,
//                        verifyToken
//                )
//        ) TODO
//    }

    private fun handlePing(packet: PacketInPing) {
        session.sendPacket(PacketOutPong(packet.payload))
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = session.player.location
        val newLocation = packet.location
        session.player.location = newLocation

        SessionStorage.sessions.filter { it != session }.forEach {
            it.sendPacket(PacketOutEntityPosition(
                session.player.id,
                ((newLocation.x * 32) - (oldLocation.x * 32) * 128).toInt().toShort(),
                ((newLocation.y * 32) - (oldLocation.y * 32) * 128).toInt().toShort(),
                ((newLocation.z * 32) - (oldLocation.z * 32) * 128).toInt().toShort(),
                packet.onGround
            ))
        }
    }

    companion object {

        private const val MAX_PLAYERS = 200
    }
}
