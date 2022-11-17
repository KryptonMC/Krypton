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
package org.kryptonmc.krypton.server.ban

import com.google.gson.stream.JsonWriter
import net.kyori.adventure.text.Component
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.krypton.adventure.toLegacySectionText
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

abstract class KryptonBan(
    override val source: Component,
    override val reason: Component,
    override val creationDate: OffsetDateTime,
    override val expirationDate: OffsetDateTime?
) : Ban {

    private val sourceName by lazy { source.toLegacySectionText() }
    private val reasonString by lazy { reason.toLegacySectionText() }

    abstract fun writeKey(writer: JsonWriter)

    fun write(writer: JsonWriter) {
        writer.beginObject()
        writeKey(writer)
        writer.name("created")
        writer.value(creationDate.format(DATE_FORMATTER))
        writer.name("source")
        writer.value(sourceName)
        writer.name("expires")
        writer.value(expirationDate?.format(DATE_FORMATTER) ?: "forever")
        writer.name("reason")
        writer.value(reasonString)
        writer.endObject()
    }

    companion object {

        @JvmField
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        @JvmField
        val DEFAULT_SOURCE: Component = Component.text("(Unknown)")
        @JvmField
        val DEFAULT_REASON: Component = Component.text("Banned by operator.")
    }
}
