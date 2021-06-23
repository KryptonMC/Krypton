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
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.References

class VillagerDataFix(outputSchema: Schema, name: String) : NamedEntityFix(outputSchema, false, "Villager profession data fix ($name)", References.ENTITY, name) {

    override fun fix(typed: Typed<*>): Typed<*> = with(typed[remainderFinder()]) {
        typed.set(remainderFinder(), remove("Profession").remove("Career").remove("CareerLevel").set("VillagerData", createMap(mapOf(
            createString("type") to createString("minecraft:plains"),
            createString("profession") to createString(upgradeData(get("Profession").asInt(0), get("Career").asInt(0))),
            createString("level") to DataFixUtils.orElse(get("CareerLevel").result(), createInt(1))
        ))))
    }

    private fun upgradeData(profession: Int, career: Int) = when (profession) {
        0 -> when (career) {
            2 -> "minecraft:fisherman"
            3 -> "minecraft:shepherd"
            4 -> "minecraft:fletcher"
            else -> "minecraft:farmer"
        }
        1 -> if (career == 2) "minecraft:cartographer" else "minecraft:librarian"
        2 -> "minecraft:cleric"
        3 -> when (career) {
            2 -> "minecraft:weaponsmith"
            3 -> "minecraft:toolsmith"
            else -> "minecraft:armorer"
        }
        4 -> if (career == 2) "minecraft:leatherworker" else "minecraft:butcher"
        5 -> "minecraft:nitwit"
        else -> "minecraft:none"
    }
}
