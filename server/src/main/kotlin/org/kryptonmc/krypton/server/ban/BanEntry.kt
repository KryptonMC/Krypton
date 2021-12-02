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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.krypton.server.ServerConfigEntry
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

sealed class BanEntry<T>(
    key: T,
    final override val creationDate: OffsetDateTime = OffsetDateTime.now(),
    final override val source: Component = DEFAULT_SOURCE,
    final override val expirationDate: OffsetDateTime? = null,
    final override val reason: Component = DEFAULT_REASON
) : ServerConfigEntry<T>(key), Ban {

    private val sourceName by lazy { LegacyComponentSerializer.legacySection().serialize(source) }
    private val reasonString by lazy { LegacyComponentSerializer.legacySection().serialize(reason) }
    private val startFormatted by lazy { creationDate.format(DATE_FORMATTER) }
    private val endFormatted by lazy { expirationDate?.format(DATE_FORMATTER) ?: "forever" }
    override val isInvalid: Boolean
        get() {
            val now = OffsetDateTime.now()
            if (expirationDate == null) return false
            return expirationDate.isEqual(now) || expirationDate.isBefore(now)
        }

    abstract fun writeKey(writer: JsonWriter)

    final override fun write(writer: JsonWriter) {
        writer.beginObject()
        writeKey(writer)
        writer.name("created")
        writer.value(startFormatted)
        writer.name("source")
        writer.value(sourceName)
        writer.name("expires")
        writer.value(endFormatted)
        writer.name("reason")
        writer.value(reasonString)
        writer.endObject()
    }

    companion object {

        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        val DEFAULT_SOURCE = Component.text("(Unknown)")
        val DEFAULT_REASON = Component.text("Banned by operator.")
    }
}
