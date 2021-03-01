package org.kryptonmc.krypton

import io.netty.channel.Channel
import me.bardy.komponent.Component
import me.bardy.komponent.event.changePage
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.encryption.Encryption.Companion.SHARED_SECRET_ALGORITHM
import org.kryptonmc.krypton.encryption.toDecryptingCipher
import org.kryptonmc.krypton.encryption.toEncryptingCipher
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketHandler
import org.kryptonmc.krypton.packet.data.ClientSettings
import org.kryptonmc.krypton.packet.out.PacketOutPlayDisconnect
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.packet.transformers.*
import javax.crypto.SecretKey

class Session(val id: Int, private val channel: Channel, private val server: Server) {

    private val handler = PacketHandler(this, server)

    lateinit var profile: GameProfile
    lateinit var settings: ClientSettings

    lateinit var player: Player
    var lastTeleportId = 0

    var lastKeepAliveId = 0L

    var isEncrypted = false
        private set

    private lateinit var packetDecrypter: PacketDecrypter
    private lateinit var packetEncrypter: PacketEncrypter

    @Volatile
    internal var currentState: PacketState = PacketState.HANDSHAKE

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun disconnect(component: Component) {
        channel.writeAndFlush(PacketOutPlayDisconnect(component)).addListener { channel.close() }
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

    fun setupCompression(threshold: Int) {
        val compressor = channel.pipeline().get(PacketCompressor.NETTY_NAME)
        val decompressor = channel.pipeline().get(PacketDecompressor.NETTY_NAME)
        if (threshold >= 0) {
            if (decompressor is PacketDecompressor) {
                decompressor.threshold = threshold
            } else {
                channel.pipeline().addBefore(
                    PacketDecoder.NETTY_NAME,
                    PacketDecompressor.NETTY_NAME,
                    PacketDecompressor(threshold)
                )
            }
            if (compressor is PacketCompressor) {
                compressor.threshold = threshold
            } else {
                channel.pipeline().addBefore(
                    PacketEncoder.NETTY_NAME,
                    PacketCompressor.NETTY_NAME,
                    PacketCompressor(threshold)
                )
            }
            return
        }

        if (decompressor is PacketDecompressor) {
            channel.pipeline().remove(PacketDecompressor.NETTY_NAME)
        }
        if (compressor is PacketCompressor) {
            channel.pipeline().remove(PacketCompressor.NETTY_NAME)
        }
    }

    companion object {

        private val LOGGER = logger<Session>()
    }
}