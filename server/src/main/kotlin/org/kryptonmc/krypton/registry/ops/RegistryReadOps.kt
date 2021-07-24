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
package org.kryptonmc.krypton.registry.ops

import com.google.common.base.Suppliers
import com.google.gson.JsonElement
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import com.mojang.serialization.Lifecycle
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.resource.Resources
import org.kryptonmc.krypton.util.DelegatingOps
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.logger
import java.util.IdentityHashMap
import java.util.function.Supplier

/**
 * Please feel free to blame Java generics for the amount of UNCHECKED_CAST
 * suppression in this class.
 *
 * I probably could have reduced it, but there comes a point where one has
 * just had enough.
 */
class RegistryReadOps<T : Any> private constructor(
    delegate: DynamicOps<T>,
    private val resources: Resources,
    private val registryHolder: RegistryHolder,
    private val readCache: IdentityHashMap<ResourceKey<out Registry<*>>, ReadCache<*>> = IdentityHashMap()
) : DelegatingOps<T>(delegate) {

    @Suppress("UNCHECKED_CAST")
    private val jsonOps: RegistryReadOps<JsonElement> = if (delegate == JsonOps.INSTANCE) {
        this as RegistryReadOps<JsonElement>
    } else {
        RegistryReadOps(JsonOps.INSTANCE, resources, registryHolder, readCache)
    }

    fun <E : Any> registry(key: ResourceKey<out Registry<E>>): DataResult<Registry<E>> = registryHolder.ownedRegistry(key)?.let { DataResult.success(it) } ?: DataResult.error("Unknown registry $key!")

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> decodeElement(input: T, registryKey: ResourceKey<out Registry<E>>, elementCodec: Codec<E>, allowInline: Boolean): DataResult<Pair<Supplier<E>, T>> {
        val registry = registryHolder.ownedRegistry(registryKey) ?: return DataResult.error("Unknown registry $registryKey!")
        val dataResult = KEY_CODEC.decode(delegate, input)
        if (!dataResult.result().isPresent) return if (!allowInline) DataResult.error("Inline definitions are not allowed here!") else elementCodec.decode(this, input).map { pair -> pair.mapFirst { Supplier { it } } }
        val result = dataResult.result().get()
        val key = result.first
        return readAndRegister(registryKey, registry, elementCodec, key).map { Pair.of(it, result.second) as Pair<Supplier<E>, T> }
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> decodeElements(registry: KryptonRegistry<E>, registryKey: ResourceKey<out Registry<E>>, elementCodec: Codec<E>): DataResult<KryptonRegistry<E>> {
        val resourceList = resources.list(registryKey)
        var result = DataResult.success(registry, Lifecycle.stable())
        val prefix = registryKey.location.value() + "/"
        resourceList.forEach {
            val value = it.value()
            if (!value.endsWith(JSON_SUFFIX)) {
                LOGGER.warn("Resource $it is not a JSON file! Skipping...")
                return@forEach
            }
            if (!value.startsWith(prefix)) {
                LOGGER.warn("Resource $it does not start with a registry name prefix! Skipping...")
                return@forEach
            }
            val path = value.substring(prefix.length, value.length - JSON_SUFFIX.length)
            val key = key(it.namespace(), path)
            result = result.flatMap { resultRegistry -> readAndRegister(registryKey, resultRegistry, elementCodec, key).map { resultRegistry } }
        }
        return result.setPartial(registry)
    }

    // Do not even begin to even look at this, or try to understand what it does,
    // just know that it works and move on, because this way lies madness...
    @Suppress("UNCHECKED_CAST")
    private fun <E : Any> readAndRegister(registryKey: ResourceKey<out Registry<E>>, registry: Registry<E>, elementCodec: Codec<E>, key: Key): DataResult<Supplier<E>> {
        val resourceKey = ResourceKey.of(registryKey, key)
        val readCache = readCache(registryKey)
        val dataResult = readCache.values[resourceKey]
        if (dataResult != null) return dataResult
        val supplier = Suppliers.memoize { registry[resourceKey] ?: throw RuntimeException("Error during recursive registry parsing, element $resourceKey resolved too early!") }
        readCache.values[resourceKey] = DataResult.success(supplier)
        val optionalParsed = resources.parseJson(jsonOps, registryKey, resourceKey, elementCodec)
        val result = if (!optionalParsed.isPresent) DataResult.success(object : Supplier<E> {
            override fun get() = registry[resourceKey]!!
            override fun toString() = resourceKey.toString()
        }) else {
            val parsedResult = optionalParsed.get()
            val parsed = parsedResult.result()
            if (parsed.isPresent) (registry as KryptonRegistry<E>).registerOrOverride(parsed.get().second, resourceKey, parsed.get().first)
            parsedResult.map { Supplier { registry[resourceKey] } }
        }
        readCache.values[resourceKey] = result as DataResult<Supplier<E>>?
        return result
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : Any> readCache(key: ResourceKey<out Registry<E>>) = readCache.getOrPut(key) { ReadCache<E>() } as ReadCache<E>

    companion object {

        private const val JSON_SUFFIX = ".json"
        private val LOGGER = logger<RegistryReadOps<*>>()

        fun <T : Any> createAndLoad(delegate: DynamicOps<T>, manager: ResourceManager, holder: RegistryHolder) = createAndLoad(delegate, Resources.forManager(manager), holder)

        fun <T : Any> createAndLoad(delegate: DynamicOps<T>, resources: Resources, holder: RegistryHolder) = RegistryReadOps(delegate, resources, holder).apply {
            holder.load(this)
        }
    }
}

private class ReadCache<E : Any> {

    val values = IdentityHashMap<ResourceKey<E>, DataResult<Supplier<E>>>()
}
