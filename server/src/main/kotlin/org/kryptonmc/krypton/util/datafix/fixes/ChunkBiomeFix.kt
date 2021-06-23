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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import java.util.Arrays

class ChunkBiomeFix(outputSchema: Schema, changesType: Boolean) : AbstractChunkFix("ChunkBiomeFix", outputSchema, changesType) {

    override fun fix(level: Typed<*>): Typed<*> = level.update(remainderFinder()) {
        val biomes = it["Biomes"].asIntStreamOpt().result()
        if (!biomes.isPresent) return@update it
        val oldBiomeArray = biomes.get().toArray()
        val newBiomesArray = IntArray(1024)
        for (x in 0..3) {
            for (z in 0..3) {
                val packedZ = (z shl 2) + 2
                val packedX = (x shl 2) + 2
                val packed = packedX shl 4 or packedZ
                newBiomesArray[x shl 2 or z] = oldBiomeArray.getOrNull(packed) ?: -1
            }
        }
        for (i in 1..63) System.arraycopy(newBiomesArray, 0, newBiomesArray, i * 16, 16)
        it.set("Biomes", it.createIntList(Arrays.stream(newBiomesArray)))
    }
}
