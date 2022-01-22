package org.kryptonmc.krypton.world.generation

import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.world.biome.gen.FixedBiomeGenerator

object DebugChunkGenerator : KryptonChunkGenerator(FixedBiomeGenerator(Biomes.PLAINS), emptySet())
