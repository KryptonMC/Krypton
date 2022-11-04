/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

    override fun contains(property: Property<*>): Boolean = asState().contains(property.downcast())

    override fun <T : Comparable<T>> get(property: Property<T>): T? = asState().get(property.downcast())

    override fun <T : Comparable<T>> require(property: Property<T>): T = asState().require(property.downcast())

    override fun <T : Comparable<T>> set(property: Property<T>, value: T): S = asState().set(property.downcast(), value) as S
}
