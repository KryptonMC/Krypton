package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.registry.NamespacedKey

abstract class GeneratorSettings {

    abstract val structures: GeneratorStructures
}

data class GeneratorStructures(
    val stronghold: GeneratorStronghold,
    val structures: List<GeneratorStructure>
)

data class GeneratorStronghold(
    val distance: Int,
    val count: Int,
    val spread: Int
)

data class GeneratorStructure(
    val id: NamespacedKey,
    val spacing: Int,
    val separation: Int,
    val salt: Int
)