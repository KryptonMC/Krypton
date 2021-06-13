/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.handlers

import com.velocitypowered.natives.util.Natives
import io.netty.buffer.Unpooled
import kotlinx.coroutines.launch
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.event.login.LoginEvent
import org.kryptonmc.krypton.IOScope
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.BungeeCordHandshakeData
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.packet.session.SessionManager
import org.kryptonmc.krypton.packet.transformers.PacketCompressor
import org.kryptonmc.krypton.packet.transformers.PacketDecoder
import org.kryptonmc.krypton.packet.transformers.PacketDecompressor
import org.kryptonmc.krypton.packet.transformers.PacketDecrypter
import org.kryptonmc.krypton.packet.transformers.PacketEncoder
import org.kryptonmc.krypton.packet.transformers.PacketEncrypter
import org.kryptonmc.krypton.packet.transformers.SizeDecoder
import org.kryptonmc.krypton.packet.transformers.SizeEncoder
import org.kryptonmc.krypton.util.encryption.Encryption
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readVelocityData
import org.kryptonmc.krypton.util.verifyIntegrity
import java.net.InetSocketAddress
import java.util.UUID
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

/**
 * Handles all inbound packets in the [Login][org.kryptonmc.krypton.packet.state.PacketState.LOGIN] state.
 *
 * There are two inbound packets in this state that we handle:
 * - [Login Start][org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart] -
 *   sent to initiate the login sequence
 * - [Encryption Response][org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse] -
 *   sent to confirm the client wants to enable encryption
 */
