/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.api.util.CataloguedBy
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
     * All the keys in this registry.
     */
    @get:JvmName("keySet")
    public val keys: Set<Key>

    /**
     * All the registry keys in this registry.
     */
    @get:JvmName("registryKeySet")
    public val registryKeys: Set<ResourceKey<T>>

    /**
     * All the entries in this registry.
     */
    @get:JvmName("entrySet")
    public val entries: Set<Map.Entry<ResourceKey<T>, T>>

    /**
     * The size of this registry.
     */
    @get:JvmName("size")
    public val size: Int

    /**
     * All the tag keys in this registry.
     */
    public val tagKeys: Set<TagKey<T>>

    /**
     * All the tags in this registry.
     */
    public val tags: Map<TagKey<T>, TagSet<T>>

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
     * Gets the tag values for the given [key], or returns null if there are no
     * tag values associated with the given [key].
     *
     * @param key the tag key
     * @return the tag set encapsulating the values for the tag
     */
    public fun getTag(key: TagKey<T>): TagSet<T>?

    /**
     * Creates a new stream of the elements in this registry.
     *
     * @return a new stream of this registry
     */
    public fun stream(): Stream<T>
}
