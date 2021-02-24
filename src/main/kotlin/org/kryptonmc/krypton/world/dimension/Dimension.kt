package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.world.generation.Generator

data class Dimension(
    val type: NamespacedKey,
    val generator: Generator
)