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
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References

class SplitHorseFix(outputSchema: Schema, changesType: Boolean) : SplitEntityFix("SplitHorseFix", outputSchema, changesType) {

    override fun fix(entityName: String, typed: Typed<*>): Pair<String, Typed<*>> {
        val data = typed[remainderFinder()]
        return if (entityName == "EntityHorse") {
            val name = when (data["Type"].asInt(0)) {
                0 -> "Horse"
                1 -> "Donkey"
                2 -> "Mule"
                3 -> "ZombieHorse"
                4 -> "SkeletonHorse"
                else -> "Horse"
            }
            data.remove("Type")
            val horseType = outputSchema.findChoiceType(References.ENTITY).types()[name]!!
            Pair.of(name, typed.write().flatMap { horseType.readTyped(it) }.result().orElseThrow { IllegalStateException("Could not parse the newly created horse data!") }.first)
        } else {
            Pair.of(entityName, typed)
        }
    }
}
