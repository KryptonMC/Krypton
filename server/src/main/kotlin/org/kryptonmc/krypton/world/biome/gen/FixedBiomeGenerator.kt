package org.kryptonmc.krypton.world.biome.gen

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeGenerator

class FixedBiomeGenerator(private val biome: Biome) : BiomeGenerator {

    override val biomes: List<Biome> = persistentListOf(biome)

    override fun generate(x: Int, y: Int, z: Int): Biome = biome
}
