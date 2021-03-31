package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.world.generation.Generator

data class Dimension(
    val type: NamespacedKey,
    val generator: Generator
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type.toString())
        .put("generator", generator.toNBT())
        .build()
}