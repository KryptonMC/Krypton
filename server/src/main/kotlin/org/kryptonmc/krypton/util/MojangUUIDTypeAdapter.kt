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
package org.kryptonmc.krypton.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.UUID

object MojangUUIDTypeAdapter : TypeAdapter<UUID>() {

    private val REPLACEMENT_REGEX = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex()

    @JvmStatic
    fun fromString(string: String): UUID = UUID.fromString(string.replace(REPLACEMENT_REGEX, "$1-$2-$3-$4-$5"))

    @JvmStatic
    fun toString(uuid: UUID): String = uuid.toString().replace("-", "")

    override fun read(reader: JsonReader): UUID = fromString(reader.nextString())

    override fun write(out: JsonWriter, value: UUID) {
        out.value(toString(value))
    }
}
