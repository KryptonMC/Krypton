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

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import me.bardy.gsonkt.parseToJson
import org.kryptonmc.krypton.util.datafix.References
import java.util.stream.Collectors

class GeneratorOptionsFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val levelType = outputSchema.getType(References.LEVEL)
        return fixTypeEverywhereTyped("GeneratorOptionsFix", inputSchema.getType(References.LEVEL), levelType) { typed ->
            typed.write().flatMap {
                val options = it[GENERATOR_OPTIONS].asString().result()
                val data = when (it["generatorName"].asString("").lowercase()) {
                    "flat" -> it.set(GENERATOR_OPTIONS, options.orElse("").convert(it.ops))
                    "buffet" -> if (options.isPresent) {
                        val dynamic = Dynamic(JsonOps.INSTANCE, options.get().parseToJson())
                        it.set("generatorOptions", dynamic.convert(it.ops))
                    } else it
                    else -> it
                }
                levelType.readTyped(data)
            }.map { it.first }.result().orElseThrow { IllegalStateException("Could not read new level type!") }
        }
    }

    companion object {

        private const val GENERATOR_OPTIONS = "generatorOptions"
        private val MAP = mapOf(
            "0" to "minecraft:ocean",
            "1" to "minecraft:plains",
            "2" to "minecraft:desert",
            "3" to "minecraft:mountains",
            "4" to "minecraft:forest",
            "5" to "minecraft:taiga",
            "6" to "minecraft:swamp",
            "7" to "minecraft:river",
            "8" to "minecraft:nether",
            "9" to "minecraft:the_end",
            "10" to "minecraft:frozen_ocean",
            "11" to "minecraft:frozen_river",
            "12" to "minecraft:snowy_tundra",
            "13" to "minecraft:snowy_mountains",
            "14" to "minecraft:mushroom_fields",
            "15" to "minecraft:mushroom_field_shore",
            "16" to "minecraft:beach",
            "17" to "minecraft:desert_hills",
            "18" to "minecraft:wooded_hills",
            "19" to "minecraft:taiga_hills",
            "20" to "minecraft:mountain_edge",
            "21" to "minecraft:jungle",
            "22" to "minecraft:jungle_hills",
            "23" to "minecraft:jungle_edge",
            "24" to "minecraft:deep_ocean",
            "25" to "minecraft:stone_shore",
            "26" to "minecraft:snowy_beach",
            "27" to "minecraft:birch_forest",
            "28" to "minecraft:birch_forest_hills",
            "29" to "minecraft:dark_forest",
            "30" to "minecraft:snowy_taiga",
            "31" to "minecraft:snowy_taiga_hills",
            "32" to "minecraft:giant_tree_taiga",
            "33" to "minecraft:giant_tree_taiga_hills",
            "34" to "minecraft:wooded_mountains",
            "35" to "minecraft:savanna",
            "36" to "minecraft:savanna_plateau",
            "37" to "minecraft:badlands",
            "38" to "minecraft:wooded_badlands_plateau",
            "39" to "minecraft:badlands_plateau",
            "40" to "minecraft:small_end_islands",
            "41" to "minecraft:end_midlands",
            "42" to "minecraft:end_highlands",
            "43" to "minecraft:end_barrens",
            "44" to "minecraft:warm_ocean",
            "45" to "minecraft:lukewarm_ocean",
            "46" to "minecraft:cold_ocean",
            "47" to "minecraft:deep_warm_ocean",
            "48" to "minecraft:deep_lukewarm_ocean",
            "49" to "minecraft:deep_cold_ocean",
            "50" to "minecraft:deep_frozen_ocean",
            "127" to "minecraft:the_void",
            "129" to "minecraft:sunflower_plains",
            "130" to "minecraft:desert_lakes",
            "131" to "minecraft:gravelly_mountains",
            "132" to "minecraft:flower_forest",
            "133" to "minecraft:taiga_mountains",
            "134" to "minecraft:swamp_hills",
            "140" to "minecraft:ice_spikes",
            "149" to "minecraft:modified_jungle",
            "151" to "minecraft:modified_jungle_edge",
            "155" to "minecraft:tall_birch_forest",
            "156" to "minecraft:tall_birch_hills",
            "157" to "minecraft:dark_forest_hills",
            "158" to "minecraft:snowy_taiga_mountains",
            "160" to "minecraft:giant_spruce_taiga",
            "161" to "minecraft:giant_spruce_taiga_hills",
            "162" to "minecraft:modified_gravelly_mountains",
            "163" to "minecraft:shattered_savanna",
            "164" to "minecraft:shattered_savanna_plateau",
            "165" to "minecraft:eroded_badlands",
            "166" to "minecraft:modified_wooded_badlands_plateau",
            "167" to "minecraft:modified_badlands_plateau"
        )

        private fun <T> String.convert(ops: DynamicOps<T>): Dynamic<T> {
            val split = split(";").iterator()
            var biome = "minecraft:plains"
            val structures = mutableMapOf<String, MutableMap<String, String>>()
            val layers = if (isNotEmpty() && split.hasNext()) {
                val value = split.next().toLayersInfo()
                if (value.isNotEmpty()) {
                    if (split.hasNext()) biome = MAP.getOrDefault(split.next(), "minecraft:plains")
                    if (split.hasNext()) split.next().lowercase().split(",").forEach { next ->
                        val subSplit = next.split("(", limit = 2)
                        if (subSplit[0].isEmpty()) return@forEach
                        structures[subSplit[0]] = mutableMapOf()
                        if (subSplit.size <= 1 || !subSplit[1].endsWith(")") || subSplit[1].length <= 1) return@forEach
                        subSplit[1].substring(0, subSplit[0].lastIndex).split(" ").forEach {
                            val equalsSplit = it.split("=", limit = 2)
                            if (equalsSplit.size == 2) structures[subSplit[0]]?.put(equalsSplit[0], equalsSplit[1])
                        }
                    } else {
                        structures["village"] = mutableMapOf()
                    }
                }
                value
            } else mutableListOf<Pair<Int, String>>().apply {
                add(Pair.of(1, "minecraft:bedrock"))
                add(Pair.of(2, "minecraft:dirt"))
                add(Pair.of(1, "minecraft:grass_block"))
                structures["village"] = mutableMapOf()
            }
            val layerList = ops.createList(layers.stream().map { ops.createMap(mapOf(
                ops.createString("height") to ops.createInt(it.first),
                ops.createString("block") to ops.createString(it.second)
            )) })
            val structureMap = ops.createMap(structures.entries.stream()
                .map { map -> Pair.of(ops.createString(map.key.lowercase()), ops.createMap(map.value.entries.stream()
                    .map { Pair.of(ops.createString(it.key), ops.createString(it.value)) }
                    .collect(Collectors.toMap(Pair<T, T>::getFirst, Pair<T, T>::getSecond)))) }
                .collect(Collectors.toMap(Pair<T, T>::getFirst, Pair<T, T>::getSecond)))
            return Dynamic(ops, ops.createMap(mapOf(
                ops.createString("layers") to layerList,
                ops.createString("biome") to ops.createString(biome),
                ops.createString("structures") to structureMap
            )))
        }

        private fun String.toLayerInfo(): Pair<Int, String>? {
            val split = split("*", limit = 2)
            val first = if (split.size == 2) split[0].toIntOrNull() ?: return null else 1
            val string = split.last()
            return Pair.of(first, string)
        }

        private fun String.toLayersInfo(): List<Pair<Int, String>> = split(",").map {
            val layerInfo = it.toLayerInfo() ?: return emptyList()
            layerInfo
        }
    }
}
