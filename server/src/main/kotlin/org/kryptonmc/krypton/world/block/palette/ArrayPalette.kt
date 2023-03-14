/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.map.IntBiMap
import org.kryptonmc.krypton.util.writeVarInt

@Suppress("UNCHECKED_CAST")
class ArrayPalette<T> private constructor(
    private val registry: IntBiMap<T>,
    private val values: Array<T?>,
    private val resizer: PaletteResizer<T>,
    private val bits: Int,
    private var size: Int
) : Palette<T> {

    private constructor(registry: IntBiMap<T>, bits: Int, resizer: PaletteResizer<T>,
                        entries: List<T>) : this(registry, arrayOfNulls<Any>(1 shl bits) as Array<T?>, resizer, bits, entries.size) {
        require(entries.size <= values.size) {
            "Failed to initialise array palette with entries $entries! Entries size (${entries.size}) must be < palette size (${1 shl bits})!"
        }
        for (i in entries.indices) {
            values[i] = entries.get(i)
        }
    }

    override fun size(): Int = size

    override fun getId(value: T): Int {
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
            buf.writeVarInt(registry.getId(values[i]!!))
        }
    }

    override fun calculateSerializedSize(): Int {
        var size = ByteBufExtras.getVarIntBytes(size())
        for (i in 0 until this.size) {
            size += ByteBufExtras.getVarIntBytes(registry.getId(values[i]!!))
        }
        return size
    }

    override fun copy(): Palette<T> = ArrayPalette(registry, values.clone(), resizer, bits, size)

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> =
            ArrayPalette(registry, bits, resizer, entries)
    }
}
