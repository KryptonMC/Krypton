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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.event.login.LoginEvent
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.handshake.BungeeCordHandshakeData
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
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
import org.kryptonmc.krypton.util.encryption.toDecryptingCipher
import org.kryptonmc.krypton.util.encryption.toEncryptingCipher
import org.kryptonmc.krypton.util.logger
import java.net.InetSocketAddress
import java.util.UUID
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

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
        val rawAddress = session.channel.remoteAddress() as InetSocketAddress
        val address = if (bungeecordData != null) InetSocketAddress(bungeecordData.forwardedIp, rawAddress.port) else rawAddress
        session.player = KryptonPlayer(packet.name, server, session, address)

        if (!server.isOnline) {
            val uuid = if (bungeecordData != null) {
                session.player.uuid = bungeecordData.uuid
                session.profile = GameProfile(bungeecordData.uuid, packet.name, bungeecordData.properties)
                bungeecordData.uuid
            } else {
                // Note: Per the protocol, offline players use UUID v3, rather than UUID v4.
                val offlineUUID = UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
                session.player.uuid = offlineUUID
                session.profile = GameProfile(offlineUUID, packet.name, emptyList())
                offlineUUID
            }
            if (!callLoginEvent(packet.name, uuid)) return

            sessionManager.beginPlayState(session)
            return
        }

        session.sendPacket(PacketOutEncryptionRequest(Encryption.publicKey, verifyToken))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        if (!verifyToken(verifyToken, packet.verifyToken)) return
        val sharedSecret = Encryption.decrypt(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        enableEncryption(secretKey)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                session.profile = SessionService.authenticateUser(session.player.name, sharedSecret, server.config.server.ip)
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
        val decryptedActual = Encryption.decrypt(actual)
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
