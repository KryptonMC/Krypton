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

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

data class NoiseGenerator(
    val seed: Int,
    val settings: NamespacedKey,
    val biomeSource: BiomeGenerator
) : Generator(ID) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", ID.toString())
        .putInt("seed", seed)
        .putString("settings", settings.toString())
        .put("biome_source", biomeSource.toNBT())
        .build()

    companion object {

        val ID = NamespacedKey(value = "noise")
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

    override fun toNBT() = CompoundBinaryTag.builder()
        .putInt("bedrock_roof_position", bedrockRoofPosition)
        .putInt("bedrock_floor_position", bedrockFloorPosition)
        .putInt("sea_level", seaLevel)
        .putBoolean("disable_mob_generation", disableMobGeneration)
        .put("default_block", defaultBlock.toNBT())
        .put("default_fluid", defaultFluid.toNBT())
        .put("structures", structures.toNBT())
        .put("noise", noise.toNBT())
        .build()
}

data class NoiseBlockState(
    val name: NamespacedKey,
    val properties: Map<String, String>
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("Name", name.toString())
        .put("Properties", CompoundBinaryTag.from(properties.mapValues { StringBinaryTag.of(it.value) }))
        .build()
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

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("height", height)
        .putInt("size_horizontal", horizontalSize)
        .putInt("size_vertical", verticalSize)
        .putDouble("density_factor", densityFactor)
        .putDouble("density_offset", densityOffset)
        .putBoolean("simplex_surface_noise", simplexSurfaceNoise)
        .apply { if (randomDensityOffset) putBoolean("random_density_offset", randomDensityOffset) }
        .apply { if (islandNoiseOverride) putBoolean("island_noise_override", islandNoiseOverride) }
        .apply { if (amplified) putBoolean("amplified", amplified) }
        .put("sampling", sampling.toNBT())
        .put("top_slide", topSlide.toNBT())
        .put("bottom_slide", bottomSlide.toNBT())
        .build()
}

data class NoiseSampling(
    val xzScale: Double,
    val xzFactor: Double,
    val yScale: Double,
    val yFactor: Double
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putDouble("xz_scale", xzScale)
        .putDouble("xz_factor", xzFactor)
        .putDouble("y_scale", yScale)
        .putDouble("y_factor", yFactor)
        .build()
}

data class NoiseSlide(
    val target: Int,
    val size: Int,
    val offset: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("target", target)
        .putInt("size", size)
        .putInt("offset", offset)
        .build()
}

sealed class BiomeGenerator(val type: NamespacedKey) {

    abstract val seed: Int

    abstract fun toNBT(): CompoundBinaryTag

    companion object {

        internal val VANILLA_LAYERED = NamespacedKey(value = "vanilla_layered")
        internal val MULTI_NOISE = NamespacedKey(value = "multi_noise")
        internal val THE_END = NamespacedKey(value = "the_end")
        internal val FIXED = NamespacedKey(value = "fixed")
        internal val CHECKERBOARD = NamespacedKey(value = "checkerboard")

        fun fromNBT(nbt: CompoundBinaryTag) = when (val type = nbt.getString("type").toNamespacedKey()) {
            VANILLA_LAYERED -> VanillaLayeredBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getBoolean("large_biomes")
            )
            MULTI_NOISE -> MultiNoiseBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getString("preset").toNamespacedKey()
            )
            THE_END -> TheEndBiomeGenerator(nbt.getInt("seed"))
            FIXED -> FixedBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getString("biome")
            )
            CHECKERBOARD -> CheckerboardBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getList("biomes").map { (it as IntBinaryTag).value() }.toIntArray(),
                nbt.getInt("scale")
            )
            else -> throw UnsupportedOperationException("Unsupported biome generator type $type")
        }
    }
}

data class VanillaLayeredBiomeGenerator(
    override val seed: Int,
    val largeBiomes: Boolean,
) : BiomeGenerator(VANILLA_LAYERED) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", VANILLA_LAYERED.toString())
        .putInt("seed", seed)
        .putBoolean("large_biomes", largeBiomes)
        .build()
}

data class MultiNoiseBiomeGenerator(
    override val seed: Int,
    val preset: NamespacedKey
) : BiomeGenerator(MULTI_NOISE) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", MULTI_NOISE.toString())
        .putInt("seed", seed)
        .putString("preset", preset.toString())
        .build()
}

data class TheEndBiomeGenerator(override val seed: Int) : BiomeGenerator(THE_END) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", THE_END.toString())
        .putInt("seed", seed)
        .build()
}

data class FixedBiomeGenerator(
    override val seed: Int,
    val biome: String
) : BiomeGenerator(FIXED) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", FIXED.toString())
        .putInt("seed", seed)
        .putString("biome", biome)
        .build()
}

data class CheckerboardBiomeGenerator(
    override val seed: Int,
    val biomes: IntArray,
    val scale: Int
) : BiomeGenerator(CHECKERBOARD) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", CHECKERBOARD.toString())
        .putInt("seed", seed)
        .put("biomes", ListBinaryTag.of(BinaryTagTypes.INT, biomes.toList().map { IntBinaryTag.of(it) }))
        .putInt("scale", scale)
        .build()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CheckerboardBiomeGenerator
        return seed == other.seed && biomes.contentEquals(other.biomes) && scale == other.scale
    }

    override fun hashCode(): Int {
        var result = seed
        result = 31 * result + biomes.contentHashCode()
        result = 31 * result + scale
        return result
    }
}

data class NoiseSettings(
    val firstOctave: Int,
    val amplitudes: FloatArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NoiseSettings
        return firstOctave == other.firstOctave && amplitudes.contentEquals(other.amplitudes)
    }

    override fun hashCode(): Int {
        var result = firstOctave
        result = 31 * result + amplitudes.contentHashCode()
        return result
    }
}
