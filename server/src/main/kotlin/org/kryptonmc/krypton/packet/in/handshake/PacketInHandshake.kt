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
package org.kryptonmc.krypton.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketInfo
import org.kryptonmc.krypton.network.PacketState
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt

class PacketInHandshake(buf: ByteBuf) : Packet {

    override val info = PacketInfo(0x00, PacketState.HANDSHAKE)

    val protocol = buf.readVarInt()
    val address = buf.readString()
    val port = buf.readUnsignedShort().toUShort()
    val nextState = buf.readEnum<PacketState>()
}
