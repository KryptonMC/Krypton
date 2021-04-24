package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey

abstract class GeneratorSettings {

    abstract val structures: GeneratorStructures

    abstract fun toNBT(): CompoundBinaryTag
}

data class GeneratorStructures(
    val stronghold: GeneratorStronghold,
    val structures: Map<NamespacedKey, GeneratorStructure>
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .put("stronghold", stronghold.toNBT())
        .put("structures", CompoundBinaryTag.from(structures.mapKeys { it.key.toString() }.mapValues { it.value.toNBT() }))
        .build()
}

data class GeneratorStronghold(
    val distance: Int,
    val count: Int,
    val spread: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("distance", distance)
        .putInt("count", count)
        .putInt("spread", spread)
        .build()
}

data class GeneratorStructure(
    val spacing: Int,
    val separation: Int,
    val salt: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("spacing", spacing)
        .putInt("separation", separation)
        .putInt("salt", salt)
        .build()
}
