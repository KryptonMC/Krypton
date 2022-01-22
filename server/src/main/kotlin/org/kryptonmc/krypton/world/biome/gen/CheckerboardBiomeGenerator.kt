package org.kryptonmc.krypton.world.biome.gen

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeGenerator

class CheckerboardBiomeGenerator(override val biomes: List<Biome>, size: Int) : BiomeGenerator {

    private val bitShift = size + 2

    override fun generate(x: Int, y: Int, z: Int): Biome = biomes[Math.floorMod((x shr bitShift) + (z shr bitShift), biomes.size)]
}
