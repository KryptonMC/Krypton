package org.kryptonmc.krypton.registry

import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry

class RegistryManager {

    val registries: Registries

    val dimensions: DimensionRegistry
    val biomes: BiomeRegistry

    init {
        val registriesFile = javaClass.classLoader.getResourceAsStream("registries/registries.json")!!
            .reader(Charsets.UTF_8)
            .readText()
        registries = JSON.decodeFromString(Registries.serializer(), registriesFile)

        val dimensionsFile = javaClass.classLoader.getResourceAsStream("registries/custom/dimensions.json")!!
            .reader(Charsets.UTF_8)
            .readText()
        dimensions = JSON.decodeFromString(DimensionRegistry.serializer(), dimensionsFile)

        val biomesFile = javaClass.classLoader.getResourceAsStream("registries/custom/biomes.json")!!
            .reader(Charsets.UTF_8)
            .readText()
        biomes = JSON.decodeFromString(BiomeRegistry.serializer(), biomesFile)
    }

    companion object {

        private val JSON = Json {}
    }
}