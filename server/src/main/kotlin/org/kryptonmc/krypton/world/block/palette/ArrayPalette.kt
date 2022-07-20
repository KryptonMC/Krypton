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
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt

@Suppress("UNCHECKED_CAST")
class ArrayPalette<T> private constructor(
    private val registry: IntBiMap<T>,
    private val values: Array<T?>,
    private val resizer: PaletteResizer<T>,
    private val bits: Int,
    size: Int
) : Palette<T> {

    override var size: Int = size
        private set

    private constructor(
        registry: IntBiMap<T>,
        bits: Int,
        resizer: PaletteResizer<T>,
        entries: List<T>
    ) : this(registry, arrayOfNulls<Any>(1 shl bits) as Array<T?>, resizer, bits, entries.size) {
        require(entries.size <= values.size) {
            "Failed to initialise array palette with entries $entries! Entries size (${entries.size}) must be < palette size (${1 shl bits})!"
        }
        for (i in entries.indices) {
            values[i] = entries[i]
        }
    }

    override fun get(value: T): Int {
        for (i in 0 until size) {
            if (values[i] === value) return i
        }

        val size = size
        if (size < values.size) {
            values[size] = value
            this.size++
            return size
        }
        return resizer.onResize(bits + 1, value)
    }

    override fun get(id: Int): T = values.getOrNull(id) ?: throw MissingPaletteEntryException(id)

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(size)
        for (i in 0 until size) {
            buf.writeVarInt(registry.idOf(values[i]!!))
        }
    }

    override fun calculateSerializedSize(): Int {
        var size = size.varIntBytes()
        for (i in 0 until this.size) {
            size += registry.idOf(values[i]!!).varIntBytes()
        }
        return size
    }

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> =
            ArrayPalette(registry, bits, resizer, entries)
    }
}
