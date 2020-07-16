package me.bristermitten.minekraft.packet

import me.bristermitten.minekraft.Server
import me.bristermitten.minekraft.Session
import me.bristermitten.minekraft.lang.Color
import me.bristermitten.minekraft.packet.`in`.PacketInClientSettings
import me.bristermitten.minekraft.packet.`in`.PacketInLoginStart
import me.bristermitten.minekraft.packet.`in`.PacketInPing
import me.bristermitten.minekraft.packet.`in`.PacketInStatusRequest
import me.bristermitten.minekraft.packet.`in`.login.PacketInEncryptionResponse
import me.bristermitten.minekraft.packet.data.*
import me.bristermitten.minekraft.packet.out.*
import me.bristermitten.minekraft.packet.state.PacketState
import java.util.*
import javax.crypto.spec.SecretKeySpec

class PacketHandler(private val session: Session, private val server: Server) {

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    fun handle(packet: Packet) {
        when (packet) {
            is PacketInPing -> handlePing(packet)
            is PacketInStatusRequest -> handleStatusPacket()
            is PacketInLoginStart -> sendEncryptionRequest()
            is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
            is PacketInClientSettings -> handleClientSettings()
        }
    }

    private fun handleClientSettings() {
        session.sendPacket(PacketOutHeldItemChange())
        session.sendPacket(PacketOutDeclareRecipes())
//        session.sendPacket(PacketOutTags())
//        session.sendPacket(PacketOutEntityStatus())
//        session.sendPacket(PacketOutDeclareCommands())
//        session.sendPacket(PacketOutUnlockRecipes())
        session.sendPacket(PacketOutPlayerPositionAndLook())
        session.sendPacket(PacketOutPlayerInfo())
    }

    fun handleStatusPacket() {
        session.sendPacket(
            PacketOutStatusResponse(
                StatusResponse(
                    ServerVersion("1.16.1", 736),
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

        beginPlayState()
    }


    private fun beginPlayState() {
        session.sendPacket(
            PacketOutLoginSuccess(
                UUID.fromString("876ce46d-dc56-4a17-9644-0be67fe7c7f6"),
                "BristerMitten"
            )
        )

        session.currentState = PacketState.PLAY
        session.sendPacket(PacketOutJoinGame())
//        session.sendPacket(PacketOutPluginMessage())
    }

    private fun sendEncryptionRequest() {
        beginPlayState()
//        session.sendPacket(
//                PacketOutEncryptionRequest(
//                        server.encryption.public,
//                        verifyToken
//                )
//        ) TODO
    }

    fun handlePing(packet: PacketInPing) {
        session.sendPacket(PacketOutPingResponse(packet.payload))
    }
}
