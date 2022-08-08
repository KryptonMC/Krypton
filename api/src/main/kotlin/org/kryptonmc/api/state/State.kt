/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.state

/**
 * Something that is a possible state of something else.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface State<out S : State<S>> {

    /**
     * All of the properties that can be set on this state.
     */
    @get:JvmName("availableProperties")
    public val availableProperties: Set<Property<*>>

    /**
     * All of the properties that are currently set on this state.
     */
    @get:JvmName("properties")
    public val properties: Map<Property<*>, Comparable<*>>

    /**
     * Checks if the given [property] has a value on this state.
     */
    public operator fun contains(property: Property<*>): Boolean

    /**
     * Gets the value for the given [property], or returns null if there is no
     * value for the given [property].
     *
     * @param property the property
     * @return the value, or null if not present
     */
    public operator fun <T : Comparable<T>> get(property: Property<T>): T?

    /**
     * Gets the value for the given [property], or throws an
     * [IllegalArgumentException] if there is no value for the given
     * [property].
     *
     * @param property the property
     * @return the value
     * @throws IllegalArgumentException if there is no value for the property
     */
    public fun <T : Comparable<T>> require(property: Property<T>): T

    /**
     * Sets the value for the given [property] to the given [value] and returns
     * the resulting state.
     *
     * As states are immutable, this will return a different state where the
     * given [property] has the given [value].
     *
     * @param property the property
     * @param value the value
     * @return the state with the property set to the value
     */
    public fun <T : Comparable<T>> set(property: Property<T>, value: T): S
}
