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
package org.kryptonmc.krypton.util.datafix.fixes.entity

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References
import java.util.Optional

class JigsawRotationFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("Jigsaw rotation fix", inputSchema.getType(References.BLOCK_STATE)) { typed ->
        typed.update(remainderFinder()) { jigsaw ->
            val name = jigsaw["Name"].asString().result()
            if (name == Optional.of("minecraft:jigsaw")) jigsaw.update("Properties") {
                val facing = it["facing"].asString("north")
                it.remove("facing").set("orientation", it.createString(REMAPPED.getOrDefault(facing, facing)))
            } else jigsaw
        }
    }

    companion object {

        private val REMAPPED = mapOf(
            "down" to "down_south",
            "up" to "up_north",
            "north" to "north_up",
            "south" to "south_up",
            "west" to "west_up",
            "east" to "east_up"
        )
    }
}
