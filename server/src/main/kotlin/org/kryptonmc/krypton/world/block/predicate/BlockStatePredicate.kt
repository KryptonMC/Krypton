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
package org.kryptonmc.krypton.world.block.predicate

import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import java.util.function.Predicate

class BlockStatePredicate private constructor(
    private val definition: StateDefinition<KryptonBlock, KryptonBlockState>
) : Predicate<KryptonBlockState?> {

    private val properties = HashMap<KryptonProperty<*>, Predicate<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun <V : Comparable<V>> where(property: KryptonProperty<V>, predicate: Predicate<V>): BlockStatePredicate = apply {
        require(definition.properties.contains(property)) { "$definition cannot support property $property!" }
        properties.put(property, predicate as Predicate<Any>)
    }

    override fun test(t: KryptonBlockState?): Boolean {
        if (t == null || t.block != definition.owner) return false
        if (properties.isEmpty()) return true
        properties.forEach { if (!applies(t, it.key, it.value)) return false }
        return true
    }

    private fun <T : Comparable<T>> applies(state: KryptonBlockState, property: KryptonProperty<T>, predicate: Predicate<Any>): Boolean =
        predicate.test(state.require(property))

    companion object {

        @JvmField
        val ANY: Predicate<KryptonBlockState?> = Predicate { true }

        @JvmStatic
        fun of(block: KryptonBlock): Predicate<KryptonBlockState?> = BlockStatePredicate(block.stateDefinition)
    }
}
