/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.registry

import com.google.gson.JsonParseException
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.UnboundedMapCodec
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.resource.MemoryResources
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import java.util.Optional

class RegistryHolder(val registries: Map<out ResourceKey<out Registry<*>>, KryptonRegistry<*>>) {

    @Suppress("UNCHECKED_CAST")
    constructor() : this(REGISTRIES.keys.associateWith { KryptonRegistry(it as ResourceKey<out Registry<Any>>) })

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> ownedRegistry(key: ResourceKey<out Registry<out E>>): Registry<E>? = registries[key] as? Registry<E>

    fun <E : Any> ownedRegistryOrThrow(key: ResourceKey<out Registry<out E>>) = ownedRegistry(key) ?: error("Missing required registry with key $key!")

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> registry(key: ResourceKey<out Registry<out E>>): Registry<E>? = ownedRegistry(key) ?: Registries.PARENT[key] as? Registry<E>

    fun <E : Any> registryOrThrow(key: ResourceKey<out Registry<out E>>) = registry(key) ?: error("Missing required registry with key $key!")

    fun load(ops: RegistryReadOps<*>) = REGISTRIES.values.forEach { read(ops, it) }

    private fun <E : Any> read(ops: RegistryReadOps<*>, data: RegistryData<E>) {
        val key = data.key
        val owned = ownedRegistryOrThrow(key) as KryptonRegistry<E>
        ops.decodeElements(owned, key, data.codec).error().ifPresent { throw JsonParseException("Error loading registry data: ${it.message()}") }
    }

    private fun <R : Registry<*>> copyBuiltin(key: ResourceKey<R>) = copy(Registries.PARENT[key] ?: error("Missing required key $key!"))

    private fun <E : Any> copy(registry: Registry<E>) {
        val owned = ownedRegistryOrThrow(registry.key)
        registry.forEach { (key, value) -> owned.register(registry.idOf(value), key, value) }
    }

    private fun <E : Any> addBuiltins(resources: MemoryResources, data: RegistryData<E>) {
        val key = data.key
        val shouldAdd = key != InternalResourceKeys.NOISE_GENERATOR_SETTINGS && key != ResourceKeys.DIMENSION_TYPE
        val registry = BUILTIN.registryOrThrow(key)
        val owned = ownedRegistryOrThrow(key)
        registry.forEach { (key, value) -> if (shouldAdd) resources.add(BUILTIN, key, data.codec, registry.idOf(value), value) else owned.register(registry.idOf(value), key, value) }
    }

    companion object {

        val REGISTRIES: Map<ResourceKey<out Registry<*>>, RegistryData<*>> = mapOf(
            ResourceKeys.DIMENSION_TYPE to RegistryData(ResourceKeys.DIMENSION_TYPE, DimensionTypes.DIRECT_CODEC, DimensionTypes.DIRECT_CODEC),
            InternalResourceKeys.BIOME to RegistryData(InternalResourceKeys.BIOME, KryptonBiome.DIRECT_CODEC, KryptonBiome.DIRECT_CODEC),
            InternalResourceKeys.NOISE_GENERATOR_SETTINGS to RegistryData(InternalResourceKeys.NOISE_GENERATOR_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC, null)
        )
        private val BUILTIN = RegistryHolder().apply {
            DimensionTypes.registerBuiltins(this)
            REGISTRIES.keys.asSequence().filter { it != ResourceKeys.DIMENSION_TYPE }.forEach { copyBuiltin(it) }
        }
        val NETWORK_CODEC: Codec<RegistryHolder> = networkCodec<Any>()

        fun builtin(): RegistryHolder = RegistryHolder().apply {
            val resources = MemoryResources()
            REGISTRIES.values.forEach { addBuiltins(resources, it) }
            RegistryReadOps.createAndLoad(JsonOps.INSTANCE, resources, this)
        }

        private fun <E : Any> networkCodec(): Codec<RegistryHolder> {
            val registryKeyCodec: Codec<ResourceKey<out Registry<E>>> = KEY_CODEC.xmap(::createKey, ResourceKey<*>::location)
            val registryCodec: Codec<KryptonRegistry<E>> = registryKeyCodec.partialDispatch(
                "type",
                { DataResult.success(it.key) },
                { key -> key.networkCodec().map { key.networkCodec(it) } }
            )
            return Codec.unboundedMap(registryKeyCodec, registryCodec).capture()
        }
    }
}

private fun <T : Any> createKey(key: Key): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.MINECRAFT, key)

@Suppress("UNCHECKED_CAST")
private fun <K : ResourceKey<out Registry<*>>, V : KryptonRegistry<*>> UnboundedMapCodec<K, V>.capture() = xmap(::RegistryHolder) { holder ->
    holder.registries.entries.filter { RegistryHolder.REGISTRIES[it.key]!!.shouldSend }.associate { (it.key to it.value as V) as Pair<K, V> }
}

@Suppress("UNCHECKED_CAST")
private fun <E : Any> ResourceKey<out Registry<E>>.networkCodec(): DataResult<out Codec<E>> = Optional.ofNullable(RegistryHolder.REGISTRIES[this])
    .map { it.networkCodec }
    .map { DataResult.success(it) }
    .orElseGet { DataResult.error("Unknown or not serializable registry $this!") } as DataResult<out Codec<E>>

data class RegistryData<E : Any>(
    val key: ResourceKey<out Registry<E>>,
    val codec: Codec<E>,
    val networkCodec: Codec<E>?
) {

    val shouldSend = networkCodec != null
}
