/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.data

import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.properties.ReadWriteProperty

/**
 * A data holder that can be mutated.
 */
public interface MutableDataHolder : DataHolder {

    /**
     * Sets the value associated with the given [key] to the given [value] on
     * this data holder.
     *
     * @param key the key
     * @param value the value
     */
    public operator fun <E> set(key: Key<E>, value: E)

    /**
     * Removes the value associated with the given [key] from this data
     * holder.
     *
     * @param key the key
     */
    public fun remove(key: Key<*>)

    /**
     * Gets the collection associated with the given [key] and applies the
     * given [mutator] to it.
     *
     * @param key the key
     * @param mutator the mutator to apply
     */
    public fun <E> editCollection(key: Key<Collection<E>>, mutator: Consumer<MutableCollection<E>>)

    /**
     * Adds the given [value] to the collection of values associated with the
     * given [key] on this data holder.
     *
     * @param key the key
     * @param value the value to add
     */
    public fun <E> add(key: Key<Collection<E>>, value: E)

    /**
     * Adds the given [values] to the collection of values associated with the
     * given [key] on this data holder.
     *
     * @param key the key
     * @param values the values to add
     */
    public fun <E> addAll(key: Key<Collection<E>>, values: Collection<E>)

    /**
     * Removes the given [value] from the collection of values associated
     * with the given [key] on this data holder.
     *
     * @param key the key
     * @param value the value to remove
     */
    public fun <E> remove(key: Key<Collection<E>>, value: E)

    /**
     * Removes all the given [values] from the collection of values associated
     * with the given [key] on this data holder.
     *
     * @param key the key
     * @param values the values to remove
     */
    public fun <E> removeAll(key: Key<Collection<E>>, values: Collection<E>)

    /**
     * Removes all the values that match the given [predicate] from the
     * collection associated with the given [key] on this data holder.
     *
     * @param key the key
     * @param predicate the predicate to match values against
     */
    public fun <E> removeAll(key: Key<Collection<E>>, predicate: Predicate<E>)

    /**
     * Gets the map associated with the given [key] and applies the given
     * [mutator] to it.
     *
     * @param key the key
     * @param mutator the mutator to apply
     */
    public fun <K, V> editMap(key: Key<Map<K, V>>, mutator: Consumer<MutableMap<K, V>>)

    /**
     * Puts the given [mapValue] associated with the given [mapKey] in to the
     * map associated with the given [key] on this data holder.
     *
     * @param key the key
     * @param mapKey the key in the map
     * @param mapValue the value in the map
     */
    public fun <K, V> put(key: Key<Map<K, V>>, mapKey: K, mapValue: V)

    /**
     * Puts the given [values] in to the map associated with the given [key] on
     * this data holder.
     *
     * @param key the key
     * @param values the values
     */
    public fun <K, V> putAll(key: Key<Map<K, V>>, values: Map<K, V>)

    /**
     * Removes the value associated with the given [mapKey] in the map
     * associated with the given [key] on this data holder.
     *
     * @param key the key
     * @param mapKey the key to remove from the map
     */
    public fun <K, V> removeKey(key: Key<Map<K, V>>, mapKey: K)

    /**
     * Removes all the given [values] from the map associated with the given
     * [key] on this data holder.
     */
    public fun <K, V> removeAll(key: Key<Map<K, V>>, values: Map<K, V>)

    /**
     * Creates a new property delegate for the given [key].
     *
     * This can be used in a `by` clause to create a delegated property that
     * will read from and write to this data holder.
     *
     * @param key the key
     * @return the property delegate
     */
    @JvmSynthetic
    public fun <E> delegate(key: Key<E>): ReadWriteProperty<MutableDataHolder, E?>
}
