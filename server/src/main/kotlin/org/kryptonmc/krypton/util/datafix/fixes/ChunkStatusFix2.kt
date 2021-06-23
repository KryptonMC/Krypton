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

class ChunkStatusFix2(outputSchema: Schema, changesType: Boolean) : AbstractChunkFix("ChunkStatusFix2", outputSchema, changesType) {

    override fun fix(level: Typed<*>): Typed<*> {
        val data = level[remainderFinder()]
        val status = data["Status"].asString("empty")
        val rewrittenStatus = RENAMES_AND_DOWNGRADES.getOrDefault(status, "empty")
        return if (status == rewrittenStatus) level else level.set(remainderFinder(), data.set("Status", data.createString(rewrittenStatus)))
    }

    companion object {

        private val RENAMES_AND_DOWNGRADES = mapOf(
            "structure_references" to "empty",
            "biomes" to "empty",
            "base" to "surface",
            "carved" to "carvers",
            "liquid_carved" to "liquid_carvers",
            "decorated" to "features",
            "lighted" to "light",
            "mobs_spawned" to "spawn",
            "finalized" to "heightmaps",
            "fullchunk" to "full"
        )
    }
}
