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

class SingleValuePalette<T>(
    private val registry: IntBiMap<T>,
    private val resizer: PaletteResizer<T>,
    entries: List<T>
) : Palette<T> {

    private var value: T? = null
    override val size: Int = 1
    override val serializedSize: Int
        get() {
            checkNotNull(value) { "Attempted to use an uninitialised single value palette!" }
            return registry.idOf(value!!).varIntBytes()
        }

    init {
        if (entries.isNotEmpty()) {
            require(entries.size <= 1) { "Cannot initialise a single value palette with more than 1 value!" }
            value = entries.first()
        }
    }

    override fun get(value: T): Int {
        if (this.value != null && this.value != value) return resizer.onResize(1, value)
        this.value = value
        return 0
    }

    override fun get(id: Int): T {
        check(value != null && id == 0) { "Missing palette entry for ID $id!" }
        return value!!
    }

    override fun write(buf: ByteBuf) {
        checkNotNull(value) { "Attempted to use an uninitialised palette!" }
        buf.writeVarInt(registry.idOf(value!!))
    }

    object Factory : Palette.Factory {

        override fun <T> create(
            bits: Int,
            registry: IntBiMap<T>,
            resizer: PaletteResizer<T>,
            entries: List<T>
        ): Palette<T> = SingleValuePalette(registry, resizer, entries)
    }
}
