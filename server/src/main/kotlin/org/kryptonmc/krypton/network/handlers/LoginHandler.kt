/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.network.handlers

import com.velocitypowered.natives.util.Natives
import io.netty.buffer.Unpooled
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.event.auth.AuthenticationEvent
import org.kryptonmc.api.event.player.LoginEvent
import org.kryptonmc.api.event.server.SetupPermissionsEvent
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.config.category.ForwardingMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.network.data.ForwardedData
import org.kryptonmc.krypton.network.data.VelocityProxy
import org.kryptonmc.krypton.network.netty.GroupedPacketHandler
import org.kryptonmc.krypton.network.netty.PacketCompressor
import org.kryptonmc.krypton.network.netty.PacketDecoder
import org.kryptonmc.krypton.network.netty.PacketDecompressor
import org.kryptonmc.krypton.network.netty.PacketDecrypter
import org.kryptonmc.krypton.network.netty.PacketEncoder
import org.kryptonmc.krypton.network.netty.PacketEncrypter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.`in`.login.VerificationData
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.util.ComponentException
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.crypto.InsecurePublicKeyException
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.readVarInt
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.security.SecureRandom
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Handles all inbound packets in the [Login][org.kryptonmc.krypton.packet.PacketState.LOGIN]
 * state.
 *
 * There are two inbound packets in this state that we handle:
 * - [Login Start][org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart] -
 *   sent to initiate the login sequence
 * - [Encryption Response][org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse] -
 *   sent to confirm the client wants to enable encryption
 */
