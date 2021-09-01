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
package org.kryptonmc.krypton.util.converter.versions

import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V2202 {

    private const val VERSION = MCVersions.V19W35A + 1

    fun register() = MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
        val level = data.getMap<String>("Level") ?: return@addStructureConverter null
        val oldBiomes = level.getInts("Biomes") ?: return@addStructureConverter null

        val newBiomes = IntArray(1024)
        level.setInts("Biomes", newBiomes)

        for (i in 0..3) {
            for (j in 0..3) {
                val k = (j shl 2) + 2
                val l = (i shl 2) + 2
                val m = l shl 4 or k
                newBiomes[i shl 2 or j] = if (m < oldBiomes.size) oldBiomes[m] else -1
            }
        }

        for (i in 1..63) {
            System.arraycopy(newBiomes, 0, newBiomes, i * 16, 16)
        }
        null
    }
}
