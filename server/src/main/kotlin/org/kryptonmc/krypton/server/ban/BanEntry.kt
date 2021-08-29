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
package org.kryptonmc.krypton.server.ban

import com.google.gson.stream.JsonWriter
import org.kryptonmc.krypton.server.ServerConfigEntry
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

sealed class BanEntry<T>(
    key: T,
    private val creationDate: OffsetDateTime = OffsetDateTime.now(),
    val source: String = "(Unknown)",
    val expiryDate: OffsetDateTime? = null,
    val reason: String = "Banned by operator."
) : ServerConfigEntry<T>(key) {

    override val isInvalid: Boolean
        get() = expiryDate?.isBefore(OffsetDateTime.now()) ?: false

    abstract fun writeKey(writer: JsonWriter)

    final override fun write(writer: JsonWriter) {
        writer.beginObject()
        writeKey(writer)
        writer.name("created")
        writer.value(creationDate.format(DATE_FORMATTER))
        writer.name("source")
        writer.value(source)
        writer.name("expires")
        writer.value(expiryDate?.format(DATE_FORMATTER) ?: "forever")
        writer.name("reason")
        writer.value(reason)
        writer.endObject()
    }

    companion object {

        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }
}
