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
import org.kryptonmc.api.state.StateHolder
import org.kryptonmc.krypton.state.property.KryptonProperty

interface StateHolderDelegate<out S : State<S>, K : KryptonState<*, K>> : StateHolder<S> {

    val stateDefinition: StateDefinition<*, K>
    override val states: List<S>
        @Suppress("UNCHECKED_CAST") get() = stateDefinition.states as List<S>
    override val availableProperties: Collection<KryptonProperty<*>>
        get() = stateDefinition.properties

    override fun getProperty(name: String): Property<*>? = stateDefinition.getProperty(name)
}
