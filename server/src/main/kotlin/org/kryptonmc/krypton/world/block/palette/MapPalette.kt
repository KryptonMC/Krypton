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
import org.kryptonmc.krypton.util.map.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class MapPalette<T>(
    private val registry: IntBiMap<T>,
    private val bits: Int,
    private val resizer: PaletteResizer<T>,
    private val values: IntIdentityHashBiMap<T>
) : Palette<T> {

    constructor(registry: IntBiMap<T>, bits: Int, resizer: PaletteResizer<T>, entries: List<T>) : this(registry, bits, resizer) {
        entries.forEach(values::add)
    }

    constructor(registry: IntBiMap<T>, bits: Int,
                resizer: PaletteResizer<T>) : this(registry, bits, resizer, IntIdentityHashBiMap.create(1 shl bits))

    fun entries(): Sequence<T> = values.asSequence()

    override fun size(): Int = values.size()

    override fun getId(value: T): Int {
        var id = values.getId(value)
        if (id == -1) {
            id = values.add(value)
            if (id >= 1 shl bits) id = resizer.onResize(bits + 1, value)
        }
        return id
    }

    override fun get(id: Int): T = values.get(id) ?: throw MissingPaletteEntryException(id)

    override fun write(buf: ByteBuf) {
        val size = size()
        buf.writeVarInt(size)
        for (i in 0 until size) {
            buf.writeVarInt(registry.getId(values.get(i)!!))
        }
    }

    override fun calculateSerializedSize(): Int {
        var size = ByteBufExtras.getVarIntBytes(size())
        for (i in 0 until size()) {
            size += ByteBufExtras.getVarIntBytes(registry.getId(values.get(i)!!))
        }
        return size
    }

    override fun copy(): Palette<T> = MapPalette(registry, bits, resizer, values.copy())

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> =
            MapPalette(registry, bits, resizer, entries)
    }
}
