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
package org.kryptonmc.krypton.util.datafix.fixes.uuid

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.namedChoice
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import java.util.Optional
import java.util.UUID
import java.util.stream.IntStream

sealed class UUIDFix(outputSchema: Schema, protected val typeReference: DSL.TypeReference) : DataFix(outputSchema, false) {

    protected fun Typed<*>.updateNamedChoice(name: String, updater: (Dynamic<*>) -> Dynamic<*>): Typed<*> {
        val oldType = inputSchema.getChoiceType(typeReference, name)
        val newType = outputSchema.getChoiceType(typeReference, name)
        return updateTyped(namedChoice(name, oldType), newType) { it.update(remainderFinder(), updater) }
    }
}

fun Dynamic<*>.replaceUUIDString(old: String, new: String) = createUUIDFromString(old).map { remove(old).set(new, it) }

fun Dynamic<*>.replaceUUIDMLTag(old: String, new: String): Optional<Dynamic<*>> =
    get(old).result().flatMap { it.createUUIDFromML() }.map { remove(old).set(new, it) }

fun Dynamic<*>.replaceUUIDLeastMost(mostPrefix: String, leastPrefix: String): Optional<Dynamic<*>> {
    val mostName = "${mostPrefix}Most"
    val leastName = "${leastPrefix}Least"
    return createUUIDFromLongs(mostName, leastName).map { remove(mostName).remove(leastName).set(leastPrefix, it) }
}

fun Dynamic<*>.createUUIDFromString(string: String): Optional<Dynamic<*>> = get(string).result().flatMap {
    val asString = it.asString(null)
    if (asString != null) {
        try {
            val uuid = UUID.fromString(asString)
            createUUIDTag(uuid.mostSignificantBits, uuid.leastSignificantBits)
        } catch (exception: IllegalArgumentException) {
            Optional.empty()
        }
    } else {
        Optional.empty()
    }
}

fun Dynamic<*>.createUUIDFromML() = createUUIDFromLongs("M", "L")

fun Dynamic<*>.createUUIDFromLongs(mostName: String, leastName: String): Optional<Dynamic<*>> {
    val most = get(mostName).asLong(0L)
    val least = get(leastName).asLong(0)
    return if (most != 0L && least != 0L) createUUIDTag(most, least) else Optional.empty()
}

fun Dynamic<*>.createUUIDTag(mostSignificant: Long, leastSignificant: Long) = Optional.of(createIntList(IntStream.of(
    (mostSignificant shr 32).toInt(),
    mostSignificant.toInt(),
    (leastSignificant shr 32).toInt(),
    leastSignificant.toInt()
)))
