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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.util.readBlockPos
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeBlockPos
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

@JvmRecord
data class PacketOutBlockUpdate(val position: BlockPos, val block: KryptonBlockState) : Packet {

    constructor(buf: ByteBuf) : this(buf.readBlockPos(), KryptonBlock.stateFromId(buf.readVarInt()))

    override fun write(buf: ByteBuf) {
        buf.writeBlockPos(position)
        buf.writeVarInt(KryptonBlock.idOf(block))
    }
}
