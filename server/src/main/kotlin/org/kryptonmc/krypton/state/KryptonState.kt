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

import com.google.common.collect.ArrayTable
import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import kotlinx.collections.immutable.ImmutableMap
import org.kryptonmc.krypton.util.ZeroCollidingReferenceStateTable
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.Optional
import java.util.function.Function

@Suppress("LeakingThis", "UNCHECKED_CAST")
abstract class KryptonState<O, S : KryptonState<O, S>>(
    protected val owner: O,
    val values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    private val propertiesCodec: MapCodec<S>
) {

    private var populatedNeighbours = false
    private val optimisedTable = ZeroCollidingReferenceStateTable(this, values)

    fun contains(property: KryptonProperty<*>): Boolean = optimisedTable.get(property) != null

    fun <V : Comparable<V>> get(property: KryptonProperty<V>): V? = property.type.cast(optimisedTable.get(property))

    fun <V : Comparable<V>> require(property: KryptonProperty<V>): V {
        val value = requireNotNull(optimisedTable.get(property)) { "Cannot get property $property as it does not exist on $owner!" }
        return property.type.cast(value)
    }

    fun <V : Comparable<V>> set(property: KryptonProperty<V>, value: V): S = optimisedTable.get(property, value) as? S ?: this as S

    fun <V : Comparable<V>> cycle(property: KryptonProperty<V>): S = set(property, findNext(property.values, get(property))!!)

    fun populateNeighbours(states: Map<Map<KryptonProperty<*>, Comparable<*>>, S>) {
        check(!populatedNeighbours) { "Attempted to populate neighbours when they were already populated!" }
        val table = HashBasedTable.create<KryptonProperty<*>, Comparable<*>, S>()
        values.forEach { entry ->
            val property = entry.key
            property.values.forEach {
                if (it != entry.value) table.put(property, it, states.get(createNeighbourValues(property, it))!!)
            }
        }
        val neighbours = if (table.isEmpty) table else ArrayTable.create(table)
        optimisedTable.loadInTable(neighbours as Table<KryptonProperty<*>, Comparable<*>, KryptonState<*, *>>, values)
        populatedNeighbours = true
    }

    private fun createNeighbourValues(property: KryptonProperty<*>, value: Comparable<*>): Map<KryptonProperty<*>, Comparable<*>> {
        val values = HashMap(values)
        values.put(property, value)
        return values
    }

    final override fun toString(): String = buildString {
        append(owner)
        if (values.isNotEmpty()) {
            append('[')
            append(values.entries.joinToString(",") { "${it.key.name}=${toString(it.key, it.value)}" })
            append(']')
        }
    }

    companion object {

        @JvmStatic
        protected fun <O, S : KryptonState<O, S>> codec(
            ownerCodec: Codec<O>,
            defaultGetter: Function<O, S>
        ): Codec<S> = ownerCodec.dispatch("Name", { it.owner }) { owner ->
            val state = defaultGetter.apply(owner)
            if (state.values.isEmpty()) {
                Codec.unit(state)
            } else {
                state.propertiesCodec.codec().optionalFieldOf("Properties").xmap({ it.orElse(state) }, { Optional.of(it) }).codec()
            }
        }

        @JvmStatic
        private fun <T> findNext(collection: Collection<T>, value: T): T {
            val iterator = collection.iterator()
            while (iterator.hasNext()) {
                if (iterator.next() != value) continue
                if (iterator.hasNext()) return iterator.next()
                return collection.iterator().next()
            }
            return iterator.next()
        }

        @JvmStatic
        private fun <T : Comparable<T>> toString(property: KryptonProperty<T>, value: Comparable<*>): String = property.toString(value as T)
    }
}
