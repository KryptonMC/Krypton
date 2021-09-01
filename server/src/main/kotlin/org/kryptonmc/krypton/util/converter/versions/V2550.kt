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
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import kotlin.math.max

object V2550 {

    private const val VERSION = MCVersions.V20W20B + 13
    private val DEFAULTS = mapOf(
        "minecraft:village" to StructureFeatureConfig(32, 8, 10387312),
        "minecraft:desert_pyramid" to StructureFeatureConfig(32, 8, 14357617),
        "minecraft:igloo" to StructureFeatureConfig(32, 8, 14357618),
        "minecraft:jungle_pyramid" to StructureFeatureConfig(32, 8, 14357619),
        "minecraft:swamp_hut" to StructureFeatureConfig(32, 8, 14357620),
        "minecraft:pillager_outpost" to StructureFeatureConfig(32, 8, 165745296),
        "minecraft:monument" to StructureFeatureConfig(32, 5, 10387313),
        "minecraft:endcity" to StructureFeatureConfig(20, 11, 10387313),
        "minecraft:mansion" to StructureFeatureConfig(80, 20, 10387319),
    )

    fun register() = MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureConverter(VERSION) { data, _, _ ->
        val seed = data.getLong("RandomSeed")
        val generatorName = data.getString("generatorName")?.lowercase()
        val legacyCustomOptions = data.getString("legacy_custom_options")
            ?: if (generatorName == "customized") data.getString("generatorOptions") else null

        var caves = false
        val generator = if (generatorName == "customized" || generatorName == null) {
            defaultOverworld(seed)
        } else when (generatorName) {
            "flat" -> {
                val generatorOptions = data.getMap<String>("generatorOptions")
                val structures = fixFlatStructures(generatorOptions)
                val settings = NBTTypeUtil.createEmptyMap<String>()

                settings.setMap("structures", structures)
                val layers = generatorOptions?.getList("layers", ObjectType.MAP) ?: NBTTypeUtil.createEmptyList().apply {
                    val heights = intArrayOf(1, 2, 1)
                    val blocks = arrayOf("minecraft:bedrock", "minecraft:dirt", "minecraft:grass_block")
                    for (i in 0..2) {
                        val layer = NBTTypeUtil.createEmptyMap<String>().apply {
                            setInt("height", heights[i])
                            setString("block", blocks[i])
                        }
                        addMap(layer)
                    }
                }

                settings.setList("layers", layers)
                settings.setString("biome", generatorOptions?.getString("biome") ?: "minecraft:plains")
                NBTTypeUtil.createEmptyMap<String>().apply {
                    setString("type", "minecraft:flat")
                    setMap("settings", settings)
                }
            }
            "debug_all_block_states" -> NBTTypeUtil.createEmptyMap<String>().apply { setString("type", "minecraft:debug") }
            "buffet" -> {
                val generatorOptions = data.getMap<String>("generatorOptions")
                val chunkGenerator = generatorOptions?.getMap<String>("chunk_generator")
                val newType = when (chunkGenerator?.getString("type")) {
                    "minecraft:caves" -> {
                        caves = true
                        "minecraft:caves"
                    }
                    "minecraft:floating_islands" -> "minecraft:floating_islands"
                    else -> "minecraft:overworld"
                }

                val biomeSource = generatorOptions?.getMap("biome_source")
                    ?: NBTTypeUtil.createEmptyMap<String>().apply { setString("type", "minecraft:fixed") }
                if (biomeSource.getString("type") == "minecraft:fixed") {
                    val options = biomeSource.getMap<String>("options")
                    val biomes = options?.getList("biomes", ObjectType.STRING)
                    val biome = if (biomes == null || biomes.size() == 0) "minecraft:ocean" else biomes.getString(0)
                    biomeSource.remove("options")
                    biomeSource.setString("biome", biome)
                }
                noise(seed, newType, biomeSource)
            }
            else -> {
                val defaultGen = generatorName == "default"
                val default11Gen = generatorName == "default_1_1" || defaultGen && data.getInt("generatorVersion") == 0
                val amplified = generatorName == "amplified"
                val largeBiomes = generatorName == "largebiomes"
                noise(seed, if (amplified) "minecraft:amplified" else "minecraft:overworld", vanillaBiomeSource(seed, default11Gen, largeBiomes))
            }
        }

        val mapFeatures = data.getBoolean("MapFeatures", true)
        val bonusChest = data.getBoolean("BonusChest", false)

        NBTTypeUtil.createEmptyMap<String>().apply {
            setLong("seed", seed)
            setBoolean("generate_features", mapFeatures)
            setBoolean("bonus_chest", bonusChest)
            setMap("dimensions", vanillaLevels(seed, generator, caves))
            legacyCustomOptions?.let { setString("legacy_custom_options", it) }
        }
    }

    fun noise(seed: Long, worldType: String, biomeSource: MapType<String>) = NBTTypeUtil.createEmptyMap<String>().apply {
        setString("type", "minecraft:noise")
        setMap("biome_source", biomeSource)
        setLong("seed", seed)
        setString("settings", worldType)
    }

    fun vanillaBiomeSource(seed: Long, default11Gen: Boolean, largeBiomes: Boolean) = NBTTypeUtil.createEmptyMap<String>().apply {
        setString("type", "minecraft:vanilla_layered")
        setLong("seed", seed)
        setBoolean("large_biomes", largeBiomes)
        if (default11Gen) setBoolean("legacy_biome_init_layer", default11Gen)
    }

