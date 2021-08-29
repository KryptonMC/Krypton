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

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Codec
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicLike
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.OptionalDynamic
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.krypton.util.datafix.References
import java.util.Optional
import java.util.stream.Stream
import kotlin.math.max

class WorldGenSettingsFix(outputSchema: Schema) : DataFix(outputSchema, true) {

    override fun makeRule(): TypeRewriteRule = fixTypeEverywhereTyped("WorldGenSettings building", inputSchema.getType(References.WORLD_GEN_SETTINGS)) { typed -> typed.update(remainderFinder()) { it.fix() } }

    data class StructureFeatureConfig(val spacing: Int, val separation: Int, val salt: Int) {

        fun <T> serialize(ops: DynamicOps<T>) = Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap()))

        companion object {

            val CODEC: Codec<StructureFeatureConfig> = RecordCodecBuilder.create<StructureFeatureConfig> { builder -> builder.group(
                Codec.INT.fieldOf("spacing").forGetter { it.spacing },
                Codec.INT.fieldOf("separation").forGetter { it.separation },
                Codec.INT.fieldOf("salt").forGetter { it.salt }
            ).apply(builder, ::StructureFeatureConfig) }
        }
    }

    companion object {

        private const val VILLAGE = "minecraft:village"
        private const val DESERT_PYRAMID = "minecraft:desert_pyramid"
        private const val IGLOO = "minecraft:igloo"
        private const val JUNGLE_TEMPLE = "minecraft:jungle_pyramid"
        private const val SWAMP_HUT = "minecraft:swamp_hut"
        private const val PILLAGER_OUTPOST = "minecraft:pillager_outpost"
        private const val END_CITY = "minecraft:endcity"
        private const val WOODLAND_MANSION = "minecraft:mansion"
        private const val OCEAN_MONUMENT = "minecraft:monument"
        private val CUSTOMIZED = Optional.of("customized")
        private val DEFAULTS = object : Map<String, StructureFeatureConfig> by mapOf(
            VILLAGE to StructureFeatureConfig(32, 8, 10387312),
            DESERT_PYRAMID to StructureFeatureConfig(32, 8, 14357617),
            IGLOO to StructureFeatureConfig(32, 8, 14357618),
            JUNGLE_TEMPLE to StructureFeatureConfig(32, 8, 14357619),
            SWAMP_HUT to StructureFeatureConfig(32, 8, 14357620),
            PILLAGER_OUTPOST to StructureFeatureConfig(32, 8, 165745296),
            OCEAN_MONUMENT to StructureFeatureConfig(32, 5, 10387313),
            END_CITY to StructureFeatureConfig(20, 11, 10387313),
            WOODLAND_MANSION to StructureFeatureConfig(80, 20, 10387319)
        ) {
            override fun get(key: String) = getValue(key)
        }

        fun <T> Dynamic<T>.vanillaLevels(seed: Long, generator: Dynamic<T>, isCaves: Boolean): T = with(ops) {
            createMap(mapOf(
                createString("minecraft:overworld") to createMap(mapOf(
                    createString("type") to createString("minecraft:overworld${if (isCaves) "_caves" else ""}"),
                    createString("generator") to generator.value
                )),
                createString("minecraft:the_nether") to createMap(mapOf(
                    createString("type") to createString("minecraft:the_nether"),
                    createString("generator") to with(this@vanillaLevels) { noise(seed, createString("minecraft:nether"), createMap(mapOf(
                        createString("type") to createString("multi_noise"),
                        createString("seed") to createLong(seed),
                        createString("preset") to createString("minecraft:nether")
                    ))) }.value
                )),
                createString("minecraft:the_end") to createMap(mapOf(
                    createString("type") to createString("minecraft:the_end"),
                    createString("generator") to with(this@vanillaLevels) { noise(seed, createString("minecraft:the_end"), createMap(mapOf(
                        createString("type") to createString("minecraft:the_end"),
                        createString("seed") to createLong(seed)
                    ))) }.value
                )
                )))
        }

        fun <T> Dynamic<T>.defaultOverworld(seed: Long): Dynamic<T> =
            noise(seed, createString("minecraft:overworld"), vanillaBiomeSource(seed, legacyInitLayer = false, largeBiomes = false))

        private fun <T> DynamicLike<T>.noise(seed: Long, settings: Dynamic<T>, biomeSource: Dynamic<T>) = createMap(mapOf(
            createString("type") to createString("minecraft:noise"),
            createString("biome_source") to biomeSource,
            createString("seed") to createLong(seed),
            createString("settings") to settings
        ))

        private fun <T> Dynamic<T>.vanillaBiomeSource(seed: Long, legacyInitLayer: Boolean, largeBiomes: Boolean) = createMap(mapOf(
            createString("type") to createString("minecraft:vanilla_layered"),
            createString("seed") to createLong(seed),
            createString("large_biomes") to createBoolean(largeBiomes)
        ).let { if (legacyInitLayer) it.plus(createString("legacy_biome_int_layer") to createBoolean(true)) else it })

        private fun <T : Map<K, V>, K, V> Optional<T>.forEachIfPresent(action: (K, V) -> Unit) = ifPresent { it.forEach(action) }

        @JvmStatic
        private fun <T> Dynamic<T>.fix(): Dynamic<T> {
            val randomSeed = get("RandomSeed").asLong(0L)
            val generatorName = get("generatorName").asString().map { it.lowercase() }.result()
            val legacyCustomOptions = get("legacy_custom_options").asString().result().map { Optional.of(it) }.orElseGet {
                if (generatorName == CUSTOMIZED) get("generatorOptions").asString().result() else Optional.empty()
            }
            var isCaves = false
            val generator = when {
                generatorName == CUSTOMIZED -> defaultOverworld(randomSeed)
                generatorName.isEmpty -> defaultOverworld(randomSeed)
                else -> when (generatorName.get()) {
                    "flat" -> {
                        val generatorOptions = get("generatorOptions")
                        val fixedStructures = generatorOptions.fixFlatStructures(ops)
                        createMap(mapOf(
                            createString("type") to createString("minecraft:flat"),
                            createString("settings") to createMap(mapOf(
                                createString("structures") to createMap(fixedStructures),
                                createString("layers") to generatorOptions["layers"].result().orElseGet { createList(Stream.of(
                                    createMap(mapOf(
                                        createString("height") to createInt(1),
                                        createString("block") to createString("minecraft:bedrock"),
                                    )),
                                    createMap(mapOf(
                                        createString("height") to createInt(2),
                                        createString("block") to createString("minecraft:dirt")
                                    )),
                                    createMap(mapOf(
                                        createString("height") to createInt(1),
                                        createString("block") to createString("minecraft:grass_block")
                                    ))
                                )) },
                                createString("biome") to createString(generatorOptions["biome"].asString("minecraft:plains"))
                            ))
                        ))
                    }
                    "debug_all_block_states" -> createMap(mapOf(createString("type") to createString("minecraft:debug")))
                    "buffet" -> {
                        val generatorOptions = get("generatorOptions")
                        val chunkGenerator = generatorOptions["chunk_generator"]
                        val settings = when (chunkGenerator["type"].asString().result()) {
                            Optional.of("minecraft:caves") -> {
                                isCaves = true
                                createString("minecraft:caves")
                            }
                            Optional.of("minecraft:floating_islands") -> createString("minecraft:floating_islands")
                            else -> createString("minecraft:overworld")
                        }
                        val biomeSource = generatorOptions["biome_source"].result().orElseGet { createMap(mapOf(createString("type") to createString("minecraft:fixed"))) }
                        val modified = if (biomeSource["type"].asString().result() == Optional.of("minecraft:fixed")) {
                            val biomes = biomeSource["options"]["biomes"].asStream().findFirst().flatMap { it.asString().result() }.orElse("minecraft:ocean")
                            biomeSource.remove("options").set("biome", createString(biomes))
                        } else {
                            biomeSource
                        }
                        noise(randomSeed, settings, modified)
                    }
                    else -> {
                        val isDefault = generatorName.get() == "default"
                        val isDefaultVersion = generatorName.get() == "default_1_1" || isDefault && get("generatorVersion").asInt(0) == 0
                        val isAmplified = generatorName.get() == "amplified"
                        val isLargeBiomes = generatorName.get() == "largebiomes"
                        noise(randomSeed, createString(if (isAmplified) "minecraft:amplified" else "minecraft:overworld"), vanillaBiomeSource(randomSeed, isDefaultVersion, isLargeBiomes))
                    }
                }
            }
            val mapFeatures = get("MapFeatures").asBoolean(true)
            val bonusChest = get("BonusChest").asBoolean(false)
            val map = mutableMapOf(
                ops.createString("seed") to ops.createLong(randomSeed),
                ops.createString("generate_features") to ops.createBoolean(mapFeatures),
                ops.createString("bonus_chest") to ops.createBoolean(bonusChest),
                ops.createString("dimensions") to vanillaLevels(randomSeed, generator, isCaves)
            )
            legacyCustomOptions.ifPresent { map[ops.createString("legacy_custom_options")] = ops.createString(it) }
            return Dynamic(ops, ops.createMap(map))
        }

        @JvmStatic
        private fun <T> OptionalDynamic<T>.fixFlatStructures(ops: DynamicOps<T>): Map<Dynamic<T>, Dynamic<T>> {
            var distance = 32
            var spread = 3
            var count = 128
            var isStronghold = false
            val configurations = mutableMapOf<String, StructureFeatureConfig>()
            if (result().isEmpty) {
                isStronghold = true
                configurations[VILLAGE] = DEFAULTS[VILLAGE]
            }
            get("structures").flatMap { it.mapValues }.result().forEachIfPresent { nameData, structureData ->
                structureData.mapValues.result().forEachIfPresent structures@{ parameterData, valueData ->
                    val name = nameData.asString("")
                    val parameterName = parameterData.asString("")
                    val value = valueData.asString("")
                    if (name == "stronghold") {
                        isStronghold = true
                        when (parameterName) {
                            "distance" -> {
                                distance = max(distance, value.toIntOrNull() ?: 1)
                                return@structures
                            }
                            "spread" -> {
                                spread = max(spread, value.toIntOrNull() ?: 1)
                                return@structures
                            }
                            "count" -> {
                                count = max(count, value.toIntOrNull() ?: 1)
                                return@structures
                            }
                        }
                    } else when (parameterName) {
                        "distance" -> when (name) {
                            "village" -> {
                                configurations.setSpacing(VILLAGE, value, 9)
                                return@structures
                            }
                            "biome_1" -> {
                                configurations.setSpacing(DESERT_PYRAMID, value, 9)
                                configurations.setSpacing(IGLOO, value, 9)
                                configurations.setSpacing(JUNGLE_TEMPLE, value, 9)
                                configurations.setSpacing(SWAMP_HUT, value, 9)
                                configurations.setSpacing(PILLAGER_OUTPOST, value, 9)
                                return@structures
                            }
                            "endcity" -> {
                                configurations.setSpacing(END_CITY, value, 1)
                                return@structures
                            }
                            "mansion" -> {
                                configurations.setSpacing(WOODLAND_MANSION, value, 1)
                                return@structures
                            }
                            else -> return@structures
                        }
                        "separation" -> {
                            if (name == "oceanmonument") {
                                val config = configurations.getOrDefault(OCEAN_MONUMENT, DEFAULTS[OCEAN_MONUMENT])
                                val separation = max(config.separation, value.toIntOrNull() ?: 1)
                                configurations[OCEAN_MONUMENT] = config.copy(spacing = separation)
                            }
                            return@structures
                        }
                        "spacing" -> {
                            if (name == "oceanmonument") configurations.setSpacing(OCEAN_MONUMENT, value, 1)
                            return@structures
                        }
                    }
                }
            }
            return mapOf(
                createString("structures") to createMap(configurations.entries.associate { createString(it.key) to it.value.serialize(ops) })
            ).let { if (isStronghold) it.plus(createString("stronghold") to createMap(mapOf(
                createString("distance") to createInt(distance),
                createString("spread") to createInt(spread),
                createString("count") to createInt(count)
            ))) else it }
        }

        private fun MutableMap<String, StructureFeatureConfig>.setSpacing(name: String, value: String, spacing: Int) {
            val config = getOrDefault(name, DEFAULTS[name])
            val spacingInt = max(config.spacing, value.toIntOrNull() ?: spacing)
            put(name, config.copy(spacing = spacingInt))
        }
    }
}
