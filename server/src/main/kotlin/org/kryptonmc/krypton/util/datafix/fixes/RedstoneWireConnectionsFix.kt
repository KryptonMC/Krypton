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
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class RedstoneWireConnectionsFix(outputSchema: Schema) : DataFix(outputSchema, false) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("RedstoneConnectionsFix", inputSchema.getType(References.BLOCK_STATE)) { typed -> typed.update(remainderFinder()) { it.updateConnections() } }

    private fun <T> Dynamic<T>.updateConnections() = run {
        val hasName = get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent
        if (!hasName) this else update("Properties") { properties ->
            val east = properties["east"].asString("none")
            val west = properties["west"].asString("none")
            val north = properties["north"].asString("none")
            val south = properties["south"].asString("none")
            val isEastOrWest = east.isConnected() || west.isConnected()
            val isNorthOrSouth = north.isConnected() || south.isConnected()
            val newEast = if (!east.isConnected() && !isNorthOrSouth) "side" else east
            val newWest = if (!west.isConnected() && !isNorthOrSouth) "side" else west
            val newNorth = if (!north.isConnected() && !isEastOrWest) "side" else north
            val newSouth = if (!south.isConnected() && !isEastOrWest) "side" else south
            properties.update("east") { it.createString(newEast) }
                .update("west") { it.createString(newWest) }
                .update("north") { it.createString(newNorth) }
                .update("south") { it.createString(newSouth) }
        }
    }

    companion object {

        private fun String.isConnected() = this != "none"
    }
}
