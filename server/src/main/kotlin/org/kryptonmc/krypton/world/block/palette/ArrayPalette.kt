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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.util.toKeyOrNull
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.toBlock
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import java.util.function.ToIntFunction

@Suppress("UNCHECKED_CAST")
sealed class ArrayPalette<T>(
    private val bits: Int,
    private val resizer: PaletteResizer<T>,
    private val idProvider: ToIntFunction<T>,
    private val deserializer: (Tag) -> T
) : Palette<T> {

    private val values = arrayOfNulls<Any>(1 shl bits) as Array<T?>
    override var size = 0
    override val serializedSize: Int
        get() {
            var temp = size.varIntBytes
            for (i in 0 until size) temp += idProvider.applyAsInt(values[i]!!).varIntBytes
            return temp
        }

    override fun get(value: T): Int {
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
        for (i in 0 until size) buf.writeVarInt(idProvider.applyAsInt(values[i]!!))
    }

    override fun load(data: ListTag) {
        for (i in data.indices) {
            values[i] = deserializer(data.getCompound(i))
        }
        size = data.size
    }

    class Blocks(
        bits: Int,
        resizer: PaletteResizer<Block>
    ) : ArrayPalette<Block>(bits, resizer, { BlockLoader.STATES.idOf(it) }, { (it as CompoundTag).toBlock() })

    class Biomes(
        bits: Int,
        resizer: PaletteResizer<Biome>
    ) : ArrayPalette<Biome>(bits, resizer, { Registries.BIOME.idOf(it) }, {
        it as StringTag
        val key = it.value.toKeyOrNull() ?: error("Cannot convert ${it.value} to a valid biome key!")
        Registries.BIOME[key] ?: error("No biome found with key $key!")
    })
}
