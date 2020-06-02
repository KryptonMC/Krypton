package me.bristermitten.minekraft

import io.netty.channel.Channel
import me.bristermitten.minekraft.encryption.Encryption.Companion.SHARED_SECRET_ALGORITHM
import me.bristermitten.minekraft.encryption.toDecryptingCipher
import me.bristermitten.minekraft.encryption.toEncryptingCipher
import me.bristermitten.minekraft.packet.Packet
import me.bristermitten.minekraft.packet.PacketHandler
import me.bristermitten.minekraft.packet.state.PacketState
import me.bristermitten.minekraft.packet.transformers.PacketDecoder
import me.bristermitten.minekraft.packet.transformers.PacketDecrypter
import me.bristermitten.minekraft.packet.transformers.PacketEncoder
import me.bristermitten.minekraft.packet.transformers.PacketEncrypter
import javax.crypto.SecretKey

class Session(private val channel: Channel, private val server: Server)
{

    private val handler = PacketHandler(this, server)
    var isEncrypted = false
        private set

    private lateinit var packetDecrypter: PacketDecrypter
    private lateinit var packetEncrypter: PacketEncrypter

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    fun sendPacket(packet: Packet)
    {
        channel.writeAndFlush(packet)
    }


    fun receive(msg: Packet)
    {
        handler.handle(msg)
    }

    fun verifyToken(expected: ByteArray, encryptedActual: ByteArray)
    {

        val actual = server.encryption.decryptWithPrivateKey(encryptedActual)
        require(actual.contentEquals(expected)) {
            "Decrypted Verify Token did not match original!, ${expected.contentToString()}, ${actual.contentToString()}"
        }
        //TODO fail by disconnecting
    }

    fun commenceEncryption(secretKey: SecretKey)
    {
        packetDecrypter = PacketDecrypter(secretKey.toDecryptingCipher(SHARED_SECRET_ALGORITHM))
        packetEncrypter = PacketEncrypter(secretKey.toEncryptingCipher(SHARED_SECRET_ALGORITHM))

        channel.pipeline().addBefore(
                PacketDecoder.NETTY_NAME,
                "decrypt",
                packetDecrypter
        )

        channel.pipeline().addBefore(
                PacketEncoder.NETTY_NAME, "encrypt", packetEncrypter
        )

        isEncrypted = true
    }
}
