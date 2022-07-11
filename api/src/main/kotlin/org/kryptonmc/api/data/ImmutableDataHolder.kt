/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import java.util.function.Function
import kotlin.properties.ReadOnlyProperty

/**
 * A data holder that cannot be mutated. Every write operation creates a new
 * copy of the holder with the requested changes.
 */
public interface ImmutableDataHolder<T : ImmutableDataHolder<T>> : DataHolder {

    /**
     * Creates a new data holder with the value associated with the given [key]
     * set to the given [value].
     *
     * @param key the key
     * @param value the value
     * @return a new data holder
     */
    public fun <E> set(key: Key<E>, value: E): T

    /**
     * Creates a new data holder with the value associated with the given [key]
     * removed from it.
     *
     * @param key the key
     * @return a new data holder
     */
    public fun remove(key: Key<*>): T

    /**
     * Creates a new data holder with the value associated with the given [key]
     * transformed by the given [transformation] function from its current
     * value to a new value.
     *
     * @param key the key
     * @param transformation the transformation function
     * @return a new data holder
     */
    @JvmSynthetic
    public fun <E> transform(key: Key<E>, transformation: (E) -> E): T

    /**
     * Creates a new data holder with the value associated with the given [key]
     * transformed by the given [transformation] function from its current
     * value to a new value.
     *
     * @param key the key
     * @param transformation the transformation function
     * @return a new data holder
     */
    public fun <E> transform(key: Key<E>, transformation: Function<E, E>): T = transform(key, transformation::apply)

    /**
     * Creates a new property delegate for the given [key].
     *
     * This can be used in a `by` clause to create a delegated property that
     * will read from this data holder.
     *
     * @param key the key
     * @return the property delegate
     */
    @JvmSynthetic
    public fun <E> delegate(key: Key<E>): ReadOnlyProperty<T, E?>
}
