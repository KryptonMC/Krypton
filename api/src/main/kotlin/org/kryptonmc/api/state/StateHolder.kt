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
