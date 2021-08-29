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
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References
import java.util.Arrays

class ProjectileOwnerFix(outputSchema: Schema) : DataFix(outputSchema, false) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("ProjectileOwnerFix", inputSchema.getType(References.ENTITY)) { it.updateProjectiles() }

    private fun Typed<*>.updateProjectiles(): Typed<*> {
        var temp = updateEntity("minecraft:egg") { it.updateThrowableOwner() }
        temp = temp.updateEntity("minecraft:ender_pearl") { it.updateThrowableOwner() }
        temp = temp.updateEntity("minecraft:experience_bottle") { it.updateThrowableOwner() }
        temp = temp.updateEntity("minecraft:snowball") { it.updateThrowableOwner() }
        temp = temp.updateEntity("minecraft:potion") { it.updateThrowableOwner() }
        temp = temp.updateEntity("minecraft:potion") { it.updatePotion() }
        temp = temp.updateEntity("minecraft:llama_spit") { it.updateLlamaSpitOwner() }
        temp = temp.updateEntity("minecraft:arrow") { it.updateArrowOwner() }
        temp = temp.updateEntity("minecraft:spectral_arrow") { it.updateArrowOwner() }
        return temp.updateEntity("minecraft:trident") { it.updateArrowOwner() }
    }

    private fun Dynamic<*>.updateArrowOwner(): Dynamic<*> {
        val most = get("OwnerUUIDMost").asLong(0L)
        val least = get("OwnerUUIDLeast").asLong(0L)
        return setUUID(most, least).remove("OwnerUUIDMost").remove("OwnerUUIDLeast")
    }

    private fun Dynamic<*>.updateLlamaSpitOwner(): Dynamic<*> {
        val owner = get("Owner")
        val most = owner["OwnerUUIDMost"].asLong(0L)
        val least = owner["OwnerUUIDLeast"].asLong(0L)
        return setUUID(most, least).remove("Owner")
    }

    private fun Dynamic<*>.updatePotion() = set("Item", get("Potion").orElseEmptyMap()).remove("Potion")

    private fun Dynamic<*>.updateThrowableOwner(): Dynamic<*> {
        val owner = get("owner")
        val most = owner["M"].asLong(0L)
        val least = owner["L"].asLong(0L)
        return setUUID(most, least)
    }

    private fun Dynamic<*>.setUUID(most: Long, least: Long) = if (most != 0L && least != 0L) set("OwnerUUID", createIntList(Arrays.stream(createUUID(most, least)))) else this

    private fun Typed<*>.updateEntity(name: String, updater: (Dynamic<*>) -> Dynamic<*>): Typed<*> {
        val inputType = inputSchema.getChoiceType(References.ENTITY, name)
        val outputType = outputSchema.getChoiceType(References.ENTITY, name)
        return updateTyped(namedChoice(name, inputType), outputType) { it.update(remainderFinder(), updater) }
    }

    companion object {

        private fun createUUID(most: Long, least: Long) = intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt())
    }
}
