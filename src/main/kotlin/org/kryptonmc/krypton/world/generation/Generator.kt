package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.registry.NamespacedKey

abstract class Generator(val id: NamespacedKey)

object DebugGenerator : Generator(NamespacedKey(value = "debug"))

// TODO: Add support for generators when world generation exists
//fun CompoundBinaryTag.toGenerator() = when (val type = getString("type").toNamespacedKey()) {
//    NamespacedKey(value = "flat") -> FlatGenerator(FlatGeneratorSettings.fromNBT(getCompound("settings")))
//    NamespacedKey(value = "noise") -> NoiseGenerator()
//    else -> throw IllegalArgumentException("Unsupported generator type $type")
//}