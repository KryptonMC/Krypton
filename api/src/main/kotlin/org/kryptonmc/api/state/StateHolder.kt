/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.state

import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Something that can be in some state.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface StateHolder<out S : State<S>> {

    /**
     * All of the states that this holder can be in.
     */
    @get:JvmName("states")
    public val states: @Unmodifiable List<S>

    /**
     * All of the properties that can be set on the states of this holder.
     */
    @get:JvmName("availableProperties")
    public val availableProperties: @Unmodifiable Collection<Property<*>>

    /**
     * The default state that this holder will appear in.
     */
    @get:JvmName("defaultState")
    public val defaultState: S

    /**
     * Gets the property with the given [name] for this holder, or returns null
     * if there is no property with the given [name] for this holder.
     *
     * @param name the name of the property
     * @return the property, or null if not present
     */
    public fun getProperty(name: String): Property<*>?
}
