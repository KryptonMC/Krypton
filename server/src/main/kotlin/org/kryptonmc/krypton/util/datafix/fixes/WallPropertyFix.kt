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
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class WallPropertyFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("WallPropertyFix", inputSchema.getType(References.BLOCK_STATE)) { wall -> wall.update(remainderFinder()) { it.upgradeBlockStateTag() } }

    companion object {

        private val WALL_BLOCKS = setOf(
            "minecraft:andesite_wall",
            "minecraft:brick_wall",
            "minecraft:cobblestone_wall",
            "minecraft:diorite_wall",
            "minecraft:end_stone_brick_wall",
            "minecraft:granite_wall",
            "minecraft:mossy_cobblestone_wall",
            "minecraft:mossy_stone_brick_wall",
            "minecraft:nether_brick_wall",
            "minecraft:prismarine_wall",
            "minecraft:red_nether_brick_wall",
            "minecraft:red_sandstone_wall",
            "minecraft:sandstone_wall",
            "minecraft:stone_brick_wall"
        )

        @JvmStatic
        private fun <T> Dynamic<T>.upgradeBlockStateTag() = if (get("Name").asString().result().filter(WALL_BLOCKS::contains).isPresent) update("Properties") {
            var temp = it.fixWallProperty("east")
            temp = temp.fixWallProperty("west")
            temp = temp.fixWallProperty("north")
            temp.fixWallProperty("south")
        } else this

        @JvmStatic
        private fun <T> Dynamic<T>.fixWallProperty(name: String) = update(name) { property ->
            DataFixUtils.orElse(property.asString().result().map { if (it == "true") "low" else "none" }.map(property::createString), property)
        }
    }
}

