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
package org.kryptonmc.krypton.util

import com.google.common.collect.Table
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.property.KryptonProperty

// SpottedLeaf you're a genius
class ZeroCollidingReferenceStateTable(private val thisState: KryptonState<*, *>, values: Map<KryptonProperty<*>, Comparable<*>>) {

    // upper 32 bits: starting index
    // lower 32 bits: bitset for contained ids
    private val thisIndexTable = createTable(values.keys)
    private val thisTable: Array<Comparable<*>?>
    private var indexTable: LongArray? = null
    private var valueTable: Array<Array<KryptonState<*, *>?>?>? = null

    init {
        var maxId = -1
        for (property in values.keys) {
            val id = lookupValueIndex(property, thisIndexTable)
            if (id > maxId) maxId = id
        }
        thisTable = arrayOfNulls(maxId + 1)
        for (entry in values.entries) {
            thisTable[lookupValueIndex(entry.key, thisIndexTable)] = entry.value
        }
    }

    fun loadInTable(table: Table<KryptonProperty<*>, Comparable<*>, KryptonState<*, *>>, values: Map<KryptonProperty<*>, Comparable<*>>) {
        val combined = HashSet<KryptonProperty<*>>(table.rowKeySet())
        combined.addAll(values.keys)
        indexTable = createTable(combined)

        var maxId = -1
        for (property in combined) {
            val id = lookupValueIndex(property, indexTable!!)
            if (id > maxId) maxId = id
        }
        valueTable = arrayOfNulls(maxId + 1)

        val map = table.rowMap()
        for (property in map.keys) {
            val propertyMap = map.get(property)!!
            val id = lookupValueIndex(property, indexTable!!)
            val states = arrayOfNulls<KryptonState<*, *>?>(property.values.size)
            valueTable!![id] = states
            for (entry in propertyMap.entries) {
                if (entry.value == null) continue
                states[idForHelper(property, entry.key)] = entry.value
            }
        }

        for (entry in values.entries) {
            val property = entry.key
            val index = lookupValueIndex(property, indexTable!!)
            if (valueTable!![index] == null) valueTable!![index] = arrayOfNulls(property.values.size)
            valueTable!![index]!![idForHelper(property, entry.value)] = thisState
        }
    }

    private fun createTable(properties: Collection<KryptonProperty<*>>): LongArray {
        var maxId = -1
        for (property in properties) {
            val id = property.id
            if (id > maxId) maxId = id
        }

        val result = LongArray(maxId + 1 + 31 ushr 5) // ceil((maxId + 1) / 32)
        for (property in properties) {
            val id = property.id
            result[id ushr 5] = result[id ushr 5] or (1L shl (id and 31))
        }

        var total = 0
        for (i in 1 until result.size) {
            total += java.lang.Long.bitCount(result[i - 1] and 0xFFFFFFFFL)
            result[i] = result[i] or (total.toLong() shl 32)
        }
        return result
    }

    fun get(property: KryptonProperty<*>): Comparable<*>? {
        val index = lookupValueIndex(property, thisIndexTable)
        if (index < 0 || index >= thisTable.size) return null
        return thisTable[index]
    }

    fun get(property: KryptonProperty<*>, with: Comparable<*>): KryptonState<*, *>? {
        val withId = idForHelper(property, with)
        if (withId < 0) return null

        val index = lookupValueIndex(property, indexTable!!)
        val table = valueTable!!
        if (index < 0 || index >= table.size) return null
        val values = table[index]!!
        if (withId >= values.size) return null
        return values[withId]
    }

    companion object {

        @JvmStatic
        private fun lookupValueIndex(property: KryptonProperty<*>, indexTable: LongArray): Int {
            val id = property.id
            val bitsetMask = 1L shl (id and 31)
            val lowerMask = bitsetMask - 1
            val index = id ushr 5
            if (index >= indexTable.size) return -1
            val indexValue = indexTable[index]
            val containsCheck = (indexValue and bitsetMask) - 1 shr Long.SIZE_BITS - 1 // -1L if it doesn't contain the value
            // index = total bits set in lower table values (upper 32 bits of index_value) plus total bits set in lower indices below id
            // contains_check is 0 if the bitset had id set, else it's -1: so index is unaffected if contains_check == 0,
            // otherwise it comes out as -1.
            return ((indexValue ushr 32) + java.lang.Long.bitCount(indexValue and lowerMask) or containsCheck).toInt()
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        private fun <T : Comparable<T>> idForHelper(property: KryptonProperty<T>, value: Comparable<*>): Int = property.idFor(value as T)
    }
}
