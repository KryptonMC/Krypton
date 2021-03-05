package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.registry.toNamespacedKey

data class NoiseGenerator(
    val seed: Int,
    val settings: NamespacedKey,
//    val settings: NoiseGeneratorSettings,
    val biomeSource: BiomeGenerator
) : Generator(NamespacedKey(value = "noise"))

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

        fun fromNBT(nbt: CompoundBinaryTag) = when (val type = nbt.getString("type").toNamespacedKey()) {
            NamespacedKey(value = "vanilla_layered") -> VanillaLayeredBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getBoolean("large_biomes")
            )
            NamespacedKey(value = "multi_noise") -> MultiNoiseBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getString("preset").toNamespacedKey()
            )
            NamespacedKey(value = "the_end") -> TheEndBiomeGenerator(nbt.getInt("seed"))
            NamespacedKey(value = "fixed") -> FixedBiomeGenerator(
                nbt.getInt("seed"),
                nbt.getString("biome")
            )
            NamespacedKey(value = "checkerboard") -> CheckerboardBiomeGenerator(
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
) : BiomeGenerator(NamespacedKey(value = "vanilla_layered"))

data class MultiNoiseBiomeGenerator(
    override val seed: Int,
    val preset: NamespacedKey
) : BiomeGenerator(NamespacedKey(value = "multi_noise"))

data class TheEndBiomeGenerator(override val seed: Int) : BiomeGenerator(NamespacedKey(value = "the_end"))

data class FixedBiomeGenerator(
    override val seed: Int,
    val biome: String
) : BiomeGenerator(NamespacedKey(value = "fixed"))

data class CheckerboardBiomeGenerator(
    override val seed: Int,
    val biomes: List<Int>,
    val scale: Int
) : BiomeGenerator(NamespacedKey(value = "checkerboard"))

data class NoiseSettings(
    val firstOctave: Int,
    val amplitudes: List<Float>
)