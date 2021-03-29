package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

abstract class Generator(val id: NamespacedKey)

// we want to use this in the constructor, but if we use a property, it's not initialised
// by the time we need it
private val DEBUG_GENERATOR_ID = NamespacedKey(value = "debug")

object DebugGenerator : Generator(DEBUG_GENERATOR_ID)

// TODO: Add support for generators when world generation exists
fun CompoundBinaryTag.toGenerator() = when (val type = getString("type").toNamespacedKey()) {
    FlatGenerator.ID -> FlatGenerator(FlatGeneratorSettings.fromNBT(getCompound("settings")))
    NoiseGenerator.ID -> NoiseGenerator(
        getInt("seed"),
        getString("settings").toNamespacedKey(),
        BiomeGenerator.fromNBT(getCompound("biome_source"))
    )
    DEBUG_GENERATOR_ID -> DebugGenerator
    else -> throw IllegalArgumentException("Unsupported generator type $type")
}