    fun defaultOverworld(seed: Long) = noise(seed, "minecraft:overworld", vanillaBiomeSource(seed, false, false))

    fun fixFlatStructures(generatorOptions: MapType<String>?): MapType<String> {
        var distance = 32
        var spread = 3
        var count = 128
        var stronghold = false
        val newStructures = HashMap<String, StructureFeatureConfig>()
        if (generatorOptions == null) {
            stronghold = true
            newStructures["minecraft:village"] = DEFAULTS["minecraft:village"]!!
        }

        generatorOptions?.getMap<String>("structures")?.let { oldStructures ->
            oldStructures.keys().forEach { structureName ->
                val structureValues = oldStructures.getMap<String>(structureName) ?: return@forEach
                structureValues.keys().forEach {
                    val structureValue = structureValues.getString(it)!!
                    if (structureName == "stronghold") {
                        stronghold = true
                        when (it) {
                            "distance" -> distance = structureValue.toIntOrNull() ?: 1
                            "spread" -> spread = structureValue.toIntOrNull() ?: 1
                            "count" -> count = structureValue.toIntOrNull() ?: 1
                        }
                    } else when (it) {
                        "distance" -> when (structureName) {
                            "village" -> newStructures.setSpacing("minecraft:village", structureValue, 9)
                            "biome_1" -> {
                                newStructures.setSpacing("minecraft:desert_pyramid", structureValue, 9)
                                newStructures.setSpacing("minecraft:igloo", structureValue, 9)
                                newStructures.setSpacing("minecraft:jungle_pyramid", structureValue, 9)
                                newStructures.setSpacing("minecraft:swamp_hut", structureValue, 9)
                                newStructures.setSpacing("minecraft:pillager_outpost", structureValue, 9)
                            }
                            "endcity" -> newStructures.setSpacing("minecraft:endcity", structureValue, 1)
                            "mansion" -> newStructures.setSpacing("minecraft:mansion", structureValue, 1)
                        }
                        "separation" -> {
                            if (structureName == "oceanmonument") {
                                val structure = newStructures.getOrDefault("minecraft:monument", DEFAULTS["minecraft:monument"]!!)
                                val newSpacing = max(1, structureValue.toIntOrNull() ?: structure.separation)
                                newStructures["minecraft:monument"] = StructureFeatureConfig(newSpacing, structure.separation, structure.salt)
                            }
                        }
                        "spacing" -> {
                            if (structureName == "oceanmonument") newStructures.setSpacing("minecraft:monument", structureValue, 1)
                        }
                    }
                }
            }
        }

        val ret = NBTTypeUtil.createEmptyMap<String>()
        val structuresSerialized = NBTTypeUtil.createEmptyMap<String>()
        ret.setMap("structures", structuresSerialized)
        newStructures.keys.forEach { structuresSerialized.setMap(it, newStructures[it]!!.serialize()) }

        if (stronghold) {
            val strongholdData = NBTTypeUtil.createEmptyMap<String>()
            ret.setMap("stronghold", strongholdData)
            strongholdData.setInt("distance", distance)
            strongholdData.setInt("spread", spread)
            strongholdData.setInt("count", count)
        }
        return ret
    }

    fun vanillaLevels(seed: Long, generator: MapType<String>, caves: Boolean): MapType<String> {
        val ret = NBTTypeUtil.createEmptyMap<String>()

        val overworld = NBTTypeUtil.createEmptyMap<String>()
        val nether = NBTTypeUtil.createEmptyMap<String>()
        val end = NBTTypeUtil.createEmptyMap<String>()

        ret.setMap("minecraft:overworld", overworld)
        ret.setMap("minecraft:the_nether", nether)
        ret.setMap("minecraft:the_end", end)

        // overworld
        overworld.setString("type", if (caves) "minecraft:overworld_caves" else "minecraft:overworld")
        overworld.setMap("generator", generator)

        // nether
        nether.setString("type", "minecraft:the_nether")
        nether.setMap("generator", noise(seed, "minecraft:nether", NBTTypeUtil.createEmptyMap<String>().apply {
            setString("type", "minecraft:multi_noise")
            setLong("seed", seed)
            setString("preset", "minecraft:nether")
        }))

        // end
        end.setString("type", "minecraft:the_end")
        end.setMap("generator", noise(seed, "minecraft:the_end", NBTTypeUtil.createEmptyMap<String>().apply {
            setString("type", "minecraft:the_end")
            setLong("seed", seed)
        }))
        return ret
    }

    private fun MutableMap<String, StructureFeatureConfig>.setSpacing(structureName: String, value: String, minimum: Int) {
        val structure = getOrDefault(structureName, DEFAULTS[structureName]!!)
        val newSpacing = max(minimum, value.toIntOrNull() ?: structure.spacing)
        put(structureName, StructureFeatureConfig(newSpacing, structure.separation, structure.salt))
    }

    private data class StructureFeatureConfig(
        val spacing: Int,
        val separation: Int,
        val salt: Int
    ) {

        fun serialize() = NBTTypeUtil.createEmptyMap<String>().apply {
            setInt("spacing", spacing)
            setInt("separation", separation)
            setInt("salt", salt)
        }
    }
}
