/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.internal.annotations.CataloguedBy
import java.util.stream.Stream

/**
 * A holder for registry entries.
 *
 * @param T the registry entry type
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Registries::class)
public interface Registry<T> : Iterable<T> {

    /**
     * The registry key for this registry.
     */
    public val key: ResourceKey<out Registry<T>>

    /**
     * Gets the set of keys registered in this registry.
     *
     * The returned set is unmodifiable, meaning it will be updated by changes
     * in the registry, but cannot be modified directly.
     *
     * @return the registered key set
     */
    public fun keys(): Set<Key>

    /**
     * Gets the set of registry keys registered in this registry.
     *
     * The returned set is unmodifiable, meaning it will be updated by changes
     * in the registry, but cannot be modified directly.
     *
     * @return the registered registry key set
     */
    public fun registryKeys(): Set<ResourceKey<T>>

    /**
     * Gets the set of entries registered in this registry.
     *
     * The returned set is unmodifiable, meaning it will be updated by changes
     * in the registry, but cannot be modified directly.
     *
     * @return the registered entry set
     */
    public fun entries(): Set<Map.Entry<ResourceKey<T>, T>>

    /**
     * Gets the size of this registry, which is the number of registered
     * elements.
     *
     * @return the size of this registry
     */
    public fun size(): Int

    /**
     * Checks if the given [key] has a registered value in this registry.
     *
     * @param key the key
     * @return true if the key has a registered value, false otherwise
     */
    public fun containsKey(key: Key): Boolean

    /**
     * Checks if the given [key] has a registered value in this registry.
     *
     * @param key the resource key
     * @return true if the key has a registered value, false otherwise
     */
    public fun containsKey(key: ResourceKey<T>): Boolean

    /**
     * Gets a value by its namespaced [key], or null if there is no value
     * associated with the given [key].
     *
     * @param key the key
     * @return the value, or null if not present
     */
    public fun get(key: Key): T?

    /**
     * Gets a value by its resource [key], or null if there is no value
     * associated with the given [key].
     *
     * @param key the resource key
     * @return the value, or null if not present
     */
    public fun get(key: ResourceKey<T>): T?

    /**
     * Gets a namespaced [Key] by its [value], or null if there is no key
     * associated with the given [value].
     *
     * @param value the value
     * @return the key, or null if not present
     */
    public fun getKey(value: T): Key?

    /**
     * Gets the [ResourceKey] for the given [value], or null if there is no key
     * associated with the given [value].
     *
     * @param value the value
     * @return the resource key, or null if not present
     */
    public fun getResourceKey(value: T): ResourceKey<T>?

    /**
     * Gets the set of keys for all the registered tags in this registry.
     *
     * The returned set is unmodifiable, meaning it will be updated by changes
     * in the registry, but cannot be modified directly.
     *
     * @return the registered tag key set
     */
    public fun tagKeys(): Set<TagKey<T>>

    /**
     * Gets the set of keys for all the registered tags in this registry.
     *
     * The returned set is unmodifiable, meaning it will be updated by changes
     * in the registry, but cannot be modified directly.
     *
     * @return the registered tag key set
     */
    public fun tags(): Collection<TagSet<T>>

    /**
     * Gets the tag values for the given [key], or returns null if there are no
     * tag values associated with the given [key].
     *
     * @param key the tag key
     * @return the tag set encapsulating the values for the tag
     */
    public fun getTagValues(key: TagKey<T>): TagSet<T>?

    /**
     * Creates a new stream of the elements in this registry.
     *
     * @return a new stream of this registry
     */
    public fun stream(): Stream<T>
}
