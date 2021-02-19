package me.bristermitten.minekraft

import io.netty.channel.Channel
import me.bristermitten.minekraft.auth.GameProfile
import me.bristermitten.minekraft.encryption.Encryption.Companion.SHARED_SECRET_ALGORITHM
import me.bristermitten.minekraft.encryption.toDecryptingCipher
import me.bristermitten.minekraft.encryption.toEncryptingCipher
import me.bristermitten.minekraft.entity.entities.Player
import me.bristermitten.minekraft.extension.logger
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketHandler
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.transformers.*
import javax.crypto.SecretKey

class Session(val id: Int, private val channel: Channel, private val server: Server) {

    private val handler = PacketHandler(this, server)

    lateinit var profile: GameProfile

    lateinit var player: Player
    var lastTeleportId = 0

    var isEncrypted = false
        private set

    private lateinit var packetDecrypter: PacketDecrypter
    private lateinit var packetEncrypter: PacketEncrypter

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }


    fun receive(msg: Packet) {
        handler.handle(msg)
    }

    fun verifyToken(expected: ByteArray, encryptedActual: ByteArray) {

        val actual = server.encryption.decryptWithPrivateKey(encryptedActual)
        require(actual.contentEquals(expected)) {
            LOGGER.warn("Decrypted Verify Token did not match original! Expected: ${expected.contentToString()}, actual: ${actual.contentToString()}")
        }
        // TODO: fail by disconnecting
    }

    fun commenceEncryption(secretKey: SecretKey) {
        packetDecrypter = PacketDecrypter(secretKey.toDecryptingCipher(SHARED_SECRET_ALGORITHM))
        packetEncrypter = PacketEncrypter(secretKey.toEncryptingCipher(SHARED_SECRET_ALGORITHM))

        channel.pipeline().addBefore(
            SizeDecoder.NETTY_NAME,
            PacketDecrypter.NETTY_NAME,
            packetDecrypter
        )

        channel.pipeline().addBefore(
            SizeEncoder.NETTY_NAME,
            PacketEncrypter.NETTY_NAME,
            packetEncrypter
        )

        isEncrypted = true
    }

    companion object {

        private val LOGGER = logger<Session>()
    }
}