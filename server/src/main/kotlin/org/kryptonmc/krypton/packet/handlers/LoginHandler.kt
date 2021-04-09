package org.kryptonmc.krypton.packet.handlers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.event.events.login.LoginEvent
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.encryption.Encryption
import org.kryptonmc.krypton.encryption.toDecryptingCipher
import org.kryptonmc.krypton.encryption.toEncryptingCipher
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.extension.logger
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.transformers.*
import org.kryptonmc.krypton.session.Session
import org.kryptonmc.krypton.session.SessionManager
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class LoginHandler(
    override val server: KryptonServer,
    private val sessionManager: SessionManager,
    override val session: Session
) : PacketHandler {

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    override fun handle(packet: Packet) = when (packet) {
        is PacketInLoginStart -> handleLoginStart(packet)
        is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
        else -> Unit
    }

    private fun handleLoginStart(packet: PacketInLoginStart) {
        session.player = KryptonPlayer(packet.name, server, session, session.channel.remoteAddress() as InetSocketAddress)

        if (!server.isOnline) {
            // Note: Per the protocol, offline players use UUID v3, rather than UUID v4.
            val offlineUUID = UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
            session.player.uuid = offlineUUID
            session.profile = GameProfile(offlineUUID, packet.name, emptyList())
            if (!callLoginEvent(packet.name, offlineUUID)) return

            sessionManager.beginPlayState(session)
            return
        }

        session.sendPacket(PacketOutEncryptionRequest(server.encryption.publicKey, verifyToken))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        if (!verifyToken(verifyToken, packet.verifyToken)) return

        val sharedSecret = server.encryption.decrypt(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        enableEncryption(secretKey)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                session.profile = SessionService.authenticateUser(session.player.name, sharedSecret, server.encryption.publicKey, server.config.server.ip)
                if (!callLoginEvent()) return@launch
            } catch (exception: AuthenticationException) {
                session.disconnect(translatable { key("multiplayer.disconnect.unverified_username") })
                return@launch
            }
            enableCompression()

            session.player.uuid = session.profile.uuid
            sessionManager.beginPlayState(session)
        }
    }

    private fun verifyToken(expected: ByteArray, actual: ByteArray): Boolean {
        val decryptedActual = server.encryption.decrypt(actual)
        require(decryptedActual.contentEquals(expected)) {
            LOGGER.error("${session.player.name} failed verification! Expected ${expected.contentToString()}, received ${decryptedActual.contentToString()}!")
            session.disconnect(translatable {
                key("disconnect.loginFailedInfo")
                args(text { content("Verify tokens did not match!") })
            })
            return false
        }
        return true
    }

    private fun enableEncryption(key: SecretKey) {
        val encrypter = PacketEncrypter(key.toEncryptingCipher(Encryption.SHARED_SECRET_ALGORITHM))
        val decrypter = PacketDecrypter(key.toDecryptingCipher(Encryption.SHARED_SECRET_ALGORITHM))

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

    private fun enableCompression() {
        val threshold = server.config.server.compressionThreshold
        if (threshold <= 0) return

        val compressor = session.channel.pipeline()[PacketCompressor.NETTY_NAME]
        val decompressor = session.channel.pipeline()[PacketDecompressor.NETTY_NAME]

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

    private fun callLoginEvent(name: String = session.profile.name, uuid: UUID = session.profile.uuid): Boolean {
        val event = LoginEvent(name, uuid, session.channel.remoteAddress() as InetSocketAddress)
        server.eventBus.call(event)
        if (event.isCancelled) {
            session.disconnect(event.cancelledReason)
            return false
        }
        return true
    }

    companion object {

        private val LOGGER = logger<LoginHandler>()
    }
}