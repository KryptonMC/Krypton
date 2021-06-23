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

import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.List.ListType
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.entity.RebuildVillagerLevelAndXpFix.Companion.minXpPerLevel

class RebuildVillagerLevelAndXpFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val villagerType = inputSchema.getChoiceType(References.ENTITY, "minecraft:villager")
        val villagerFinder = namedChoice("minecraft:villager", villagerType)
        val offersFinder = villagerType.findField("Offers")
        val recipesFinder = offersFinder.type().findField("Recipes")
        val recipeFinder = (recipesFinder.type() as ListType<*>).element.finder()
        return fixTypeEverywhereTyped("Villager level and xp rebuild", inputSchema.getType(References.ENTITY)) { typed ->
            typed.updateTyped(villagerFinder, villagerType) { villager ->
                val data = villager[remainderFinder()]
                var level = data["VillagerData"]["level"].asInt(0)
                var temp = villager
                if (level == 0 || level == 1) {
                    val recipeCount = villager.getOptionalTyped(offersFinder).flatMap { it.getOptionalTyped(recipeFinder) }.map { it.getAllTyped(recipeFinder).size }.orElse(0)
                    level = (recipeCount / TRADES_PER_LEVEL).clamp(1, 5)
                    if (level > 1) temp = temp.addLevel(level)
                }
                val xp = data["Xp"].asNumber().result()
                if (xp.isEmpty) temp = temp.addXpFromLevel(level)
                temp
            }
        }
    }

    companion object {

        private const val TRADES_PER_LEVEL = 2
        private val LEVEL_XP_THRESHOLDS = intArrayOf(0, 10, 50, 100, 150)

        fun Int.minXpPerLevel() = LEVEL_XP_THRESHOLDS[(this - 1).clamp(0, LEVEL_XP_THRESHOLDS.lastIndex)]
    }
}

private fun Typed<*>.addLevel(level: Int): Typed<*> = update(remainderFinder()) { villager -> villager.update("VillagerData") { it.set("level", it.createInt(level)) } }

private fun Typed<*>.addXpFromLevel(level: Int): Typed<*> = update(remainderFinder()) { it.set("Xp", it.createInt(level.minXpPerLevel())) }
