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

import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.world.block.BlockLoader

@JvmRecord
data class PaletteType<T>(
    val registry: IntBiMap<T>,
    val arrayRange: IntRange?,
    val mapRange: IntRange?,
    val singlePalette: (PaletteResizer<T>) -> SinglePalette<T>,
    val arrayPalette: PaletteConstructor<ArrayPalette<T>, T>,
    val mapPalette: PaletteConstructor<MapPalette<T>, T>,
    val globalPalette: GlobalPalette<T>,
    val defaultValue: T,
    val sizeBits: Int
) {

    val size: Int
        get() = 1 shl (sizeBits * 3)

    companion object {

        val BLOCKS = PaletteType(
            BlockLoader.STATES,
            1..4,
            5..8,
            { SinglePalette.Blocks(it) },
            { resizer, bits -> ArrayPalette.Blocks(bits, resizer) },
            { resizer, bits -> MapPalette.Blocks(bits, resizer) },
            GlobalPalette.Blocks,
            Blocks.AIR,
            4
        )
        val BIOMES = PaletteType(
            InternalRegistries.BIOME,
            1..2,
            null,
            { SinglePalette.Biomes(it) },
            { resizer, bits -> ArrayPalette.Biomes(bits, resizer) },
            { resizer, bits -> MapPalette.Biomes(bits, resizer) },
            GlobalPalette.Biomes,
            Biomes.PLAINS,
            2
        )
    }
}
