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
package org.kryptonmc.krypton.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

sealed class IntOrStringEnumSerializer<T : Enum<T>> : JsonSerializer<T>, JsonDeserializer<T> {

    abstract fun fromInt(value: Int): T?

    abstract fun fromString(value: String): T

    override fun serialize(src: T, type: Type, context: JsonSerializationContext) = JsonPrimitive(src.name.lowercase())

    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): T {
        if (json !is JsonPrimitive) throw JsonParseException("Cannot deserialize this as it isn't a string or integer!")
        if (json.isNumber) return fromInt(json.asInt) ?: throw JsonParseException("Cannot parse ${json.asInt}!")
        if (json.isString) {
            val string = json.asString
            if (string.toIntOrNull() != null) return fromInt(string.toInt()) ?: throw JsonParseException("Cannot parse $string!")
            return fromString(json.asString.uppercase())
        }
        throw JsonParseException("Unable to parse $json to a gamemode!")
    }
}
