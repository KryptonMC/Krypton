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
package org.kryptonmc.api.state

import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Something that is a possible state of something else.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface State<out S : State<S>> {

    /**
     * All of the properties that can be set on this state.
     */
    @get:JvmName("availableProperties")
    public val availableProperties: @Unmodifiable Set<Property<*>>

    /**
     * All of the properties that are currently set on this state.
     */
    @get:JvmName("properties")
    public val properties: @Unmodifiable Map<Property<*>, Comparable<*>>

    /**
     * Checks if the given [property] has a value on this state.
     *
     * @param property the property to check
     * @return true if this state has a value for the given property, false
     * otherwise
     */
    public fun hasProperty(property: Property<*>): Boolean

    /**
     * Gets the value for the given [property], or returns null if there is no
     * value for the given [property].
     *
     * @param T the property type
     * @param property the property
     * @return the value, or null if not present
     */
    public fun <T : Comparable<T>> getProperty(property: Property<T>): T?

    /**
     * Gets the value for the given [property], or throws an
     * [IllegalArgumentException] if there is no value for the given
     * [property].
     *
     * @param T the property type
     * @param property the property
     * @return the value
     * @throws IllegalArgumentException if there is no value for the property
     */
    public fun <T : Comparable<T>> requireProperty(property: Property<T>): T

    /**
     * Sets the value for the given [property] to the given [value] and returns
     * the resulting state.
     *
     * As states are immutable, this will return a different state where the
     * given [property] has the given [value].
     *
     * @param T the property type
     * @param property the property
     * @param value the value
     * @return the state with the property set to the value
     */
    public fun <T : Comparable<T>> setProperty(property: Property<T>, value: T): S
}