class LoginHandler(
    override val server: KryptonServer,
    private val sessionManager: SessionManager,
    override val session: Session,
    private val bungeecordData: BungeeCordHandshakeData?
) : PacketHandler {

    private val velocityMessageId = Random.nextInt(Short.MAX_VALUE.toInt())
    private var name = ""

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    override fun handle(packet: Packet) = when (packet) {
        is PacketInLoginStart -> handleLoginStart(packet)
        is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
        is PacketInPluginResponse -> handlePluginResponse(packet)
        else -> Unit
    }

    private fun handleLoginStart(packet: PacketInLoginStart) {
        val rawAddress = session.channel.remoteAddress() as InetSocketAddress
        val address = if (bungeecordData != null) InetSocketAddress(bungeecordData.forwardedIp, rawAddress.port) else rawAddress

        if (!server.isOnline) {
            if (server.config.proxy.mode == ForwardingMode.MODERN) {
                name = packet.name
                session.sendPacket(PacketOutPluginRequest(velocityMessageId, VELOCITY_CHANNEL_ID, ByteArray(0)))
                return
            }

            val uuid = if (bungeecordData != null) {
                session.profile = GameProfile(bungeecordData.uuid, packet.name, bungeecordData.properties)
                bungeecordData.uuid
            } else {
                // Note: Per the protocol, offline players use UUID v3, rather than UUID v4.
                val offlineUUID = UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
                session.profile = GameProfile(offlineUUID, packet.name, emptyList())
                offlineUUID
            }
            session.player = KryptonPlayer(packet.name, uuid, server, session, address)
            if (!callLoginEvent(packet.name, uuid)) return

            sessionManager.beginPlayState(session)
            return
        }

        name = packet.name
        session.sendPacket(PacketOutEncryptionRequest(Encryption.publicKey, verifyToken))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        if (!verifyToken(verifyToken, packet.verifyToken)) return
        val sharedSecret = Encryption.decrypt(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        enableEncryption(secretKey)

        IOScope.launch {
            try {
                session.profile = SessionService.authenticateUser(name, sharedSecret, server.config.server.ip)
                if (!callLoginEvent()) return@launch
            } catch (exception: AuthenticationException) {
                session.disconnect(translatable("multiplayer.disconnect.unverified_username"))
                return@launch
            }
            enableCompression()

            val rawAddress = session.channel.remoteAddress() as InetSocketAddress
            val address = if (bungeecordData != null) InetSocketAddress(bungeecordData.forwardedIp, rawAddress.port) else rawAddress
            session.player = KryptonPlayer(name, session.profile.uuid, server, session, address)
            sessionManager.beginPlayState(session)
        }
    }

    private fun handlePluginResponse(packet: PacketInPluginResponse) {
        if (!packet.isSuccessful) // not successful, we don't care for now
        if (packet.messageId != velocityMessageId || server.config.proxy.mode != ForwardingMode.MODERN) return // not Velocity, ignore (for now)
        if (packet.data.isEmpty()) error("Velocity sent no data in its login plugin response!")
        val secret = server.config.proxy.secret.encodeToByteArray()

        val buffer = Unpooled.copiedBuffer(packet.data)
        val isSuccess = buffer.verifyIntegrity(secret)
        if (!isSuccess) {
            session.disconnect(INVALID_VELOCITY_RESPONSE)
            return
        }

        val data = buffer.readVelocityData()
        val address = session.channel.remoteAddress() as InetSocketAddress

        LOGGER.debug("Detected Velocity login for ${data.uuid}")
        session.profile = GameProfile(data.uuid, data.username, data.properties)
        session.player = KryptonPlayer(data.username, data.uuid, server, session, InetSocketAddress(data.remoteAddress, address.port))
        sessionManager.beginPlayState(session)
    }

    private fun verifyToken(expected: ByteArray, actual: ByteArray): Boolean {
        val decryptedActual = Encryption.decrypt(actual)
        require(decryptedActual.contentEquals(expected)) {
            Messages.NETWORK.LOGIN.FAIL_VERIFY_ERROR.error(LOGGER, session.player.name, expected.contentToString(), decryptedActual.contentToString())
            session.disconnect(translatable("disconnect.loginFailedInfo", listOf(Messages.NETWORK.LOGIN.FAIL_VERIFY())))
            return false
        }
        return true
    }

    private fun enableEncryption(key: SecretKey) {
        val cipher = Natives.cipher.get()
        val encrypter = PacketEncrypter(cipher.forEncryption(key))
        val decrypter = PacketDecrypter(cipher.forDecryption(key))
        session.channel.pipeline().addBefore(SizeDecoder.NETTY_NAME, PacketDecrypter.NETTY_NAME, decrypter)
        session.channel.pipeline().addBefore(SizeEncoder.NETTY_NAME, PacketEncrypter.NETTY_NAME, encrypter)
    }

    private fun enableCompression() {
        val threshold = server.config.server.compressionThreshold
        if (threshold == -1) {
            session.channel.pipeline().remove(PacketCompressor.NETTY_NAME)
            session.channel.pipeline().remove(PacketDecompressor.NETTY_NAME)
            return
        }

        var encoder = session.channel.pipeline()[PacketCompressor.NETTY_NAME] as? PacketCompressor
        var decoder = session.channel.pipeline()[PacketDecompressor.NETTY_NAME] as? PacketDecompressor
        if (encoder != null && decoder != null) {
            encoder.threshold = threshold
            decoder.threshold = threshold
            return
        }

        session.sendPacket(PacketOutSetCompression(threshold))
        val compressor = Natives.compress.get().create(4)
        encoder = PacketCompressor(compressor, threshold)
        decoder = PacketDecompressor(compressor, threshold)

        session.channel.pipeline().remove(SizeEncoder.NETTY_NAME) // The compressor does this itself
        session.channel.pipeline().addBefore(PacketDecoder.NETTY_NAME, PacketDecompressor.NETTY_NAME, decoder)
        session.channel.pipeline().addBefore(PacketEncoder.NETTY_NAME, PacketCompressor.NETTY_NAME, encoder)
    }

    private fun callLoginEvent(name: String = session.profile.name, uuid: UUID = session.profile.uuid): Boolean {
        val event = LoginEvent(name, uuid, session.channel.remoteAddress() as InetSocketAddress)
        val result = server.eventManager.fireSync(event).result
        if (!result.isAllowed) {
            val reason = if (result.reason !== Component.empty()) result.reason else translatable("multiplayer.disconnect.kicked")
            session.disconnect(reason)
            return false
        }
        return true
    }

    companion object {

        private val VELOCITY_CHANNEL_ID = Key.key("velocity", "player_info")
        private val INVALID_VELOCITY_RESPONSE = text("Invalid proxy response!", NamedTextColor.RED)
        private val LOGGER = logger<LoginHandler>()
    }
}
