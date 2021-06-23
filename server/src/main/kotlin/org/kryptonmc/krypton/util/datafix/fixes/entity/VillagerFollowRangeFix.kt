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
import org.kryptonmc.krypton.util.datafix.References

class VillagerFollowRangeFix(outputSchema: Schema) : NamedEntityFix(outputSchema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager") {

    override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) { villager ->
        villager.update("Attributes") { attributes ->
            villager.createList(attributes.asStream().map {
                if (it["Name"].asString("") == "generic.follow_range" && it["Base"].asDouble(0.0) == ORIGINAL_VALUE) {
                    it.set("Base", it.createDouble(NEW_BASE_VALUE))
                } else {
                    it
                }
            })
        }
    }

    companion object {

        private const val ORIGINAL_VALUE = 16.0
        private const val NEW_BASE_VALUE = 48.0
    }
}
