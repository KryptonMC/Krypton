package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.api.registry.NamespacedKey

abstract class GeneratorSettings {

    abstract val structures: GeneratorStructures
}

data class GeneratorStructures(
    val stronghold: GeneratorStronghold,
    val structures: Map<NamespacedKey, GeneratorStructure>
)

data class GeneratorStronghold(
    val distance: Int,
    val count: Int,
    val spread: Int
)

data class GeneratorStructure(
    val spacing: Int,
    val separation: Int,
    val salt: Int
)