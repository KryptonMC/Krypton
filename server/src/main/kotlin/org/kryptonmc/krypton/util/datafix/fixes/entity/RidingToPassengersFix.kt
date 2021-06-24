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

import com.mojang.datafixers.DSL.and
import com.mojang.datafixers.DSL.field
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.optional
import com.mojang.datafixers.DSL.unit
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import org.kryptonmc.krypton.util.datafix.References
import java.util.Optional
import java.util.function.Function

class RidingToPassengersFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val oldTreeType = inputSchema.getTypeRaw(References.ENTITY_TREE)
        val newTreeType = inputSchema.getTypeRaw(References.ENTITY_TREE)
        val entityType = inputSchema.getTypeRaw(References.ENTITY)
        return cap(oldTreeType, newTreeType, entityType)
    }

    @Suppress("UNREACHABLE_CODE") // See note below
    private fun <O, N, E> cap(ridingType: Type<O>, passengerType: Type<N>, entityType: Type<E>): TypeRewriteRule {
        val oldType: Type<Pair<String, Pair<Either<O, Unit>, E>>> = named(References.ENTITY_TREE.typeName(), and(optional(field("Riding", ridingType)), entityType))
        val newType: Type<Pair<String, Pair<Either<List<N>, Unit>, E>>> = named(References.ENTITY_TREE.typeName(), and(optional(field("Passengers", list(passengerType))), entityType))
        val inputTreeType = inputSchema.getType(References.ENTITY_TREE)
        val outputTreeType = outputSchema.getType(References.ENTITY_TREE)
        check(inputTreeType == oldType) { "Old entity type is not what was expected! Expected $oldType, got $inputTreeType!" }
        check(outputTreeType.equals(newType, true, true)) { "New entity type is not what was expected! Expected $newType, got $outputTreeType" }
        val oldTypeFinder = oldType.finder()
        val newTypeFinder = newType.finder()
        return TypeRewriteRule.seq(
            fixTypeEverywhere("RidingToPassengersFix", oldType, newType) { ops ->
                Function { pair ->
                    var optional = Optional.empty<Pair<String, Pair<Either<List<N>, Unit>, E>>>()
                    var tempPair = pair
                    while (true) {
                        val either = DataFixUtils.orElse(optional.map {
                            val pointed = passengerType.pointTyped(ops).orElseThrow { IllegalStateException("Could not create new entity tree!") }
                            val tree = pointed.set(newTypeFinder, it).getOptional(passengerType.finder()).orElseThrow { IllegalStateException("An entity tree was required and was not present!") }
                            Either.left(listOf(tree))
                        }, Either.right<List<N>, Unit>(unit()))
                        optional = Optional.of(Pair.of(References.ENTITY_TREE.typeName(), Pair.of(either, tempPair.second.second)))
                        val oldTree = tempPair.second.first.left()
                        if (oldTree.isEmpty) return@Function optional.orElseThrow { IllegalStateException("An entity tree was required and was not present!") }
                        tempPair = Typed(ridingType, ops, oldTree.get()).getOptional(oldTypeFinder).orElseThrow { IllegalStateException("An entity tree was required and was not present!") }
                    }
                    // We will never get here anyway, since the loop never breaks, it only returns, but the compiler isn't smart
                    // enough to figure that out apparently, but it knows that this code is unreachable :thonking:
                    optional.get()
                }
            },
            writeAndRead("player RootVehicle injector", inputSchema.getType(References.PLAYER), outputSchema.getType(References.PLAYER))
        )
    }
}
