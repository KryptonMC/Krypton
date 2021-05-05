/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarLong
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.KryptonWorldBorder

/**
 * Sets the world border for the world. The only action supported for this at the moment is [INITIALIZE][BorderAction.INITIALIZE]
 *
 * @param action the action for this packet
 * @param border the world border
 */
class PacketOutWorldBorder(
    private val action: BorderAction,
    private val border: KryptonWorldBorder
) : PlayPacket(0x3D) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)

        buf.writeDouble(border.center.x)
        buf.writeDouble(border.center.z)
        buf.writeDouble(border.size)
        buf.writeDouble(border.size)
        buf.writeVarLong(0)
        buf.writeVarInt(29999984) // portal teleport boundary - generally this value
        buf.writeVarInt(border.warningBlocks.toInt())
        buf.writeVarInt(border.warningTime.toInt())
    }
}

enum class BorderAction(val id: Int) {

    INITIALIZE(3)
}
