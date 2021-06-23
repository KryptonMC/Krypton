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
package org.kryptonmc.krypton.util.datafix.fixes.name

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class RenameHeightmapsFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val type = inputSchema.getType(References.CHUNK)
        val levelFinder = type.findField("Level")
        return fixTypeEverywhereTyped("RenameHeightmapsFix", type) { typed -> typed.updateTyped(levelFinder) { level -> level.update(remainderFinder()) { it.fix() } } }
    }

    private fun Dynamic<*>.fix(): Dynamic<*> {
        val optionalHeightmaps = get("Heightmaps").result()
        if (optionalHeightmaps.isEmpty) return this
        var heightmaps = optionalHeightmaps.get()
        val liquid = heightmaps["LIQUID"].result()
        if (liquid.isPresent) {
            heightmaps = heightmaps.remove("LIQUID")
            heightmaps = heightmaps.set("WORLD_SURFACE_WG", liquid.get())
        }
        val solid = heightmaps["SOLID"].result()
        if (solid.isPresent) {
            heightmaps = heightmaps.remove("SOLID")
            heightmaps = heightmaps.set("OCEAN_FLOOR_WG", solid.get())
            heightmaps = heightmaps.set("OCEAN_FLOOR", solid.get())
        }
        val light = heightmaps["LIGHT"].result()
        if (light.isPresent) {
            heightmaps = heightmaps.remove("LIGHT")
            heightmaps = heightmaps.set("LIGHT_BLOCKING", light.get())
        }
        val rain = heightmaps["RAIN"].result()
        if (rain.isPresent) {
            heightmaps = heightmaps.remove("RAIN")
            heightmaps = heightmaps.set("MOTION_BLOCKING", rain.get())
            heightmaps = heightmaps.set("MOTION_BLOCKING_NO_LEAVES", rain.get())
        }
        return set("Heightmaps", heightmaps)
    }
}
