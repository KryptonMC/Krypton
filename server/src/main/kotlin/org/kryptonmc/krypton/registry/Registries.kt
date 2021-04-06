package org.kryptonmc.krypton.registry

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry
import org.kryptonmc.krypton.registry.impl.MappedRegistry
import org.kryptonmc.krypton.registry.json.RegistryBlock
import org.kryptonmc.krypton.registry.json.RegistryEntry

private val REGISTRIES: Map<NamespacedKey, RegistryEntry> =
    Json.decodeFromString(registryData("registries/registries.json"))

private val STATE_REGISTRY: Map<NamespacedKey, RegistryBlock> =
    Json.decodeFromString(registryData("registries/blocks.json"))

object Registries {

    val BLOCKS = MappedRegistry(REGISTRIES.getValue(NamespacedKey(value = "block")).entries.mapValues { it.value.id })
    val FLUIDS = MappedRegistry(REGISTRIES.getValue(NamespacedKey(value = "fluid")).entries.mapValues { it.value.id })
    val ITEMS = MappedRegistry(REGISTRIES.getValue(NamespacedKey(value = "item")).entries.mapValues { it.value.id })
    val ENTITY_TYPES = MappedRegistry(REGISTRIES.getValue(NamespacedKey(value = "entity_type")).entries.mapValues { it.value.id })

    val STATES = STATE_REGISTRY

    val BIOMES = Json.decodeFromString<BiomeRegistry>(registryData("registries/custom/biomes.json"))
    val DIMENSIONS = Json.decodeFromString<DimensionRegistry>(registryData("registries/custom/dimensions.json"))
}

private fun registryData(path: String) = (Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    ?: throw IllegalStateException("$path not on classpath! Something has gone horribly wrong!"))
    .reader()
    .readText()