/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.pointer.Pointers
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.ItemStack

/**
 * Holder for metadata for an [ItemStack].
 */
public interface MetaHolder : Pointers {

    /**
     * Gets the value associated with the given [key], or null if there is no
     * value associated with the given [key].
     *
     * @param key the key
     * @return the value, or null if not present
     */
    public operator fun <V : Any> get(key: MetaKey<V>): V?

    /**
     * Sets the value associated with the given [key] to the provided [value].
     *
     * @param key the key
     * @param value the new value
     */
    public operator fun <V : Any> set(key: MetaKey<V>, value: V)

    /**
     * Returns if this holder contains metadata for the given [key].
     *
     * @param key the key
     * @return true if there is metadata for the key, false otherwise
     */
    public operator fun <V : Any> contains(key: MetaKey<V>): Boolean

    /**
     * Creates a copy of this meta holder.
     */
    @Contract("_ -> new", pure = true)
    public fun copy(): MetaHolder
}
