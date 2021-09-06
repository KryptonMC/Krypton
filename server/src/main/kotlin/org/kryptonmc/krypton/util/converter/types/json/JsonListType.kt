/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.converter.types.json

import ca.spottedleaf.dataconverter.types.ListType
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

data class JsonListType(val array: JsonArray, val compressed: Boolean) : ListType {

    constructor(compressed: Boolean) : this(JsonArray(), compressed)

    override fun copy() = JsonListType(array.deepCopy(), compressed)

    override fun getType() = ObjectType.UNDEFINED

    override fun size() = array.size()

    override fun remove(index: Int) {
        array.remove(index)
    }

    override fun getNumber(index: Int): Number {
        val element = array[index]
        if (element !is JsonPrimitive) error("Expected JSON primitive at index $index, was $element!")
        return if (element.isNumber) {
            element.asNumber
        } else if (element.isBoolean) {
            if (element.asBoolean) 1.toByte() else 0.toByte()
        } else if (compressed && element.isString) {
            element.asString.toInt()
        } else {
            error("Expected JSON primitive at index $index, was $element!")
        }
    }

    override fun getByte(index: Int) = getNumberOrNull(index)?.toByte() ?: 0

    override fun setByte(index: Int, to: Byte) {
        array[index] = JsonPrimitive(to)
    }

    override fun getShort(index: Int) = getNumberOrNull(index)?.toShort() ?: 0

    override fun setShort(index: Int, to: Short) {
        array[index] = JsonPrimitive(to)
    }

    override fun getInt(index: Int) = getNumberOrNull(index)?.toInt() ?: 0

    override fun setInt(index: Int, to: Int) {
        array[index] = JsonPrimitive(to)
    }

    override fun getLong(index: Int) = getNumberOrNull(index)?.toLong() ?: 0

    override fun setLong(index: Int, to: Long) {
        array[index] = JsonPrimitive(to)
    }

    override fun getFloat(index: Int) = getNumberOrNull(index)?.toFloat() ?: 0F

    override fun setFloat(index: Int, to: Float) {
        array[index] = JsonPrimitive(to)
    }

    override fun getDouble(index: Int) = getNumberOrNull(index)?.toDouble() ?: 0.0

    override fun setDouble(index: Int, to: Double) {
        array[index] = JsonPrimitive(to)
    }

    override fun getBytes(index: Int): ByteArray {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun setBytes(index: Int, to: ByteArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getShorts(index: Int): ShortArray {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun setShorts(index: Int, to: ShortArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getInts(index: Int): IntArray {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun setInts(index: Int, to: IntArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getLongs(index: Int): LongArray {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun setLongs(index: Int, to: LongArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getList(index: Int) = JsonListType(requireNotNull(array[index] as? JsonArray), compressed)

    override fun setList(index: Int, list: ListType) {
        array[index] = (list as JsonListType).array
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getMap(index: Int) = JsonMapType(requireNotNull(array[index] as? JsonObject), compressed) as MapType<T>

    override fun setMap(index: Int, to: MapType<*>) {
        array[index] = (to as JsonMapType).map
    }

    override fun getString(index: Int): String {
        val element = array[index]
        check(element is JsonPrimitive) { "Expected JSON primitive at index $index, was $element" }
        check(element.isString || (compressed && element.isNumber)) {
            "Expected JSON primitive at index $index, was $element"
        }
        return element.asString
    }

    override fun setString(index: Int, to: String) {
        array[index] = JsonPrimitive(to)
    }

    override fun addByte(b: Byte) {
        array.add(b)
    }

    override fun addByte(index: Int, b: Byte) {
        array.set(index, JsonPrimitive(b))
    }

    override fun addShort(s: Short) {
        array.add(s)
    }

    override fun addShort(index: Int, s: Short) {
        array.set(index, JsonPrimitive(s))
    }

    override fun addInt(i: Int) {
        array.add(i)
    }

    override fun addInt(index: Int, i: Int) {
        array.set(index, JsonPrimitive(i))
    }

    override fun addLong(l: Long) {
        array.add(l)
    }

    override fun addLong(index: Int, l: Long) {
        array.set(index, JsonPrimitive(l))
    }

    override fun addFloat(f: Float) {
        array.add(f)
    }

    override fun addFloat(index: Int, f: Float) {
        array.set(index, JsonPrimitive(f))
    }

    override fun addDouble(d: Double) {
        array.add(d)
    }

    override fun addDouble(index: Int, d: Double) {
        array.set(index, JsonPrimitive(d))
    }

    override fun addByteArray(arr: ByteArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addByteArray(index: Int, arr: ByteArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addShortArray(arr: ShortArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addShortArray(index: Int, arr: ShortArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addIntArray(arr: IntArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addIntArray(index: Int, arr: IntArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addLongArray(arr: LongArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addLongArray(index: Int, arr: LongArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun addList(list: ListType) {
        array.add((list as JsonListType).array)
    }

    override fun addList(index: Int, list: ListType) {
        array.set(index, (list as JsonListType).array)
    }

    override fun addMap(map: MapType<*>) {
        array.add((map as JsonMapType).map)
    }

    override fun addMap(index: Int, map: MapType<*>) {
        array.set(index, (map as JsonMapType).map)
    }

    override fun addString(string: String) {
        array.add(string)
    }

    override fun addString(index: Int, string: String) {
        array.set(index, JsonPrimitive(string))
    }

    private fun getNumberOrNull(index: Int) = try {
        getNumber(index)
    } catch (exception: Exception) {
        null
    }
}
