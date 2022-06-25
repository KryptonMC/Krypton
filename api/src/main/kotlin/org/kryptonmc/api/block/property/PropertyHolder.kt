/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.property

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.Contract

/**
 * Represents something that can hold properties.
 *
 * @param T the recursive type of this holder, used to morph return types
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PropertyHolder<out T : PropertyHolder<T>> {

    /**
     * All of the property keys available for use by this holder.
     */
    @get:JvmName("availableProperties")
    public val availableProperties: Set<Property<*>>

    /**
     * All the properties that are being held by this property holder.
     */
    // TODO: Figure out what to do here.
    // The problem is that having the properties be a string map exposes the
    // string-based implementation, and makes it difficult for us to switch to
    // other options if necessary in the future. It also doesn't really make
    // sense when you look at the rest of the property API, and the fact that
    // it's filled with wrappers and high-level abstractions. This feels out of
    // place, but replacing it with a map of Property<*> to Comparable<*> will
    // require some backend work, and may degrade performance.
    @get:JvmName("properties")
    public val properties: Map<String, String>

    /**
     * Returns true if the specified [key] is in this holder, false otherwise.
     *
     * @param key the key
     * @return true if the key is in this holder, false otherwise
     */
    public operator fun contains(key: Property<*>): Boolean

    /**
     * Gets the value associated with the given property [key], or returns null if
     * there is no value associated with the given property [key] (usually when
     * the property given is not supported by this holder).
     *
     * @param key the key
     * @return the value for the key, or null if not present
     */
    public operator fun <V : Comparable<V>> get(key: Property<V>): V?

    /**
     * Sets the value associated with the given property [key] to the given
     * [value].
     *
     * As this holder is immutable, this returns the newly created holder that
     * has the requested changes.
     *
     * @param key the property key
     * @return a new holder with the value of the key set to the given value
     */
    @Contract("_ -> new", pure = true)
    public fun <V : Comparable<V>> set(key: Property<V>, value: V): T

    /**
     * A base builder for building property holders.
     */
    public interface Builder<B : Builder<B, T>, T : PropertyHolder<T>> : AbstractBuilder<T> {

        /**
         * Adds the given property [key] to the list of available properties
         * for the property holder and sets the value of the property to the
         * given [value] in the properties map.
         *
         * @param key the key
         * @param value the value
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun <V : Comparable<V>> property(key: Property<V>, value: V): B
    }
}
