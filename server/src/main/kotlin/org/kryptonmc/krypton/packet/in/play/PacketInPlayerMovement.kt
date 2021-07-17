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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

open class PacketInPlayerMovement(id: Int = 0x14) : PlayPacket(id) {

    class PacketInPlayerPosition(buf: ByteBuf) : PacketInPlayerMovement(0x11) {

        val x = buf.readDouble()
        val y = buf.readDouble()
        val z = buf.readDouble()
        val onGround = buf.readBoolean()
    }

    class PacketInPlayerRotation(buf: ByteBuf) : PacketInPlayerMovement(0x13) {

        val yaw = buf.readFloat()
        val pitch = buf.readFloat()
        val onGround = buf.readBoolean()
    }

    class PacketInPlayerPositionAndRotation(buf: ByteBuf) : PacketInPlayerMovement(0x12) {

        val x = buf.readDouble()
        val y = buf.readDouble()
        val z = buf.readDouble()
        val yaw = buf.readFloat()
        val pitch = buf.readFloat()
        val onGround = buf.readBoolean()
    }
}
