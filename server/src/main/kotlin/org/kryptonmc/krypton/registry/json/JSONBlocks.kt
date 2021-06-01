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
package org.kryptonmc.krypton.registry.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import me.bardy.gsonkt.deserialize

data class RegistryBlock(
    val properties: Map<String, List<String>> = emptyMap(),
    val states: List<RegistryBlockState>
) {

    companion object : JsonDeserializer<RegistryBlock> {

        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): RegistryBlock {
            json as JsonObject
            val properties = json["properties"]?.let { context.deserialize(it) } ?: emptyMap<String, List<String>>()
            val states = json["states"]?.let { context.deserialize(it) } ?: emptyList<RegistryBlockState>()
            return RegistryBlock(properties, states)
        }
    }
}

data class RegistryBlockState(
    val id: Int,
    val default: Boolean = false,
    val properties: Map<String, String> = emptyMap()
) {

    companion object : JsonDeserializer<RegistryBlockState> {

        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): RegistryBlockState {
            json as JsonObject
            val properties = json["properties"]?.let { context.deserialize(it) } ?: emptyMap<String, String>()
            return RegistryBlockState(json["id"].asInt, json["default"]?.asBoolean ?: false, properties)
        }
    }
}
