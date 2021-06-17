/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.IdMap
import java.util.Optional
import kotlin.random.Random

/**
 * A holder for registry entries, which holds the following bi-directional mappings:
 * - [T] <-> [Int] (value <-> ID)
 * - [Key] <-> [T] (key <-> value)
 * - [RegistryKey] <-> [T] (registry key <-> value)
 *
 * Register values to registries using [Registries.register], this base class is read-only.
 *
 * This class is also sealed, so we can assume it is a [WritableRegistry] in [Registries],
 * but only provide read access to users.
 *
 * @param key the registry key for this registry
 */
sealed class Registry<T>(val key: RegistryKey<out Registry<T>>) : IdMap<T>, Map<RegistryKey<T>, T> {

    /**
     * The set of [Key]s of this registry. This is called keySet because the
     * [keys] for this map is the set of [RegistryKey]s, not the set of [Key]s.
     */
    abstract val keySet: Set<Key>

    /**
     * Gets the key for the given [value], or null if there is no key for this
     * value in this registry.
     *
     * @param value the value
     * @return the key, or null if not present
     */
    abstract fun getKey(value: T): Key?

    /**
     * Gets the registry key for the given [value], or null if there is no
     * registry key for this value in this registry (the value is not registered).
     *
     * @param value the value
     * @return the registry key, or null if not present
     */
    abstract fun getRegistryKey(value: T): RegistryKey<T>?

    /**
     * Gets the value for the given [key], or returns null if there isn't a
     * value for the given key (not registered).
     *
     * @param key the key
     * @return the value, or null if not present
     */
    abstract operator fun get(key: Key): T?

    /**
     * Returns true if the given [key] is registered in this registry,
     * false otherwise.
     *
     * @param key the key
     * @return true if the key is registered, false otherwise
     */
    abstract fun containsKey(key: Key): Boolean

    /**
     * Gets a random value from this registry using the given [random]
     * instance to generate a random index, or returns null if this
     * registry has no registered values (it is empty).
     *
     * @param random the random instance for generating the index
     * @return a random value, or null if this registry is empty
     */
    abstract fun getRandom(random: Random): T?

    /**
     * Gets the value for the given [key], as an [Optional].
     *
     * @param key the registry key
     * @return the value, as an [Optional]
     */
    fun getOptional(key: RegistryKey<T>) = Optional.ofNullable(get(key))

    /**
     * Gets the value for the given [key], as an [Optional].
     *
     * @param key the registry key
     * @return the value, as an [Optional]
     */
    open fun getOptional(key: Key) = Optional.ofNullable(get(key))

    /**
     * Gets the value for the given [key], or throws an [IllegalStateException]
     * if there is no value for the given [key] registered in this registry.
     *
     * @param key the key
     * @return the value
     * @throws IllegalStateException if there is no value for the [key]
     */
    fun getOrThrow(key: RegistryKey<T>) = get(key) ?: error("Missing $key!")

    override fun toString() = "Registry(key=$key)"
}
