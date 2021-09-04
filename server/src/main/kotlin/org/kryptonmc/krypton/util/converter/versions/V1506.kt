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
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.types.MapType
import com.google.common.base.Splitter
import com.google.gson.JsonObject
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.json.JsonMapType
import org.kryptonmc.krypton.util.converter.types.json.JsonTypeUtil
import org.kryptonmc.krypton.util.converter.types.nbt.NBTMapType
import org.kryptonmc.krypton.util.transformTo
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import java.io.StringReader

object V1506 {

    private const val VERSION = MCVersions.V1_13_PRE4 + 2
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

    fun register() = MCTypeRegistry.LEVEL.addStructureConverter(VERSION) { data, _, _ ->
        val generatorOptions = data.getString("generatorOptions")
        val generatorName = data.getString("generatorName")
        if (generatorName.equals("flat", true)) {
            data.setMap("generatorOptions", convert(generatorOptions ?: ""))
        } else if (generatorName.equals("buffet", true) && generatorOptions != null) {
            val json = Streams.parse(JsonReader(StringReader(generatorName!!)).apply { isLenient = true }) as JsonObject
            data.setMap("generatorOptions", JsonTypeUtil.convertJsonToNBT(JsonMapType(json, false)))
        }
        null
    }

    private fun convert(generatorSettings: String): MapType<String> {
        val splitSettings = Splitter.on(';').split(generatorSettings).iterator()
        var biome = "minecraft:plains"
        val structures = HashMap<String, MutableMap<String, String>>()
        val layers: MutableList<Pair<Int, String>>
        if (generatorSettings.isNotEmpty() && splitSettings.hasNext()) {
            layers = getLayersInfoFromString(splitSettings.next())
            if (layers.isNotEmpty()) {
                // biome is next
                if (splitSettings.hasNext()) biome = MAP.getOrDefault(splitSettings.next(), "minecraft:plains")

                // structures is next
                if (splitSettings.hasNext()) splitSettings.next().lowercase().split(",").forEach { structureString ->
                    val structureInfo = structureString.split("\\(".toRegex(), 2)
                    if (structureInfo[0].isEmpty()) return@forEach
                    structures[structureInfo[0]] = mutableMapOf()
                    if (structureInfo.size <= 1 || !structureInfo[1].endsWith(')') || structureInfo[1].length <= 1) return@forEach
                    structureInfo[1].substring(0, structureInfo[1].length - 1).split(" ").forEach {
                        val var9 = it.split("=".toRegex(), 2)
                        if (var9.size == 2) structures[structureInfo[0]]?.put(var9[0], var9[1])
                    }
                } else {
                    structures["village"] = mutableMapOf()
                }
            }
        } else {
            layers = ArrayList()
            layers.add(Pair(1, "minecraft:bedrock"))
            layers.add(Pair(2, "minecraft:dirt"))
            layers.add(Pair(1, "minecraft:grass_block"))
            structures["village"] = mutableMapOf()
        }

        val layerTag = MutableListTag(layers.mapTo(mutableListOf()) {
            MutableCompoundTag(mutableMapOf(
                "height" to IntTag.of(it.first),
                "block" to StringTag.of(it.second)
            ))
        })
        val structuresTag = MutableCompoundTag(structures.entries.associateTo(mutableMapOf()) { structure ->
            structure.key.lowercase() to MutableCompoundTag(structure.value.transformTo(mutableMapOf()) { it.key to StringTag.of(it.value) })
        })
        return NBTMapType(MutableCompoundTag(mutableMapOf(
            "layers" to layerTag,
            "biome" to StringTag.of(biome),
            "structures" to structuresTag
        )))
    }

    private fun getLayersInfoFromString(layersString: String): MutableList<Pair<Int, String>> {
        val ret = ArrayList<Pair<Int, String>>()
        layersString.split(",").forEach {
            val layer = getLayerInfoFromString(it) ?: return mutableListOf()
            ret.add(layer)
        }
        return ret
    }

    private fun getLayerInfoFromString(layerString: String): Pair<Int, String>? {
        val split = layerString.split("\\*".toRegex(), 2)
        val layerCount = if (split.size == 2) split[0].toIntOrNull() ?: return null else 1
        val blockName = split.last()
        return Pair(layerCount, blockName)
    }
}
