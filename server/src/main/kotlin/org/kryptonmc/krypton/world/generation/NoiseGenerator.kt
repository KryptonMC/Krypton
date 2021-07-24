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
package org.kryptonmc.krypton.world.generation

import com.mojang.serialization.Dynamic
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.util.toKey
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.compound

data class NoiseGenerator(
    val seed: Int,
    val settings: Key,
    val biomeSource: BiomeGenerator
) : Generator(ID) {

    override fun toNBT() = compound {
        string("type", ID.asString())
        int("seed", seed)
        string("settings", settings.asString())
        put("biome_source", biomeSource.toNBT())
    }

    companion object {

        val ID = key("noise")
    }
}

data class NoiseGeneratorSettings(
    val bedrockRoofPosition: Int,
    val bedrockFloorPosition: Int,
    val seaLevel: Int,
    val disableMobGeneration: Boolean,
    val defaultBlock: NoiseBlockState,
    val defaultFluid: NoiseBlockState,
    override val structures: GeneratorStructures,
    val noise: NoiseGeneratorConfig
) : GeneratorSettings() {

    override fun toNBT() = compound {
        int("bedrock_roof_position", bedrockRoofPosition)
        int("bedrock_floor_position", bedrockFloorPosition)
        int("sea_level", seaLevel)
        boolean("disable_mob_generation", disableMobGeneration)
        put("default_block", defaultBlock.toNBT())
        put("default_fluid", defaultFluid.toNBT())
        put("structures", structures.toNBT())
        put("noise", noise.toNBT())
    }
}

data class NoiseBlockState(
    val name: Key,
    val properties: Map<String, String>
) {

    fun toNBT() = compound {
        string("Name", name.asString())
        compound("Properties") { properties.forEach { string(it.key, it.value) } }
    }
}

data class NoiseGeneratorConfig(
    val height: Int,
    val horizontalSize: Int,
    val verticalSize: Int,
    val densityFactor: Double,
    val densityOffset: Double,
    val simplexSurfaceNoise: Boolean,
    val randomDensityOffset: Boolean = false,
    val islandNoiseOverride: Boolean = false,
    val amplified: Boolean = false,
    val sampling: NoiseSampling,
    val topSlide: NoiseSlide,
    val bottomSlide: NoiseSlide
) {

    fun toNBT() = compound {
        int("height", height)
        int("size_horizontal", horizontalSize)
        int("size_vertical", verticalSize)
        double("density_factor", densityFactor)
        double("density_offset", densityOffset)
        boolean("simplex_surface_noise", simplexSurfaceNoise)
        if (randomDensityOffset) boolean("random_density_offset", true)
        if (islandNoiseOverride) boolean("island_noise_override", true)
        if (amplified) boolean("amplified", true)
        put("sampling", sampling.toNBT())
        put("top_slide", topSlide.toNBT())
        put("bottom_slide", bottomSlide.toNBT())
    }
}

data class NoiseSampling(
    val xzScale: Double,
    val xzFactor: Double,
    val yScale: Double,
    val yFactor: Double
) {

    fun toNBT() = compound {
        double("xz_scale", xzScale)
        double("xz_factor", xzFactor)
        double("y_scale", yScale)
        double("y_factor", yFactor)
    }
}

data class NoiseSlide(
    val target: Int,
    val size: Int,
    val offset: Int
) {

    fun toNBT() = compound {
        int("target", target)
        int("size", size)
        int("offset", offset)
    }
}

sealed class BiomeGenerator(val type: Key, val seed: Int) {

    abstract fun toNBT(): CompoundTag

    companion object {

        val VANILLA_LAYERED = key("vanilla_layered")
        val MULTI_NOISE = key("multi_noise")
        val THE_END = key("the_end")
        val FIXED = key("fixed")
        val CHECKERBOARD = key("checkerboard")

        fun of(data: Dynamic<*>) = when (val type = data["type"].asString("").toKey()) {
            VANILLA_LAYERED -> VanillaLayeredBiomeGenerator(
                data["seed"].asInt(0),
                data["large_biomes"].asBoolean(false)
            )
            MULTI_NOISE -> MultiNoiseBiomeGenerator(
                data["seed"].asInt(0),
                data["preset"].asString("").toKey()
            )
            THE_END -> TheEndBiomeGenerator(data["seed"].asInt(0))
            FIXED -> FixedBiomeGenerator(
                data["seed"].asInt(0),
                data["biome"].asString("minecraft:plains")
            )
            CHECKERBOARD -> CheckerboardBiomeGenerator(
                data["seed"].asInt(0),
                data["biomes"].asList { it.asInt(0) }.toIntArray(),
                data["scale"].asInt(0)
            )
            else -> throw UnsupportedOperationException("Unsupported biome generator type $type")
        }
    }
}

class VanillaLayeredBiomeGenerator(
    seed: Int,
    val largeBiomes: Boolean,
) : BiomeGenerator(VANILLA_LAYERED, seed) {

    override fun toNBT() = compound {
        string("type", VANILLA_LAYERED.asString())
        int("seed", seed)
        boolean("large_biomes", largeBiomes)
    }
}

class MultiNoiseBiomeGenerator(
    seed: Int,
    val preset: Key
) : BiomeGenerator(MULTI_NOISE, seed) {

    override fun toNBT() = compound {
        string("type", MULTI_NOISE.asString())
        int("seed", seed)
        string("preset", preset.asString())
    }
}

class TheEndBiomeGenerator(seed: Int) : BiomeGenerator(THE_END, seed) {

    override fun toNBT() = compound {
        string("type", THE_END.asString())
        int("seed", seed)
    }
}

class FixedBiomeGenerator(
    seed: Int,
    val biome: String
) : BiomeGenerator(FIXED, seed) {

    override fun toNBT() = compound {
        string("type", FIXED.asString())
        int("seed", seed)
        string("biome", biome)
    }
}

class CheckerboardBiomeGenerator(
    seed: Int,
    val biomes: IntArray,
    val scale: Int
) : BiomeGenerator(CHECKERBOARD, seed) {

    override fun toNBT() = compound {
        string("type", CHECKERBOARD.asString())
        int("seed", seed)
        list("biomes") { biomes.forEach { add(IntTag.of(it)) } }
        int("scale", scale)
    }
}

class NoiseSettings(
    val firstOctave: Int,
    val amplitudes: FloatArray
)
