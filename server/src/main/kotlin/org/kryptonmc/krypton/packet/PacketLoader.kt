package org.kryptonmc.krypton.packet

import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInHeldItemChange
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerBlockPlacement
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.`in`.play.PacketInTeleportConfirm
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.state.PacketState

/**
 * Responsible for registering all of the inbound packets to their respective states.
 */
object PacketLoader {

    fun loadAll() {
        // HANDSHAKE
        PacketState.HANDSHAKE.registerPacketType(0x00, ::PacketInHandshake)

        // STATUS
        PacketState.STATUS.registerPacketType(0x00, ::PacketInStatusRequest)
        PacketState.STATUS.registerPacketType(0x01, ::PacketInPing)

        // LOGIN
        PacketState.LOGIN.registerPacketType(0x00, ::PacketInLoginStart)
        PacketState.LOGIN.registerPacketType(0x01, ::PacketInEncryptionResponse)

        // PLAY
        PacketState.PLAY.registerPacketType(0x00, ::PacketInTeleportConfirm)
        PacketState.PLAY.registerPacketType(0x03, ::PacketInChat)
        PacketState.PLAY.registerPacketType(0x05, ::PacketInClientSettings)
        PacketState.PLAY.registerPacketType(0x06, ::PacketInTabComplete)
        PacketState.PLAY.registerPacketType(0x0B, ::PacketInPluginMessage)
        PacketState.PLAY.registerPacketType(0x10, ::PacketInKeepAlive)
        PacketState.PLAY.registerPacketType(0x12, ::PacketInPlayerPosition)
        PacketState.PLAY.registerPacketType(0x13, ::PacketInPlayerPositionAndRotation)
        PacketState.PLAY.registerPacketType(0x14, ::PacketInPlayerRotation)
        PacketState.PLAY.registerPacketType(0x15, ::PacketInPlayerMovement)
        PacketState.PLAY.registerPacketType(0x1A, ::PacketInPlayerAbilities)
        PacketState.PLAY.registerPacketType(0x1C, ::PacketInEntityAction)
        PacketState.PLAY.registerPacketType(0x25, ::PacketInHeldItemChange)
        PacketState.PLAY.registerPacketType(0x28, ::PacketInCreativeInventoryAction)
        PacketState.PLAY.registerPacketType(0x2C, ::PacketInAnimation)
        PacketState.PLAY.registerPacketType(0x2E, ::PacketInPlayerBlockPlacement)
    }
}
