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
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optional
import com.mojang.datafixers.DSL.or
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DSL.string
import com.mojang.datafixers.DSL.taggedChoiceType
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.FieldFinder
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.Type
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.WorldGenSettingsFix.Companion.defaultOverworld
import org.kryptonmc.krypton.util.datafix.fixes.WorldGenSettingsFix.Companion.vanillaLevels
import org.kryptonmc.krypton.util.datafix.schema.NamespacedSchema

class MissingDimensionFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    @Suppress("UNCHECKED_CAST") // Nothing we can do here, these are fine
    override fun makeRule(): TypeRewriteRule {
        val generatorType = TaggedChoiceType("type", string(), mapOf(
            "minecraft:debug" to remainderType(),
            "minecraft:flat" to optionalFields("settings" to optionalFields(
                "biome" to inputSchema.getType(References.BIOME) as Type<Any>,
                "layers" to list(optionalFields("block" to inputSchema.getType(References.BLOCK_NAME) as Type<Any>))
            )),
            "minecraft:noise" to optionalFields(
                "biome_source" to taggedChoiceType("type", string(), mapOf(
                    "minecraft:fixed" to fields("biome" to inputSchema.getType(References.BIOME) as Type<Any>),
                    "minecraft:multi_noise" to list(fields("biome" to inputSchema.getType(References.BIOME) as Type<Any>)),
                    "minecraft:checkerboard" to fields("biomes" to list(inputSchema.getType(References.BIOME) as Type<Any>)),
                    "minecraft:vanilla_layered" to remainderType(),
                    "minecraft:the_end" to remainderType()
                )),
                "settings" to or(string(), optionalFields(
                    "default_block" to inputSchema.getType(References.BLOCK_NAME) as Type<Any>,
                    "default_fluid" to inputSchema.getType(References.BLOCK_NAME) as Type<Any>
                ))
            )
        ))
        val dimensionMapType = compoundList(NamespacedSchema.NAMESPACED_STRING, fields("generator" to generatorType))
        val dimensionsType = and(dimensionMapType, remainderType())
        val worldGenSettingsType = inputSchema.getType(References.WORLD_GEN_SETTINGS)
        val dimensionsFinder = FieldFinder("dimensions", dimensionsType)
        check(worldGenSettingsType.findFieldType("dimensions") == dimensionsType)
        return fixTypeEverywhereTyped("MissingDimensionFix", worldGenSettingsType) { typed ->
            typed.updateTyped(dimensionsFinder) { dimensions ->
                dimensions.updateTyped(dimensionMapType.finder()) dimensions@{ map ->
                    val value = map.value
                    check(value is List<*>) { "Expected list, got ${map.value::class.java.simpleName}!" }
                    if (value.isNotEmpty()) return@dimensions map
                    val settings = typed[remainderFinder()].recreateSettings()
                    DataFixUtils.orElse(dimensionMapType.readTyped(settings).result().map { it.first }, map)
                }
            }
        }
    }

    private fun <T> Dynamic<T>.recreateSettings(): Dynamic<T> {
        val seed = get("seed").asLong(0L)
        return Dynamic(ops, vanillaLevels(seed, defaultOverworld(seed), false))
    }

    companion object {

        private fun <A> fields(pair: Pair<String, Type<A>>) = and(field(pair.first, pair.second), remainderType())

        private fun <A> optionalFields(pair: Pair<String, Type<A>>): Type<com.mojang.datafixers.util.Pair<Either<A, Unit>, Dynamic<*>>> =
            and(optional(field(pair.first, pair.second)), remainderType())

        private fun <A1, A2> optionalFields(
            first: Pair<String, Type<A1>>,
            second: Pair<String, Type<A2>>
        ) = and(optional(field(first.first, first.second)), optional(field(second.first, second.second)), remainderType())
    }
}
