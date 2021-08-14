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
package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInChangeHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlaceBlock
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.`in`.play.PacketInTeleportConfirm
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest

/**
 * Responsible for registering all of the inbound packets to their respective states.
 */
object PacketLoader {

    fun loadAll() {
        // HANDSHAKE
        PacketState.HANDSHAKE.registerPacketType(0x00, ::PacketInHandshake)

        // STATUS
        PacketState.STATUS.registerPacketType(0x00) { PacketInStatusRequest() }
        PacketState.STATUS.registerPacketType(0x01, ::PacketInPing)

        // LOGIN
        PacketState.LOGIN.registerPacketType(0x00, ::PacketInLoginStart)
        PacketState.LOGIN.registerPacketType(0x01, ::PacketInEncryptionResponse)
        PacketState.LOGIN.registerPacketType(0x02, ::PacketInPluginResponse)

        // PLAY
        PacketState.PLAY.registerPacketType(0x00, ::PacketInTeleportConfirm)
        PacketState.PLAY.registerPacketType(0x03, ::PacketInChat)
        PacketState.PLAY.registerPacketType(0x04, ::PacketInClientStatus)
        PacketState.PLAY.registerPacketType(0x05, ::PacketInClientSettings)
        PacketState.PLAY.registerPacketType(0x06, ::PacketInTabComplete)
        PacketState.PLAY.registerPacketType(0x0A, ::PacketInPluginMessage)
        PacketState.PLAY.registerPacketType(0x0F, ::PacketInKeepAlive)
        PacketState.PLAY.registerPacketType(0x11, ::PacketInPlayerPosition)
        PacketState.PLAY.registerPacketType(0x12, ::PacketInPlayerPositionAndRotation)
        PacketState.PLAY.registerPacketType(0x13, ::PacketInPlayerRotation)
        PacketState.PLAY.registerPacketType(0x19, ::PacketInAbilities)
        PacketState.PLAY.registerPacketType(0x1A, ::PacketInPlayerDigging)
        PacketState.PLAY.registerPacketType(0x1B, ::PacketInEntityAction)
        PacketState.PLAY.registerPacketType(0x25, ::PacketInChangeHeldItem)
        PacketState.PLAY.registerPacketType(0x28, ::PacketInCreativeInventoryAction)
        PacketState.PLAY.registerPacketType(0x2C, ::PacketInAnimation)
        PacketState.PLAY.registerPacketType(0x2E, ::PacketInPlaceBlock)
    }
}
