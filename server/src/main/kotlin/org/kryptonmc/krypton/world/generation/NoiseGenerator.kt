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
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTInt
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.util.toKey

data class NoiseGenerator(
    val seed: Int,
    val settings: Key,
    val biomeSource: BiomeGenerator
) : Generator(ID) {

    override fun toNBT() = NBTCompound()
        .setString("type", ID.toString())
        .setInt("seed", seed)
        .setString("settings", settings.toString())
        .set("biome_source", biomeSource.toNBT())

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

    override fun toNBT() = NBTCompound()
        .setInt("bedrock_roof_position", bedrockRoofPosition)
        .setInt("bedrock_floor_position", bedrockFloorPosition)
        .setInt("sea_level", seaLevel)
        .setBoolean("disable_mob_generation", disableMobGeneration)
        .set("default_block", defaultBlock.toNBT())
        .set("default_fluid", defaultFluid.toNBT())
        .set("structures", structures.toNBT())
        .set("noise", noise.toNBT())
}

data class NoiseBlockState(
    val name: Key,
    val properties: Map<String, String>
) {

    fun toNBT() = NBTCompound()
        .setString("Name", name.toString())
        .set("Properties", NBTCompound().apply { properties.mapValues { NBTString(it.value) }.forEach { set(it.key, it.value) } })
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

    fun toNBT() = NBTCompound()
        .setInt("height", height)
        .setInt("size_horizontal", horizontalSize)
        .setInt("size_vertical", verticalSize)
        .setDouble("density_factor", densityFactor)
        .setDouble("density_offset", densityOffset)
        .setBoolean("simplex_surface_noise", simplexSurfaceNoise)
        .apply { if (randomDensityOffset) setBoolean("random_density_offset", randomDensityOffset) }
        .apply { if (islandNoiseOverride) setBoolean("island_noise_override", islandNoiseOverride) }
        .apply { if (amplified) setBoolean("amplified", amplified) }
        .set("sampling", sampling.toNBT())
        .set("top_slide", topSlide.toNBT())
        .set("bottom_slide", bottomSlide.toNBT())
}

data class NoiseSampling(
    val xzScale: Double,
    val xzFactor: Double,
    val yScale: Double,
    val yFactor: Double
) {

    fun toNBT() = NBTCompound()
        .setDouble("xz_scale", xzScale)
        .setDouble("xz_factor", xzFactor)
        .setDouble("y_scale", yScale)
        .setDouble("y_factor", yFactor)
}

data class NoiseSlide(
    val target: Int,
    val size: Int,
    val offset: Int
) {

    fun toNBT() = NBTCompound()
        .setInt("target", target)
        .setInt("size", size)
        .setInt("offset", offset)
}

sealed class BiomeGenerator(val type: Key, val seed: Int) {

    abstract fun toNBT(): NBTCompound

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

    override fun toNBT() = NBTCompound()
        .setString("type", VANILLA_LAYERED.toString())
        .setInt("seed", seed)
        .setBoolean("large_biomes", largeBiomes)
}

class MultiNoiseBiomeGenerator(
    seed: Int,
    val preset: Key
) : BiomeGenerator(MULTI_NOISE, seed) {

    override fun toNBT() = NBTCompound()
        .setString("type", MULTI_NOISE.toString())
        .setInt("seed", seed)
        .setString("preset", preset.toString())
}

class TheEndBiomeGenerator(seed: Int) : BiomeGenerator(THE_END, seed) {

    override fun toNBT() = NBTCompound()
        .setString("type", THE_END.toString())
        .setInt("seed", seed)
}

class FixedBiomeGenerator(
    seed: Int,
    val biome: String
) : BiomeGenerator(FIXED, seed) {

    override fun toNBT() = NBTCompound()
        .setString("type", FIXED.toString())
        .setInt("seed", seed)
        .setString("biome", biome)
}

class CheckerboardBiomeGenerator(
    seed: Int,
    val biomes: IntArray,
    val scale: Int
) : BiomeGenerator(CHECKERBOARD, seed) {

    override fun toNBT() = NBTCompound()
        .setString("type", CHECKERBOARD.asString())
        .setInt("seed", seed)
        .set("biomes", NBTList<NBTInt>(NBTTypes.TAG_Int).apply { biomes.forEach { add(NBTInt(it)) } })
        .setInt("scale", scale)
}

class NoiseSettings(
    val firstOctave: Int,
    val amplitudes: FloatArray
)
