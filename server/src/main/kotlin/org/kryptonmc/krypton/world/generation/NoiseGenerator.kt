package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

data class NoiseGenerator(
    val seed: Int,
    val settings: NamespacedKey,
    val biomeSource: BiomeGenerator
) : Generator(ID) {

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
) : GeneratorSettings()

data class NoiseBlockState(
    val name: NamespacedKey,
    val properties: Map<String, String>
)

data class NoiseGeneratorConfig(
    val height: Int,
    val horizontalSize: Int,
    val verticalSize: Int,
    val densityFactor: Double,
    val simplexSurfaceNoise: Boolean,
    val randomDensityOffset: Boolean = false,
    val islandNoiseOverride: Boolean = false,
    val amplified: Boolean = false,
    val sampling: NoiseSampling,
    val topSlide: NoiseSlide,
    val bottomSlide: NoiseSlide
)

data class NoiseSampling(
    val xzScale: Double,
    val xzFactor: Double,
    val yScale: Double,
    val yFactor: Double
)

data class NoiseSlide(
    val target: Int,
    val size: Int,
    val offset: Int
)

sealed class BiomeGenerator(val type: NamespacedKey) {

    abstract val seed: Int

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
                nbt.getList("biomes").map { (it as IntBinaryTag).value() },
                nbt.getInt("scale")
            )
            else -> throw UnsupportedOperationException("Unsupported biome generator type $type")
        }
    }
}

data class VanillaLayeredBiomeGenerator(
    override val seed: Int,
    val largeBiomes: Boolean,
) : BiomeGenerator(VANILLA_LAYERED)

data class MultiNoiseBiomeGenerator(
    override val seed: Int,
    val preset: NamespacedKey
) : BiomeGenerator(MULTI_NOISE)

data class TheEndBiomeGenerator(override val seed: Int) : BiomeGenerator(THE_END)

data class FixedBiomeGenerator(
    override val seed: Int,
    val biome: String
) : BiomeGenerator(FIXED)

data class CheckerboardBiomeGenerator(
    override val seed: Int,
    val biomes: List<Int>,
    val scale: Int
) : BiomeGenerator(CHECKERBOARD)

data class NoiseSettings(
    val firstOctave: Int,
    val amplitudes: List<Float>
)