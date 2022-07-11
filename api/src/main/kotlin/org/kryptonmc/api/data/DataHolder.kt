/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

/**
 * An object that holds data accessible with keys.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DataHolder {

    /**
     * All the keys that are supported by this data holder.
     */
    @get:JvmName("keys")
    public val keys: Set<Key<*>>

    /**
     * Checks if the given [key] is supported by this data holder, meaning that
     * data associated with the key can be applied to this data holder.
     *
     * @param key the key
     * @return true if the key is supported by this holder, false otherwise
     */
    public fun supports(key: Key<*>): Boolean

    /**
     * Gets the value associated with the given [key], or returns null if there
     * is no value associated with the given [key].
     *
     * @param key the key
     * @return the value, or null if not present
     */
    public operator fun <E> get(key: Key<E>): E?

    /**
     * Gets the value associated with the given [key], or returns the given
     * [default] value if there is no value associated with the given [key].
     *
     * @param key the key
     * @param default the default value
     * @return the value, or the default if not present
     */
    public fun <E> getOrDefault(key: Key<E>, default: E): E = get(key) ?: default
}
