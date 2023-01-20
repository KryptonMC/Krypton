package org.kryptonmc.api.world.biome

public interface BiomeProvider {

    public fun possibleBiomes(): Set<Biome>

    public fun getBiome(x: Int, y: Int, z: Int): Biome
}
