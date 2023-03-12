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

import com.google.common.primitives.Ints
import io.netty.buffer.Unpooled
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.auth.KryptonPlayerAuthenticationEvent
import org.kryptonmc.krypton.event.player.KryptonPlayerLoginEvent
import org.kryptonmc.krypton.event.server.KryptonSetupPermissionsEvent
import org.kryptonmc.krypton.locale.DisconnectMessages
import org.kryptonmc.krypton.locale.MinecraftTranslationManager
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.forwarding.ProxyForwardedData
import org.kryptonmc.krypton.network.forwarding.VelocityProxy
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.readVarInt
import java.net.InetSocketAddress
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
class LoginPacketHandler(
    private val server: KryptonServer,
    override val connection: NettyConnection,
    private val proxyForwardedData: ProxyForwardedData?
) : PacketHandler {

    // Doesn't really matter what this is, just needs to be unique.
    private val velocityMessageId = RANDOM.nextInt(Short.MAX_VALUE.toInt())
    private val forwardingSecret = server.config.proxy.secret.encodeToByteArray()

    private var name = "" // We cache the name here to avoid late initialization of the KryptonPlayer object.
    private val verifyToken = generateVerifyToken()

    fun handleLoginStart(packet: PacketInLoginStart) {
        name = packet.name

        // Ignore online mode if we want proxy forwarding
        if (!server.config.isOnline || server.config.proxy.mode.authenticatesUsers) {
            if (server.config.proxy.mode == ProxyCategory.Mode.MODERN) {
                // Try to establish Velocity connection.
                connection.writeNow(PacketOutPluginRequest(velocityMessageId, VELOCITY_CHANNEL_ID, ByteArray(0)))
            } else {
                processOfflineLogin(packet.name)
            }
            return
        }

        // The server isn't offline and the client wasn't forwarded, enable encryption.
        connection.writeNow(PacketOutEncryptionRequest.create(Encryption.publicKey.encoded, verifyToken))
    }

    private fun processOfflineLogin(name: String) {
        // Copy over the data from legacy forwarding
        // Note: Per the protocol, offline players use UUID v3, rather than UUID v4.
        val address = createAddress()
        val uuid = proxyForwardedData?.uuid ?: UUIDUtil.createOfflinePlayerId(name)
        val profile = KryptonGameProfile.full(uuid, name, proxyForwardedData?.properties ?: persistentListOf())

        // Check the player can join and the login event was not cancelled.
        if (!callLoginEvent(profile)) return

        // Initialize the player and setup their permissions.
        val player = KryptonPlayer(connection, profile, server.worldManager.default, address)
        val permissionsEvent = server.eventNode.fire(KryptonSetupPermissionsEvent(player, KryptonPlayer.DEFAULT_PERMISSION_FUNCTION))
        player.permissionFunction = permissionsEvent.result?.function ?: permissionsEvent.defaultFunction

        finishLogin(player)
    }

    fun handleEncryptionResponse(packet: PacketInEncryptionResponse) {
        // Check that the token we sent them is what they sent back to us.
        if (!verifyToken.contentEquals(Encryption.decrypt(packet.verifyToken))) return

        // We decrypt the shared secret with the server's private key and then create a new AES streaming
        // cipher to use for encryption and decryption (see https://wiki.vg/Protocol_Encryption).
        val sharedSecret = Encryption.decrypt(packet.secret)
        connection.enableEncryption(SecretKeySpec(sharedSecret, Encryption.SYMMETRIC_ALGORITHM))

        val address = createAddress()

        // Fire the authentication event.
        val authEvent = KryptonPlayerAuthenticationEvent(name)
        if (!authEvent.isAllowed()) return

        val profile = authEvent.result?.profile ?: SessionService.hasJoined(name, sharedSecret, server.config.server.ip)
        if (profile == null) {
            disconnect(DisconnectMessages.UNVERIFIED_USERNAME)
            return
        }

        if (!callLoginEvent(profile)) return
        val player = KryptonPlayer(connection, profile, server.worldManager.default, address)

        val permissionsEvent = server.eventNode.fire(KryptonSetupPermissionsEvent(player, KryptonPlayer.DEFAULT_PERMISSION_FUNCTION))
        player.permissionFunction = permissionsEvent.result?.function ?: permissionsEvent.defaultFunction
        finishLogin(player)
    }

    fun handlePluginResponse(packet: PacketInPluginResponse) {
        if (packet.messageId != velocityMessageId || server.config.proxy.mode != ProxyCategory.Mode.MODERN) {
            disconnect(DisconnectMessages.UNEXPECTED_QUERY_RESPONSE)
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
        val address = connection.connectAddress() as InetSocketAddress

        // All good to go, let's construct our stuff
        LOGGER.debug("Detected Velocity login for ${data.uuid}")
        val profile = KryptonGameProfile.full(data.uuid, data.username, data.properties)
        val player = KryptonPlayer(connection, profile, server.worldManager.default, InetSocketAddress(data.remoteAddress, address.port))

        // Setup permissions for the player
        val permissionsEvent = server.eventNode.fire(KryptonSetupPermissionsEvent(player, KryptonPlayer.DEFAULT_PERMISSION_FUNCTION))
        player.permissionFunction = permissionsEvent.result?.function ?: permissionsEvent.defaultFunction
        finishLogin(player)
    }

    private fun finishLogin(player: KryptonPlayer) {
        connection.enableCompression()
        connection.writeNow(PacketOutLoginSuccess.create(player.profile))
        connection.setState(PacketState.PLAY)
        connection.setHandler(PlayPacketHandler(server, connection, player))

        try {
            server.playerManager.addPlayer(player)
        } catch (exception: Exception) {
            LOGGER.error("Disconnecting player ${player.profile.name} due to exception caught whilst attempting to load them in...", exception)
            player.disconnect(Component.text("An unexpected exception occurred. Please contact the system administrator."))
        }
    }

    private fun callLoginEvent(profile: GameProfile): Boolean {
        val event = server.eventNode.fire(KryptonPlayerLoginEvent(profile, connection.connectAddress() as InetSocketAddress))
        if (!event.isAllowed()) {
            disconnect(event.result?.reason ?: DisconnectMessages.KICKED)
            return false
        }
        return true
    }

    private fun createAddress(): InetSocketAddress {
        val rawAddress = connection.connectAddress() as InetSocketAddress
        return if (proxyForwardedData != null) {
            val port = if (proxyForwardedData.forwardedPort != -1) proxyForwardedData.forwardedPort else rawAddress.port
            InetSocketAddress(proxyForwardedData.forwardedAddress, port)
        } else {
            rawAddress
        }
    }

    private fun disconnect(reason: Component) {
        val translated = MinecraftTranslationManager.render(reason)
        LOGGER.info("Disconnecting ${formatName()}: ${PlainTextComponentSerializer.plainText().serialize(translated)}")
        connection.writeAndDisconnect(PacketOutLoginDisconnect(reason), reason)
    }

    override fun onDisconnect(message: Component) {
        val translated = MinecraftTranslationManager.render(message)
        LOGGER.info("${formatName()} was disconnected: ${PlainTextComponentSerializer.plainText().serialize(translated)}")
    }

    private fun formatName(): String = "$name (${connection.connectAddress()})"

    companion object {

        private const val VELOCITY_CHANNEL_ID = "velocity:player_info"
        private val RANDOM = RandomSource.createThreadSafe()
        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun generateVerifyToken(): ByteArray = Ints.toByteArray(RANDOM.nextInt())
    }
}