class LoginHandler(
    override val server: KryptonServer,
    override val session: SessionHandler,
    private val proxyForwardedData: ForwardedData?
) : PacketHandler {

    private val playerManager = server.playerManager

    // Doesn't really matter what this is, just needs to be unique.
    private val velocityMessageId = ThreadLocalRandom.current().nextInt(Short.MAX_VALUE.toInt())
    private val forwardingSecret = server.config.proxy.secret.encodeToByteArray()

    private var name = "" // We cache the name here to avoid late initialization of the KryptonPlayer object.
    private val verifyToken = generateVerifyToken()
    private var publicKey: PlayerPublicKey? = null

    override fun handle(packet: Packet) {
        when (packet) {
            is PacketInLoginStart -> handleLoginStart(packet)
            is PacketInEncryptionResponse -> handleEncryptionResponse(packet)
            is PacketInPluginResponse -> handlePluginResponse(packet)
            else -> Unit
        }
    }

    private fun handleLoginStart(packet: PacketInLoginStart) {
        name = packet.name
        try {
            publicKey = validatePublicKey(packet.publicKey, SignatureValidator.YGGDRASIL, server.config.server.enforceSecureProfiles)
        } catch (exception: PublicKeyParseException) {
            LOGGER.error(exception.message, exception.cause)
            disconnect(exception.asComponent())
            return
        }

        // Ignore online mode if we want proxy forwarding
        if (!server.isOnline || server.config.proxy.mode.authenticatesUsers) {
            if (server.config.proxy.mode == ForwardingMode.MODERN) {
                session.send(PacketOutPluginRequest(velocityMessageId, VELOCITY_CHANNEL_ID, ByteArray(0)))
                return
            }

            // Copy over the data from legacy forwarding
            // Note: Per the protocol, offline players use UUID v3, rather than UUID v4.
            val address = createAddress()
            val uuid = proxyForwardedData?.uuid ?: UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
            val profile = KryptonGameProfile(packet.name, uuid, proxyForwardedData?.properties ?: persistentListOf())

            // Check the player can join and the login event was not cancelled.
            if (!canJoin(profile, address) || !callLoginEvent(profile)) return

            // Initialize the player and setup their permissions.
            val player = KryptonPlayer(session, profile, server.worldManager.default, address)
            server.eventManager.fire(SetupPermissionsEvent(player, KryptonPlayer.DEFAULT_PERMISSIONS))
                .thenApplyAsync({ finishLogin(it, player) }, session.channel.eventLoop())
            return
        }

        // The server isn't offline and the client wasn't forwarded, enable encryption.
        session.send(PacketOutEncryptionRequest(Encryption.publicKey.encoded, verifyToken))
    }

    private fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        // Check that the token we sent them is what they sent back to us.
        if (!verifyToken(packet.verificationData)) return

        // We decrypt the shared secret with the server's private key and then create a new AES streaming
        // cipher to use for encryption and decryption (see https://wiki.vg/Protocol_Encryption).
        val sharedSecret = Encryption.decrypt(packet.secret)
        enableEncryption(SecretKeySpec(sharedSecret, Encryption.SYMMETRIC_ALGORITHM))

        val address = createAddress()
        // Fire the authentication event.
        server.eventManager.fire(AuthenticationEvent(name)).thenApplyAsync({
            if (!it.result.isAllowed) return@thenApplyAsync null

            val profile = SessionService.hasJoined(name, sharedSecret, server.config.server.ip)
            if (profile == null) {
                session.disconnect(Component.translatable("multiplayer.disconnect.unverified_username"))
                return@thenApplyAsync null
            }
            if (!canJoin(profile, address) || !callLoginEvent(profile)) return@thenApplyAsync null

            // Check the profile from the event and construct the player.
            val resultProfile = it.result.profile
            val finalProfile = if (resultProfile != null && resultProfile is KryptonGameProfile) resultProfile else profile
            KryptonPlayer(session, finalProfile, server.worldManager.default, address)
        }, session.channel.eventLoop()).thenApplyAsync({
            if (it == null) return@thenApplyAsync
            // Setup permissions.
            finishLogin(server.eventManager.fireSync(SetupPermissionsEvent(it, KryptonPlayer.DEFAULT_PERMISSIONS)), it)
        }, session.channel.eventLoop())
    }

    private fun handlePluginResponse(packet: PacketInPluginResponse) {
        if (packet.messageId != velocityMessageId || server.config.proxy.mode != ForwardingMode.MODERN) {
            disconnect(Component.translatable("multiplayer.disconnect.unexpected_query_response"))
            return
        }
        if (packet.data == null) {
            disconnect(Component.text("You must connect to this server through a Velocity proxy!"))
            return
        }
        if (packet.data.isEmpty()) { // For whatever reason, there was no data sent by Velocity
            LOGGER.error("Velocity sent no data in its login plugin response!")
            return
        }

        // Verify integrity
        val buffer = Unpooled.copiedBuffer(packet.data)
        if (!VelocityProxy.verifyIntegrity(buffer, forwardingSecret)) {
            disconnect(Component.text("Response received from Velocity could not be verified!"))
            return
        }

        val version = buffer.readVarInt()
        check(version <= VelocityProxy.MAX_SUPPORTED_FORWARDING_VERSION) {
            "Unsupported forwarding version $version! Supported up to: ${VelocityProxy.MAX_SUPPORTED_FORWARDING_VERSION}, version: $version"
        }

        val data = VelocityProxy.readData(buffer)
        val address = session.channel.remoteAddress() as InetSocketAddress

        if (version >= VelocityProxy.MODERN_FORWARDING_WITH_KEY && publicKey == null) {
            try {
                publicKey = PlayerPublicKey.create(SignatureValidator.YGGDRASIL, data.key)
            } catch (exception: Exception) {
                disconnect(Component.text("Could not validate public key forwarded from Velocity!"))
                return
            }
        }

        // All good to go, let's construct our stuff
        LOGGER.debug("Detected Velocity login for ${data.uuid}")
        val profile = KryptonGameProfile(data.username, data.uuid, data.properties)
        val player = KryptonPlayer(session, profile, server.worldManager.default, InetSocketAddress(data.remoteAddress, address.port))

        // Setup permissions for the player
        server.eventManager.fire(SetupPermissionsEvent(player, KryptonPlayer.DEFAULT_PERMISSIONS))
            .thenApplyAsync({ finishLogin(it, player) }, session.channel.eventLoop())
    }

    private fun finishLogin(event: SetupPermissionsEvent, player: KryptonPlayer) {
        player.permissionFunction = event.createFunction(player)
        enableCompression()
        session.writeAndFlush(PacketOutLoginSuccess(player.profile))
        session.handler = PlayHandler(server, session, player)
        session.currentState = PacketState.PLAY
        playerManager.add(player, session).whenComplete { _, exception ->
            if (exception == null) return@whenComplete
            LOGGER.error("Disconnecting player ${player.profile.name} due to exception caught whilst attempting to load them in...", exception)
            player.disconnect(Component.text("An unexpected exception occurred. Please contact the system administrator."))
        }
    }

    private fun verifyToken(verificationData: VerificationData): Boolean {
        if (publicKey != null && !verificationData.isSignatureValid(verifyToken, publicKey!!)) {
            LOGGER.error("Signature for public key was invalid for $name! Their connection may have been intercepted.")
            val message = Component.text("Signature for public key was invalid! Your connection may have been intercepted.")
            disconnect(Component.translatable("disconnect.loginFailedInfo", message))
            return false
        }
        if (publicKey == null && !verificationData.isTokenValid(verifyToken)) {
            LOGGER.error("Verify tokens for $name did not match! Their connection may have been intercepted.")
            val message = Component.text("Verify tokens did not match! Your connection may have been intercepted.")
            disconnect(Component.translatable("disconnect.loginFailedInfo", message))
            return false
        }
        return true
    }

    /**
     * Creates the ciphers for encryption and decryption with the given [key].
     * The key should be using an AES stream cipher with the key given from the
     * encryption response packet.
     */
    private fun enableEncryption(key: SecretKey) {
        val cipher = Natives.cipher.get()
        val encrypter = PacketEncrypter(cipher.forEncryption(key))
        val decrypter = PacketDecrypter(cipher.forDecryption(key))
        session.channel.pipeline().addBefore(GroupedPacketHandler.NETTY_NAME, PacketDecrypter.NETTY_NAME, decrypter)
        session.channel.pipeline().addBefore(GroupedPacketHandler.NETTY_NAME, PacketEncrypter.NETTY_NAME, encrypter)
    }

    private fun enableCompression() {
        val threshold = server.config.server.compressionThreshold

        // Check for existing encoders and decoders
        var encoder = session.channel.pipeline()[PacketCompressor.NETTY_NAME] as? PacketCompressor
        var decoder = session.channel.pipeline()[PacketDecompressor.NETTY_NAME] as? PacketDecompressor
        if (encoder != null && decoder != null) {
            encoder.threshold = threshold
            decoder.threshold = threshold
            return
        }

        // Tell the client to update its compression threshold and create our compressor
        session.writeAndFlush(PacketOutSetCompression(threshold))
        session.compressionEnabled = true
        val compressor = Natives.compress.get().create(4)
        encoder = PacketCompressor(compressor, threshold)
        decoder = PacketDecompressor(compressor, threshold)

        // Add the compressor and decompressor to our pipeline
        session.channel.pipeline().addBefore(PacketDecoder.NETTY_NAME, PacketDecompressor.NETTY_NAME, decoder)
        session.channel.pipeline().addBefore(PacketEncoder.NETTY_NAME, PacketCompressor.NETTY_NAME, encoder)
    }

    private fun callLoginEvent(profile: KryptonGameProfile): Boolean {
        val event = LoginEvent(profile.name, profile.uuid, session.channel.remoteAddress() as InetSocketAddress)
        val result = server.eventManager.fireSync(event).result
        if (!result.isAllowed) {
            disconnect(result.reason ?: Component.translatable("multiplayer.disconnect.kicked"))
            return false
        }
        return true
    }

    private fun canJoin(profile: KryptonGameProfile, address: SocketAddress): Boolean {
        val whitelist = playerManager.whitelist
        val whitelistedIps = playerManager.whitelistedIps
        if (playerManager.bannedPlayers.contains(profile)) { // We are banned
            val entry = playerManager.bannedPlayers[profile]!!
            val text = Component.translatable("multiplayer.disconnect.banned.reason", entry.reason)
            // Add the expiration date
            if (entry.expirationDate != null) {
                val expirationDate = Component.text(BanEntry.DATE_FORMATTER.format(entry.expirationDate))
                text.append(Component.translatable("multiplayer.disconnect.banned.expiration", expirationDate))
            }

            // Inform the client that they are banned
            disconnect(text)
            LOGGER.info("${profile.name} was disconnected as they are banned from this server.")
            return false
        } else if (playerManager.whitelistEnabled && !whitelist.contains(profile) && !whitelistedIps.isWhitelisted(address)) {
            // We are not whitelisted
            disconnect(Component.translatable("multiplayer.disconnect.not_whitelisted"))
            LOGGER.info("${profile.name} was disconnected as this server is whitelisted and they are not on the whitelist.")
            return false
        } else if (playerManager.bannedIps.isBanned(address)) { // Their IP is banned.
            val entry = playerManager.bannedIps[address]!!
            val text = Component.translatable().key("multiplayer.disconnect.banned_ip.reason").args(entry.reason)
            if (entry.expirationDate != null) {
                val expirationDate = Component.text(BanEntry.DATE_FORMATTER.format(entry.expirationDate))
                text.append(Component.translatable("multiplayer.disconnect.banned.expiration", expirationDate))
            }
            disconnect(text.build())
            LOGGER.info("${profile.name} disconnected. Reason: IP Banned")
        }
        return true
    }

    private fun createAddress(): InetSocketAddress {
        val rawAddress = session.channel.remoteAddress() as InetSocketAddress
        return if (proxyForwardedData != null) {
            val port = if (proxyForwardedData.forwardedPort != -1) proxyForwardedData.forwardedPort else rawAddress.port
            InetSocketAddress(proxyForwardedData.forwardedAddress, port)
        } else {
            rawAddress
        }
    }

    private fun disconnect(reason: Component) {
        session.writeAndFlush(PacketOutLoginDisconnect(reason))
        if (session.channel.isOpen) session.channel.close().awaitUninterruptibly()
    }

    class PublicKeyParseException(message: Component, cause: Throwable? = null) : ComponentException(message, cause)

    companion object {

        private const val VELOCITY_CHANNEL_ID = "velocity:player_info"
        private val RANDOM = SecureRandom()
        private val LOGGER = logger<LoginHandler>()

        private val MISSING_PUBLIC_KEY = Component.translatable("multiplayer.disconnect.missing_public_key")
        private val INVALID_SIGNATURE = Component.translatable("multiplayer.disconnect.invalid_public_key_signature")
        private val INVALID_PUBLIC_KEY = Component.translatable("multiplayer.disconnect.invalid_public_key")

        @JvmStatic
        private fun generateVerifyToken(): ByteArray {
            val bytes = ByteArray(4)
            RANDOM.nextBytes(bytes)
            return bytes
        }

        @JvmStatic
        private fun validatePublicKey(keyData: PlayerPublicKey.Data?, validator: SignatureValidator, requireValid: Boolean): PlayerPublicKey? {
            try {
                if (keyData == null) {
                    if (requireValid) throw PublicKeyParseException(MISSING_PUBLIC_KEY)
                    return null
                }
                return PlayerPublicKey.create(validator, keyData)
            } catch (exception: InsecurePublicKeyException.Missing) {
                if (requireValid) throw PublicKeyParseException(INVALID_SIGNATURE, exception) else return null
            } catch (exception: InsecurePublicKeyException.Invalid) {
                throw PublicKeyParseException(INVALID_PUBLIC_KEY, exception)
            } catch (exception: Exception) {
                throw PublicKeyParseException(INVALID_SIGNATURE, exception)
            }
        }
    }
}
