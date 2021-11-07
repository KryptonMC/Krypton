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
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.toBlock
import org.kryptonmc.krypton.world.block.toNBT
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.list
import java.util.function.ToIntFunction

sealed class MapPalette<T>(
    private val bits: Int,
    private val resizer: PaletteResizer<T>,
    private val idProvider: ToIntFunction<T>,
    private val serializer: (T) -> Tag,
    private val deserializer: (Tag) -> T
) : Palette<T> {

    private val values = IntIdentityHashBiMap<T>(1 shl bits)
    override val size: Int
        get() = values.size
    override val serializedSize: Int
        get() {
            var temp = size.varIntBytes
            for (i in 0 until size) {
                temp += idProvider.applyAsInt(values[i]!!).varIntBytes
            }
            return temp
        }

    fun save() = list {
        for (i in 0 until this@MapPalette.size) {
            add(serializer(values[i]!!))
        }
    }

    override fun get(value: T): Int {
        var id = values.idOf(value)
        if (id == -1) {
            id = values.add(value)
            if (id >= 1 shl bits) id = resizer(bits + 1, value)
        }
        return id
    }

    override fun get(id: Int) = values[id]

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(size)
        for (i in 0 until size) buf.writeVarInt(idProvider.applyAsInt(values[i]!!))
    }

    override fun load(data: ListTag) {
        values.clear()
        for (i in data.indices) {
            values.add(deserializer(data.getCompound(i)))
        }
    }

    class Blocks(
        bits: Int,
        resizer: PaletteResizer<Block>
    ) : MapPalette<Block>(bits, resizer, { BlockLoader.STATES.idOf(it) }, Block::toNBT, { (it as CompoundTag).toBlock() })
}
