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
package org.kryptonmc.krypton.state

import org.kryptonmc.api.state.Property
import org.kryptonmc.api.state.State
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.state.property.downcast

/**
 * This is a big hack that is used by state implementors to allow the
 * KryptonState generic to be a KryptonState instance whilst avoiding the clash
 * in supertype generic types.
 */
interface StateDelegate<S : State<S>, K : KryptonState<*, K>> : State<S> {

    override val availableProperties: Set<KryptonProperty<*>>
        get() = asState().values.keys
    override val properties: Map<Property<*>, Comparable<*>>
        @Suppress("UNCHECKED_CAST") get() = asState().values as Map<Property<*>, Comparable<*>>

    fun asState(): K

    override fun hasProperty(property: Property<*>): Boolean = asState().hasProperty(property.downcast())

    override fun <T : Comparable<T>> getProperty(property: Property<T>): T? = asState().getProperty(property.downcast())

    override fun <T : Comparable<T>> requireProperty(property: Property<T>): T = asState().requireProperty(property.downcast())

    @Suppress("UNCHECKED_CAST")
    override fun <T : Comparable<T>> setProperty(property: Property<T>, value: T): S = asState().setProperty(property.downcast(), value) as S
}
