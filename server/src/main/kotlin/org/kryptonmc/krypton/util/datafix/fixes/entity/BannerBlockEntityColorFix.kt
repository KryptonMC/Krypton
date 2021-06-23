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
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class BannerBlockEntityColorFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "BannerBlockEntityColorFix", References.BLOCK_ENTITY, "minecraft:banner") {

    override fun fix(typed: Typed<*>): Typed<*> = typed.update(remainderFinder()) { it.fixTag() }

    private fun Dynamic<*>.fixTag(): Dynamic<*> {
        val temp = update("Base") { it.createInt(15 - it.asInt(0)) }
        return temp.update("Patterns") { patterns ->
            val updated = patterns.asStreamOpt().map { pattern -> pattern.map { it.update("Color") { color -> color.createInt(15 - color.asInt(0)) } } }
            updated.map(patterns::createList).result().orElse(patterns)
        }
    }
}
