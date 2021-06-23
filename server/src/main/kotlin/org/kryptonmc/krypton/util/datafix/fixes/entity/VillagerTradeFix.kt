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

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.OpticFinder
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.List.ListType
import com.mojang.datafixers.util.Pair
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class VillagerTradeFix(outputSchema: Schema, changesType: Boolean) : NamedEntityFix(outputSchema, changesType, "Villager trade fix", References.ENTITY, "minecraft:villager") {

    override fun fix(typed: Typed<*>): Typed<*> {
        val offersFinder = typed.type.findField("Offers")
        val recipesFinder = offersFinder.type().findField("Recipes")
        val recipesType = recipesFinder.type()
        check(recipesType is ListType<*>) { "Recipes type was not a list!" }
        val recipeType = recipesType.element
        val recipeFinder = recipeType.finder()
        val buyFinder = recipeType.findField("buy")
        val buyBFinder = recipeType.findField("buyB")
        val sellFinder = recipeType.findField("sell")
        val idFinder = fieldFinder("id", named(References.ITEM_NAME.typeName(), NamespacedSchema.NAMESPACED_STRING))
        val updater: (Typed<*>) -> Typed<*> = { it.updateItemStack(idFinder) }
        return typed.updateTyped(offersFinder) { offers ->
            offers.updateTyped(recipesFinder) { recipes ->
                recipes.updateTyped(recipeFinder) { recipe -> recipe.updateTyped(buyFinder, updater).updateTyped(buyBFinder, updater).updateTyped(sellFinder, updater) }
            }
        }
    }

    private fun Typed<*>.updateItemStack(finder: OpticFinder<Pair<String, String>>) = update(finder) { pair ->
        pair.mapSecond { if (it == "minecraft:carved_pumpkin") "minecraft:pumpkin" else it }
    }
}
