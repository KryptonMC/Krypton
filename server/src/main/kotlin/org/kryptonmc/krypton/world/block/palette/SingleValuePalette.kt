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

import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.map.IntBiMap

class SingleValuePalette<T>(private val registry: IntBiMap<T>, private val resizer: PaletteResizer<T>, entries: List<T>) : Palette<T> {

    private var value: T? = null

    init {
        if (entries.isNotEmpty()) {
            require(entries.size <= 1) { "Cannot initialise a single value palette with more than 1 value!" }
            value = entries.first()
        }
    }

    override fun size(): Int = 1

    override fun getId(value: T): Int {
        if (this.value != null && this.value != value) return resizer.onResize(1, value)
        this.value = value
        return 0
    }

    override fun get(id: Int): T {
        check(value != null && id == 0) { "Missing palette entry for ID $id!" }
        return value!!
    }

    override fun write(writer: BinaryWriter) {
        checkInit()
        writer.writeVarInt(registry.getId(value!!))
    }

    override fun calculateSerializedSize(): Int {
        checkInit()
        return ByteBufExtras.getVarIntBytes(registry.getId(value!!))
    }

    override fun copy(): Palette<T> {
        checkInit()
        return this
    }

    private fun checkInit() {
        checkNotNull(value) { "Attempted to use an uninitialised single value palette!" }
    }

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> =
            SingleValuePalette(registry, resizer, entries)
    }
}
