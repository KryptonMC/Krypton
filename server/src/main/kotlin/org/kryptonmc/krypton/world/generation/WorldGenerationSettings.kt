package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.world.dimension.Dimension

data class WorldGenerationSettings(
    val seed: Long,
    val generateStructures: Boolean,
    val dimensions: Map<NamespacedKey, Dimension>
)
