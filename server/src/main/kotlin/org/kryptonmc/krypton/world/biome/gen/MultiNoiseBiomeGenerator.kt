package org.kryptonmc.krypton.world.biome.gen

import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeGenerator
import org.kryptonmc.api.world.biome.Biomes

class MultiNoiseBiomeGenerator private constructor(
    private val parameters: Climate.ParameterList<Biome>,
    private val sampler: Climate.Sampler
) : BiomeGenerator {

    override val biomes: List<Biome> = parameters.pairs.map { it.second }

    override fun generate(x: Int, y: Int, z: Int): Biome = parameters.find(sampler.sample(x, y, z))

    class Preset(name: Key, private val parameters: Climate.ParameterList<Biome>) {

        init {
            BY_NAME[name] = this
        }

        fun generator(sampler: Climate.Sampler): MultiNoiseBiomeGenerator = MultiNoiseBiomeGenerator(parameters, sampler)

        companion object {

            private val BY_NAME = mutableMapOf<Key, Preset>()

            @JvmField
            val OVERWORLD: Preset = Preset(Key.key("overworld"), createOverworldParameters())
            @JvmField
            val NETHER: Preset = Preset(Key.key("nether"), createNetherParameters())

            @JvmStatic
            private fun createOverworldParameters(): Climate.ParameterList<Biome> {
                val builder = persistentListOf<Pair<Climate.ParameterPoint, Biome>>().builder()
                OverworldBiomeBuilder.addBiomes { point, biome -> builder.add(Pair(point, biome)) }
                return Climate.ParameterList(builder.build())
            }

            @JvmStatic
            private fun createNetherParameters(): Climate.ParameterList<Biome> = Climate.ParameterList(persistentListOf(
                Pair(Climate.parameters(0F, 0F, 0F, 0F, 0F, 0F, 0F), Biomes.NETHER_WASTES),
                Pair(Climate.parameters(0F, -0.5F, 0F, 0F, 0F, 0F, 0F), Biomes.SOUL_SAND_VALLEY),
                Pair(Climate.parameters(0.4F, 0F, 0F, 0F, 0F, 0F, 0F), Biomes.CRIMSON_FOREST),
                Pair(Climate.parameters(0F, 0.5F, 0F, 0F, 0F, 0F, 0.375F), Biomes.WARPED_FOREST),
                Pair(Climate.parameters(-0.5F, 0F, 0F, 0F, 0F, 0F, 0.175F), Biomes.BASALT_DELTAS)
            ))
        }
    }
}
