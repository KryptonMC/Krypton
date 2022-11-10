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
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class MapPalette<T>(
    private val registry: IntBiMap<T>,
    private val bits: Int,
    private val resizer: PaletteResizer<T>,
    private val values: IntIdentityHashBiMap<T>
) : Palette<T> {

    val entries: Sequence<T>
        get() = values.asSequence()
    override val size: Int
        get() = values.size

    constructor(registry: IntBiMap<T>, bits: Int, resizer: PaletteResizer<T>, entries: List<T>) : this(registry, bits, resizer) {
        entries.forEach(values::add)
    }

    constructor(
        registry: IntBiMap<T>,
        bits: Int,
        resizer: PaletteResizer<T>
    ) : this(registry, bits, resizer, IntIdentityHashBiMap.create(1 shl bits))

    override fun get(value: T): Int {
        var id = values.getId(value)
        if (id == -1) {
            id = values.add(value)
            if (id >= 1 shl bits) id = resizer.onResize(bits + 1, value)
        }
        return id
    }

    override fun get(id: Int): T = values.get(id) ?: throw MissingPaletteEntryException(id)

    override fun write(buf: ByteBuf) {
        val size = size
        buf.writeVarInt(size)
        for (i in 0 until size) {
            buf.writeVarInt(registry.getId(values.get(i)!!))
        }
    }

    override fun calculateSerializedSize(): Int {
        var size = size.varIntBytes()
        for (i in 0 until this.size) {
            size += registry.getId(values.get(i)!!).varIntBytes()
        }
        return size
    }

    override fun copy(): Palette<T> = MapPalette(registry, bits, resizer, values.copy())

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> =
            MapPalette(registry, bits, resizer, entries)
    }
}
