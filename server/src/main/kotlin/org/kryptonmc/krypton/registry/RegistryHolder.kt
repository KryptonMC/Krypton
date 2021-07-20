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

import com.mojang.serialization.Codec
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.dimension.DimensionTypes

class RegistryHolder(private val registries: Map<out ResourceKey<out Registry<*>>, KryptonRegistry<*>>) {

    constructor() : this(REGISTRIES.keys.associateWith { KryptonRegistry(it) })

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> ownedRegistry(key: ResourceKey<out Registry<out E>>): Registry<E>? = registries[key] as? Registry<E>

    fun <E : Any> ownedRegistryOrThrow(key: ResourceKey<out Registry<out E>>) = ownedRegistry(key) ?: error("Missing required registry with key $key!")

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> registry(key: ResourceKey<out Registry<out E>>): Registry<E>? = ownedRegistry(key) ?: Registries.PARENT[key] as? Registry<E>

    fun <E : Any> registryOrThrow(key: ResourceKey<out Registry<out E>>) = registry(key) ?: error("Missing required registry with key $key!")

    private fun <R : Registry<*>> copyBuiltin(key: ResourceKey<R>) = copy(Registries.PARENT[key] ?: error("Missing required key $key!"))

    private fun <E : Any> copy(registry: Registry<E>) {
        val owned = ownedRegistryOrThrow(registry.key)
        registry.forEach { (key, value) -> owned.register(registry.idOf(value), key, value) }
    }

    companion object {

        private val LOGGER = logger<RegistryHolder>()
        private val REGISTRIES = mapOf(
            ResourceKeys.DIMENSION_TYPE to RegistryData(ResourceKeys.DIMENSION_TYPE, DimensionTypes.DIRECT_CODEC, DimensionTypes.DIRECT_CODEC),
            // TODO: Add biomes to this list
        )
        private val BUILTIN = RegistryHolder().apply {
            DimensionTypes.registerBuiltins(this)
            REGISTRIES.keys.asSequence().filter { it != ResourceKeys.DIMENSION_TYPE }.forEach { copyBuiltin(it) }
        }
    }
}

private data class RegistryData<E : Any>(
    val key: ResourceKey<out Registry<E>>,
    val codec: Codec<E>,
    val networkCodec: Codec<E>?
) {

    val shouldSend = networkCodec != null
}
