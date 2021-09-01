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

data class JsonMapType(val map: JsonObject, val compressed: Boolean) : MapType<String> {

    constructor(compressed: Boolean) : this(JsonObject(), compressed)

    override fun size() = map.size()

    override fun isEmpty() = map.entrySet().isEmpty()

    override fun clear() = map.entrySet().clear()

    override fun keys(): Set<String> = map.keySet()

    override fun copy() = JsonMapType(map.deepCopy(), compressed)

    override fun hasKey(key: String) = map.has(key)

    override fun hasKey(key: String, type: ObjectType): Boolean {
        val element = map[key] ?: return false
        if (type === ObjectType.UNDEFINED) return true
        if (element.isJsonArray) {
            return type === ObjectType.LIST
        } else if (element.isJsonObject) {
            return type === ObjectType.MAP
        } else if (element.isJsonNull) {
            return false
        }

        val primitive = element as JsonPrimitive
        return if (primitive.isString) {
            type === ObjectType.STRING || (compressed && type === ObjectType.NUMBER)
        } else if (primitive.isBoolean) {
            type.isNumber
        } else when (primitive.asNumber) { // is number
            is Byte -> type === ObjectType.BYTE || (compressed && type === ObjectType.STRING)
            is Short -> type === ObjectType.SHORT || (compressed && type === ObjectType.STRING)
            is Int -> type === ObjectType.INT || (compressed && type === ObjectType.STRING)
            is Long -> type === ObjectType.LONG || (compressed && type === ObjectType.STRING)
            is Float -> type === ObjectType.FLOAT || (compressed && type === ObjectType.STRING)
            else -> type === ObjectType.DOUBLE || (compressed && type === ObjectType.STRING)
        }
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun getGeneric(key: String): Any? {
        val element = map[key]
        return when {
            element == null || element.isJsonNull -> null
            element is JsonObject -> JsonMapType(element, compressed)
            element is JsonArray -> JsonListType(element, compressed)
            else -> {
                val primitive = element as JsonPrimitive
                when {
                    primitive.isNumber -> primitive.asNumber
                    primitive.isString -> primitive.asString
                    primitive.isBoolean -> primitive.asBoolean
                    else -> error("Unknown JSON element $element!")
                }
            }
        }
    }

    override fun getNumber(key: String, dfl: Number?): Number? {
        val element = map[key] as? JsonPrimitive ?: return dfl
        return when {
            element.isNumber -> element.asNumber
            element.isBoolean -> if (element.asBoolean) 1.toByte() else 0.toByte()
            compressed && element.isString -> element.asString.toIntOrNull()
            else -> dfl
        }
    }

    override fun getBoolean(key: String) = getBoolean(key, false)

    override fun getBoolean(key: String, dfl: Boolean): Boolean {
        val element = map[key] as? JsonPrimitive ?: return dfl
        return when {
            element.isNumber -> element.asNumber.toByte() != 0.toByte()
            element.isBoolean -> element.asBoolean
            else -> dfl
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        map.addProperty(key, value)
    }

    override fun getByte(key: String) = getByte(key, 0)

    override fun getByte(key: String, dfl: Byte) = getNumber(key, null)?.toByte() ?: dfl

    override fun setByte(key: String, value: Byte) {
        map.addProperty(key, value)
    }

    override fun getShort(key: String) = getShort(key, 0)

    override fun getShort(key: String, dfl: Short) = getNumber(key, null)?.toShort() ?: dfl

    override fun setShort(key: String, value: Short) {
        map.addProperty(key, value)
    }

    override fun getInt(key: String) = getInt(key, 0)

    override fun getInt(key: String, dfl: Int) = getNumber(key, null)?.toInt() ?: dfl

    override fun setInt(key: String, value: Int) {
        map.addProperty(key, value)
    }

    override fun getLong(key: String) = getLong(key, 0)

    override fun getLong(key: String, dfl: Long) = getNumber(key, null)?.toLong() ?: dfl

    override fun setLong(key: String, value: Long) {
        map.addProperty(key, value)
    }

    override fun getFloat(key: String) = getFloat(key, 0F)

    override fun getFloat(key: String, dfl: Float) = getNumber(key, null)?.toFloat() ?: dfl

    override fun setFloat(key: String, value: Float) {
        map.addProperty(key, value)
    }

    override fun getDouble(key: String) = getDouble(key, 0.0)

    override fun getDouble(key: String, dfl: Double) = getNumber(key, null)?.toDouble() ?: dfl

    override fun setDouble(key: String, value: Double) {
        map.addProperty(key, value)
    }

    override fun getBytes(key: String, dfl: ByteArray?) = dfl

    override fun setBytes(key: String, value: ByteArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getShorts(key: String, dfl: ShortArray?) = dfl

    override fun setShorts(key: String, value: ShortArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getInts(key: String, dfl: IntArray?) = dfl

    override fun setInts(key: String, value: IntArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getLongs(key: String, dfl: LongArray?) = dfl

    override fun setLongs(key: String, value: LongArray?) {
        throw UnsupportedOperationException() // JSON doesn't support raw primitive arrays
    }

    override fun getListUnchecked(key: String, dfl: ListType?) = (map[key] as? JsonArray)?.let { JsonListType(it, compressed) } ?: dfl

    override fun setList(key: String, value: ListType) {
        map.add(key, (value as JsonListType).array)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getMap(key: String, dfl: MapType<T>?) = (map[key] as? JsonObject)?.let { JsonMapType(it, compressed) } as? MapType<T> ?: dfl

    override fun setMap(key: String, value: MapType<*>) {
        map.add(key, (value as JsonMapType).map)
    }

    override fun getString(key: String, dfl: String?): String? {
        val element = map[key] as? JsonPrimitive ?: return dfl
        return when {
            element.isString -> element.asString
            compressed && element.isNumber -> element.asString
            else -> dfl
        }
    }

    override fun setString(key: String, value: String) {
        map.addProperty(key, value)
    }
}
