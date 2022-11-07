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
import org.kryptonmc.api.util.CataloguedBy

/**
 * A holder for registry entries.
 *
 * @param T the registry entry type
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Registries::class)
public interface Registry<T> : Map<ResourceKey<T>, T> {

    /**
     * The registry key for this registry.
     */
    public val key: ResourceKey<out Registry<T>>

    /**
     * The set of child keys.
     */
    @get:JvmName("keys")
    public val keySet: Set<Key>

    /**
     * Checks if the given [key] has a registered value in this registry.
     *
     * @param key the key
     * @return true if the key has a registered value, false otherwise
     */
    public fun contains(key: Key): Boolean

    /**
     * Gets a value by its namespaced [key], or null if there is no value
     * associated with the given [key].
     *
     * @param key the key
     * @return the value, or null if not present
     */
    public fun get(key: Key): T?

    /**
     * Gets a namespaced [Key] by its [value], or null if there is no key
     * associated with the given [value].
     *
     * @param value the value
     * @return the key, or null if not present
     */
    public fun get(value: T): Key?

    /**
     * Gets the [ResourceKey] for the given [value], or null if there is no key
     * associated with the given [value].
     *
     * @param value the value
     * @return the resource key, or null if not present
     */
    public fun resourceKey(value: T): ResourceKey<T>?

    /**
     * Registers a new value to this registry with the given registry [key]
     * and value.
     *
     * @param V the type of value
     * @param key the registry key
     * @param value the value
     * @return the value
     */
    public fun <V : T> register(key: ResourceKey<T>, value: V): V

    /**
     * Registers a new value to this registry with the given registry [key]
     * and value.
     *
     * @param V the type of value
     * @param key the key
     * @param value the value
     */
    public fun <V : T> register(key: String, value: V): V = register(Key.key(key), value)

    /**
     * Registers a new value to this registry with the given registry [key]
     * and value.
     *
     * @param V the type of value
     * @param key the registry key
     * @param value the value
     * @return the value
     */
    public fun <V : T> register(key: Key, value: V): V
}
