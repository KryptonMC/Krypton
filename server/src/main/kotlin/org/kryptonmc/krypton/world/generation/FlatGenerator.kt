package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

data class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(ID) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", ID.toString())
        .put("settings", settings.toNBT())
        .build()

    companion object {

        val ID = NamespacedKey(value = "flat")
    }
}

data class FlatGeneratorSettings(
    val layers: List<FlatLayer>,
    val biome: NamespacedKey,
    override val structures: GeneratorStructures
) : GeneratorSettings() {

    override fun toNBT(): CompoundBinaryTag {
        return CompoundBinaryTag.builder()
            .put("layers", ListBinaryTag.of(BinaryTagTypes.COMPOUND, layers.map(FlatLayer::toNBT)))
            .putString("biome", biome.toString())
            .put("structures", structures.toNBT())
            .build()
    }

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
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("height", height)
        .putString("block", block.toString())
        .build()
}
