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
package org.kryptonmc.krypton.packet.out.play.chunk

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.writePosition
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.palette.GlobalPalette

/**
 * Update a block in a chunk
 */
class PacketOutBlockChange(private val block: KryptonBlock) : PlayPacket(0x0B) {

    override fun write(buf: ByteBuf) {
        buf.writePosition(block.location)
        buf.writeVarInt(GlobalPalette[block.type.key()].states.firstOrNull()?.id ?: 0)
    }
}
