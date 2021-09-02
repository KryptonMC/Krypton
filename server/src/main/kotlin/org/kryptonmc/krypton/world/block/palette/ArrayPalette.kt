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
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.toBlock
import org.kryptonmc.nbt.ListTag

class ArrayPalette(private val bits: Int, private val resizer: (Int, Block) -> Int) : Palette {

    private val values = arrayOfNulls<Block>(1 shl bits)
    override var size = 0
    override val serializedSize: Int
        get() {
            var temp = size.varIntBytes
            for (i in 0 until size) temp += KryptonBlock.STATES.idOf(values[i]!!).varIntBytes
            return temp
        }

    override fun get(value: Block): Int {
        for (i in 0 until size) {
            if (values[i] === value) return i
        }

        val size = size
        return if (size < values.size) {
            values[size] = value
            ++this.size
            size
        } else {
            resizer(bits + 1, value)
        }
    }

    override fun get(id: Int) = if (id in 0 until size) values[id] else null

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(size)
        for (i in 0 until size) buf.writeVarInt(KryptonBlock.STATES.idOf(values[i]!!))
    }

    override fun load(data: ListTag) {
        for (i in data.indices) {
            values[i] = data.getCompound(i).toBlock()
        }
        size = data.size
    }
}
