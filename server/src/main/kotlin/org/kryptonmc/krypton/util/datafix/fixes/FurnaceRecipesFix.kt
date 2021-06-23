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

import com.mojang.datafixers.DSL.and
import com.mojang.datafixers.DSL.compoundList
import com.mojang.datafixers.DSL.field
import com.mojang.datafixers.DSL.intType
import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DSL.optional
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References

class FurnaceRecipesFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule() = cap(outputSchema.getTypeRaw(References.RECIPE))

    private fun <R> cap(type: Type<R>): TypeRewriteRule {
        val usedType = and(optional(field("RecipesUsed", and(compoundList(type, intType()), remainderType()))), remainderType())
        val furnaceType = inputSchema.getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace")
        val blastType = inputSchema.getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace")
        val smokerType = inputSchema.getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker")
        val furnaceFinder = namedChoice("minecraft:furnace", furnaceType)
        val blastFinder = namedChoice("minecraft:blast_furnace", blastType)
        val smokerFinder = namedChoice("minecraft:smoker", smokerType)
        return fixTypeEverywhereTyped("FurnaceRecipesFix", inputSchema.getType(References.BLOCK_ENTITY), outputSchema.getType(References.BLOCK_ENTITY)) { typed ->
            typed.updateTyped(furnaceFinder, furnaceType) { it.updateFurnaceContents(type, usedType) }
                .updateTyped(blastFinder, blastType) { it.updateFurnaceContents(type, usedType) }
                .updateTyped(smokerFinder, smokerType) { it.updateFurnaceContents(type, usedType) }
        }
    }

    private fun <R> Typed<*>.updateFurnaceContents(recipeType: Type<R>, usedType: Type<Pair<Either<Pair<List<Pair<R, Int>>, Dynamic<*>>, Unit>, Dynamic<*>>>): Typed<*> {
        var recipeData = getOrCreate(remainderFinder())
        val recipesUsedSize = recipeData["RecipesUsedSize"].asInt(0)
        recipeData = recipeData.remove("RecipesUsedSize")

        val recipes = mutableListOf<Pair<R, Int>>()
        for (i in 0 until recipesUsedSize) {
            val locationName = "RecipeLocation$i"
            val amountName = "RecipeAmount$i"
            val location = recipeData[locationName].result()
            val amount = recipeData[amountName].asInt(0)
            if (amount > 0) location.ifPresent { dynamic ->
                recipeType.read(dynamic).result().ifPresent { recipes += Pair.of(it.first, amount) }
            }
            recipeData = recipeData.remove(locationName).remove(amountName)
        }

        return set(remainderFinder(), usedType, Pair.of(Either.left(Pair.of(recipes, recipeData.emptyMap())), recipeData))
    }
}
