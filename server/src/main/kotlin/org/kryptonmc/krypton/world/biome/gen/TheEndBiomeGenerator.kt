package org.kryptonmc.krypton.world.biome.gen

import de.articdive.jnoise.JNoise
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeGenerator
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.util.Quart
import org.kryptonmc.krypton.util.clamp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class TheEndBiomeGenerator(private val seed: Long) : BiomeGenerator {

    private val noise = JNoise.newBuilder()
        .fastSimplex()
        .setSeed(seed)
        .build()
    override val biomes: List<Biome> = persistentListOf(
        Biomes.THE_END,
        Biomes.END_HIGHLANDS,
        Biomes.END_MIDLANDS,
        Biomes.SMALL_END_ISLANDS,
        Biomes.END_BARRENS
    )

    override fun generate(x: Int, y: Int, z: Int): Biome {
        val quartX = Quart.fromBlock(x)
        val quartZ = Quart.fromBlock(z)
        if (quartX * quartX + quartZ * quartZ <= ISLAND_CHUNK_DISTANCE_SQUARED) return Biomes.THE_END
        val height = getHeightValue(noise, quartX * 2 + 1, quartZ * 2 + 1)
        if (height > 40F) return Biomes.END_HIGHLANDS
        if (height >= 0F) return Biomes.END_MIDLANDS
        if (height < -20F) return Biomes.SMALL_END_ISLANDS
        return Biomes.END_BARRENS
    }

    companion object {

        private const val ISLAND_CHUNK_DISTANCE_SQUARED = 4096L

        @JvmStatic
        private fun getHeightValue(noise: JNoise, x: Int, z: Int): Float {
            val halfX = x / 2
            val halfZ = z / 2
            val modX = x % 2
            val modZ = z % 2
            var height = calculateHeight(x.toFloat(), 8F, z.toFloat())

            for (i in -12..12) {
                for (j in -12..12) {
                    val offsetX = halfX + i
                    val offsetZ = halfZ + j
                    if (offsetX * offsetX + offsetZ * offsetX <= ISLAND_CHUNK_DISTANCE_SQUARED) continue
                    if (noise.getNoise(offsetX.toDouble(), offsetZ.toDouble()) >= -0.9F) continue
                    val heightY = (abs(offsetX) * 3439F + abs(offsetZ) * 147F) % 13F + 9F
                    val heightX = (modX - i * 2).toFloat()
                    val heightZ = (modZ - j * 2).toFloat()
                    height = max(height, calculateHeight(heightX, heightY, heightZ))
                }
            }
            return height
        }

        @JvmStatic
        private fun calculateHeight(x: Float, y: Float, z: Float): Float = (100F - sqrt(x * x + z * z) * y).clamp(-100F, 80F)
    }
}
