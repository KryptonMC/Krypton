package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.registry.toNamespacedKey

data class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(NamespacedKey(value = "flat"))

data class FlatGeneratorSettings(
    val layers: List<FlatLayer>,
    val biome: NamespacedKey,
    override val structures: GeneratorStructures
) : GeneratorSettings() {

    companion object {

        fun fromNBT(nbt: CompoundBinaryTag) = FlatGeneratorSettings(
            nbt.getList("layers").map { layer ->
                (layer as CompoundBinaryTag).let {
                    FlatLayer(it.getString("block").toNamespacedKey(), it.getInt("height"))
                }
            },
            nbt.getString("biome").toNamespacedKey(),
            nbt.getCompound("structures").let { nbtStructures ->
                val stronghold = nbtStructures.getCompound("stronghold")
                val structures = nbtStructures.getCompound("structures")
                GeneratorStructures(
                    GeneratorStronghold(
                        stronghold.getInt("distance"),
                        stronghold.getInt("count"),
                        stronghold.getInt("spread")
                    ),
                    structures.associate { (key, value) ->
                        key.toNamespacedKey() to (value as CompoundBinaryTag).let {
                            GeneratorStructure(
                                it.getInt("spacing"),
                                it.getInt("separation"),
                                it.getInt("salt")
                            )
                        }
                    }
                )
            }
        )
    }
}

data class FlatLayer(
    val block: NamespacedKey,
    val height: Int
)