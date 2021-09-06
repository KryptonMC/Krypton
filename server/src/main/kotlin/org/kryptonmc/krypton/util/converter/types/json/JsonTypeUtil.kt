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

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.TypeUtil
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.kryptonmc.krypton.util.converter.types.nbt.NBTListType
import org.kryptonmc.krypton.util.converter.types.nbt.NBTMapType

object JsonTypeUtil : TypeUtil {

    fun convertJsonToNBT(json: JsonMapType): NBTMapType {
        val ret = NBTMapType()
        json.map.entrySet().forEach {
            val generic = convertToGenericNBT(it.value, json.compressed) ?: return@forEach
            ret.setGeneric(it.key, generic)
        }
        return ret
    }

    private fun convertJsonToNBT(json: JsonListType): NBTListType {
        val ret = NBTListType()
        for (i in 0 until json.size()) {
            ret.addGeneric(convertToGenericNBT(json.array[i], json.compressed)
                ?: error("Nulls are not supported in NBT!"))
        }
        return ret
    }

    override fun createEmptyList() = JsonListType(false)

    @Suppress("UNCHECKED_CAST")
    override fun <K> createEmptyMap() = JsonMapType(false) as MapType<K>

    private fun convertToGenericNBT(element: JsonElement, compressed: Boolean): Any? = when(element) {
        is JsonObject -> convertJsonToNBT(JsonMapType(element, compressed))
        is JsonArray -> convertJsonToNBT(JsonListType(element, compressed))
        is JsonNull -> null
        else -> {
            val primitive = element as JsonPrimitive
            if (primitive.isBoolean) {
                if (primitive.asBoolean) 1.toByte() else 0.toByte()
            } else if (primitive.isNumber) {
                primitive.asNumber
            } else if (primitive.isString) {
                primitive.asString
            } else {
                error("Unrecognized type $element!")
            }
        }
    }
}
