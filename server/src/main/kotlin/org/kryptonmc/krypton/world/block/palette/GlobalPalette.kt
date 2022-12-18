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
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.IntBiMap

class GlobalPalette<T>(private val registry: IntBiMap<T>) : Palette<T> {

    override val size: Int
        get() = registry.size

    override fun get(value: T): Int {
        val id = registry.getId(value)
        return if (id == -1) 0 else id
    }

    override fun get(id: Int): T = registry.get(id) ?: throw MissingPaletteEntryException(id)

    override fun write(buf: ByteBuf) {
        // The global palette has nothing to write because the client assumes that
        // we are using the global palette if we send the right bits per entry
    }

    override fun calculateSerializedSize(): Int = ByteBufExtras.getVarIntBytes(0)

    override fun copy(): Palette<T> = this

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> = GlobalPalette(registry)
    }
}
