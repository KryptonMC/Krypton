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

import com.google.common.collect.ArrayTable
import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Table
import org.kryptonmc.krypton.util.collection.ZeroCollidingReferenceStateTable
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.Optional
import java.util.function.Function

@Suppress("LeakingThis", "UnnecessaryAbstractClass") // This class is designed for inheritance, not instantiation.
abstract class KryptonState<O, S : KryptonState<O, S>>(
    protected val owner: O,
    val values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    private val propertiesCodec: MapCodec<S>
) {

    private var populatedNeighbours = false
    private val optimisedTable = ZeroCollidingReferenceStateTable(this, values)

    fun hasProperty(property: KryptonProperty<*>): Boolean = optimisedTable.get(property) != null

    fun <V : Comparable<V>> getProperty(property: KryptonProperty<V>): V? = property.type.cast(optimisedTable.get(property))

    fun <V : Comparable<V>> requireProperty(property: KryptonProperty<V>): V {
        val value = requireNotNull(optimisedTable.get(property)) { "Cannot get property $property as it does not exist on $owner!" }
        return property.type.cast(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : Comparable<V>> setProperty(property: KryptonProperty<V>, value: V): S {
        val newState = optimisedTable.get(property, value) ?: return this as S
        return newState as S
    }

    fun <V : Comparable<V>> cycleProperty(property: KryptonProperty<V>): S =
        setProperty(property, findNext(property.values, requireProperty(property)))

    fun populateNeighbours(states: Map<Map<KryptonProperty<*>, Comparable<*>>, S>) {
        check(!populatedNeighbours) { "Attempted to populate neighbours when they were already populated!" }
        val table = HashBasedTable.create<KryptonProperty<*>, Comparable<*>, S>()
        values.entries.forEach { (property, value) ->
            property.values.forEach {
                if (it != value) table.put(property, it, states.get(createNeighbourValues(property, it))!!)
            }
        }
        val neighbours = if (table.isEmpty) table else ArrayTable.create(table)
        @Suppress("UNCHECKED_CAST")
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
            append(values.entries.joinToString(",") { "${it.key.name}=${toStringHelper(it.key, it.value)}" })
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
                if (iterator.next() == value) {
                    if (iterator.hasNext()) return iterator.next()
                    return collection.iterator().next()
                }
            }
            return iterator.next()
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        private fun <T : Comparable<T>> toStringHelper(property: KryptonProperty<T>, value: Comparable<*>): String = property.toString(value as T)
    }
}
