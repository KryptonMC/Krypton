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
package org.kryptonmc.krypton.tags

import com.google.common.collect.ImmutableMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.util.logger

class TagContainer(private val collections: Map<ResourceKey<out Registry<*>>, TagCollection<*>>) {

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getOrEmpty(key: ResourceKey<out Registry<T>>) = collections.getOrDefault(key, TagCollection.empty<T>()) as TagCollection<T>

    fun <T : Any, E : Exception> getOrThrow(key: ResourceKey<out Registry<T>>, id: Key, error: (Key) -> E) = (get(key) ?: throw error(id))[id] ?: throw error(id)

    fun <T : Any, E : Exception> getOrThrow(key: ResourceKey<out Registry<T>>, tag: Tag<T>, error: () -> E) = (get(key) ?: throw error())[tag] ?: throw error()

    fun bindToGlobal() = StaticTags.resetAll(this)

    fun write(holder: RegistryHolder): Map<ResourceKey<out Registry<*>>, TagCollection.NetworkPayload> {
        val map = mutableMapOf<ResourceKey<out Registry<*>>, TagCollection.NetworkPayload>()
        getAll(object : CollectionConsumer {
            override fun <T : Any> invoke(key: ResourceKey<out Registry<T>>, collection: TagCollection<T>) {
                val registry = holder.registry(key) ?: return LOGGER.error("Unknown registry $key")
                map[key] = collection.write(registry)
            }
        })
        return map
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> get(key: ResourceKey<out Registry<T>>) = collections[key] as? TagCollection<T>

    @Suppress("UNCHECKED_CAST")
    private fun getAll(consumer: CollectionConsumer) = collections.forEach { (key, value) ->
        consumer(key as ResourceKey<out Registry<Any>>, value as TagCollection<Any>)
    }

    class Builder {

        private val result = ImmutableMap.builder<ResourceKey<out Registry<*>>, TagCollection<*>>()

        fun <T : Any> add(key: ResourceKey<out Registry<T>>, collection: TagCollection<T>) = apply {
            result.put(key, collection)
        }

        fun build() = TagContainer(result.build())
    }

    interface CollectionConsumer {

        operator fun <T : Any> invoke(key: ResourceKey<out Registry<T>>, collection: TagCollection<T>)
    }

    companion object {

        private val LOGGER = logger<TagContainer>()
        val EMPTY = TagContainer(emptyMap())
    }
}
