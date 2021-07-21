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
package org.kryptonmc.krypton.pack

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toComponent

class PackMetadata(
    private val description: Component,
    private val format: Int
) {

    object Serializer {

        const val name = "pack"

        fun fromJson(json: JsonObject): PackMetadata {
            val description = json["description"]?.toComponent() ?: throw JsonParseException("Missing required pack description!")
            val format = json["pack_format"]?.asInt ?: throw JsonParseException("Missing required pack format!")
            return PackMetadata(description, format)
        }
    }
}
