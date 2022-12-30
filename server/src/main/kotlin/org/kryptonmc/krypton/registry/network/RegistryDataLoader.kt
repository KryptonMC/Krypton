/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.registry.network

import com.google.gson.JsonParser
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.pack.resources.ResourceManager
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonSimpleRegistry
import org.kryptonmc.krypton.registry.WritableRegistry
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.resource.FileToIdConverter
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.Decoder
import org.kryptonmc.serialization.gson.GsonOps
import java.io.PrintWriter
import java.io.StringWriter
import java.util.stream.Collectors

object RegistryDataLoader {

    private val LOGGER = LogManager.getLogger()
    @JvmField
    val WORLD_GENERATION_REGISTRIES: List<RegistryData<*>> = ImmutableLists.of(
        RegistryData(ResourceKeys.DIMENSION_TYPE, KryptonDimensionType.DIRECT_CODEC),
        RegistryData(ResourceKeys.BIOME, KryptonBiome.DIRECT_CODEC),
        RegistryData(KryptonResourceKeys.CHAT_TYPE, ChatType.CODEC)
    )
    // TODO: Uncomment this when WorldDimension exists
//    @JvmField
//    val DIMENSION_REGISTRIES: List<RegistryData<*>> = ImmutableLists.of(KryptonResourceKeys.DIMENSION, WorldDimension.CODEC)

    @JvmStatic
    fun load(resourceManager: ResourceManager, registryAccess: RegistryAccess, registries: List<RegistryData<*>>): RegistryAccess.Frozen {
        val errors = HashMap<ResourceKey<*>, Exception>()
        val loaders = registries.map { it.create(errors) }
        val lookup = createContext(registryAccess, loaders)
        loaders.forEach { it.second.load(resourceManager, lookup) }
        loaders.forEach { (registry, _) ->
            try {
                registry.freeze()
            } catch (exception: Exception) {
                errors.put(registry.key, exception)
            }
        }
        if (errors.isNotEmpty()) {
            logErrors(errors)
            error("Failed to load registries due to above errors!")
        }
        return RegistryAccess.ImmutableImpl(loaders.map { it.first }).freeze()
    }

    @JvmStatic
    private fun createContext(registryAccess: RegistryAccess, loaders: List<Pair<WritableRegistry<*>, Loader>>): RegistryOps.RegistryInfoLookup {
        val registries = HashMap<ResourceKey<out Registry<*>>, RegistryOps.RegistryInfo<*>>()
        registryAccess.registries().forEach { registries.put(it.key, createInfoForContextRegistry(it.value)) }
        loaders.forEach { registries.put(it.first.key, createInfoForNewRegistry(it.first)) }
        return object : RegistryOps.RegistryInfoLookup {
            @Suppress("UNCHECKED_CAST")
            override fun <T> lookup(key: ResourceKey<out Registry<out T>>): RegistryOps.RegistryInfo<T>? =
                registries.get(key) as? RegistryOps.RegistryInfo<T>
        }
    }

    @JvmStatic
    private fun <T> createInfoForNewRegistry(registry: WritableRegistry<T>): RegistryOps.RegistryInfo<T> =
        RegistryOps.RegistryInfo(registry.asLookup(), registry.createRegistrationLookup())

    @JvmStatic
    private fun <T> createInfoForContextRegistry(registry: KryptonRegistry<T>): RegistryOps.RegistryInfo<T> =
        RegistryOps.RegistryInfo(registry.asLookup(), registry.asTagAddingLookup())

    @JvmStatic
    private fun logErrors(errors: Map<ResourceKey<*>, Exception>) {
        val result = StringWriter()
        val writer = PrintWriter(result)
        val unwrappedErrors = errors.entries.stream()
            .collect(Collectors.groupingBy({ it.key.registry }, Collectors.toMap({ it.key.location }, { it.value })))
        unwrappedErrors.entries.stream().sorted(java.util.Map.Entry.comparingByKey()).forEach { (key, exceptions) ->
            writer.printf("> Errors in registry %s:%n", key)
            exceptions.entries.stream().sorted(java.util.Map.Entry.comparingByKey()).forEach {
                writer.printf(">> Errors in element %s:%n", it.key)
                it.value.printStackTrace(writer)
            }
        }
        writer.flush()
        LOGGER.error("Registry loading errors:\n$result")
    }

    @JvmStatic
    private fun registryDirectoryPath(location: Key): String = location.value()

    @JvmStatic
    private fun <E> loadRegistryContents(lookup: RegistryOps.RegistryInfoLookup, resourceManager: ResourceManager, key: ResourceKey<out Registry<E>>,
                                         registry: WritableRegistry<E>, decoder: Decoder<E>, errors: MutableMap<ResourceKey<*>, Exception>) {
        val path = registryDirectoryPath(key.location)
        val converter = FileToIdConverter.json(path)
        val ops = RegistryOps.create(GsonOps.INSTANCE, lookup)

        converter.listMatchingResources(resourceManager).entries.forEach { (location, resource) ->
            val resourceKey = KryptonResourceKey.of(key, converter.fileToId(location))
            try {
                resource.openAsReader().use { reader ->
                    val json = JsonParser.parseReader(reader)
                    registry.register(resourceKey, decoder.read(json, ops).getOrThrow(false) {})
                }
            } catch (exception: Exception) {
                errors.put(resourceKey, IllegalStateException("Failed to parse $location from pack ${resource.sourcePackId()}!", exception))
            }
        }
    }

    fun interface Loader {

        fun load(resourceManager: ResourceManager, lookup: RegistryOps.RegistryInfoLookup)
    }

    @JvmRecord
    data class RegistryData<T>(val key: ResourceKey<out Registry<T>>, val elementCodec: Codec<T>) {

        fun create(errors: MutableMap<ResourceKey<*>, Exception>): Pair<WritableRegistry<*>, Loader> {
            val registry = KryptonSimpleRegistry.standard(key)
            val loader = Loader { manager, lookup -> loadRegistryContents(lookup, manager, key, registry, elementCodec, errors) }
            return Pair(registry, loader)
        }
    }
}
