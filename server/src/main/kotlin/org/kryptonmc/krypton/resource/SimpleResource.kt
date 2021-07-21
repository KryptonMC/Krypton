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
package org.kryptonmc.krypton.resource

import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.pack.PackMetadata
import java.io.InputStream

class SimpleResource(
    override val sourceName: String,
    override val location: Key,
    override val inputStream: InputStream,
    private val metadataStream: InputStream?
) : Resource {

    override val hasMetadata = metadataStream != null

    private var triedMetadata = false
    private var jsonMetadata: JsonObject? = null

    override val metadata: PackMetadata?
        get() {
            if (!hasMetadata) return null
            if (jsonMetadata == null && !triedMetadata) {
                triedMetadata = true
                jsonMetadata = metadataStream!!.reader().use { GSON.fromJson(it) }
            }
            if (jsonMetadata == null) return null
            val name = PackMetadata.Serializer.name
            return if (jsonMetadata!!.has(name)) PackMetadata.Serializer.fromJson(jsonMetadata!![name].asJsonObject) else null
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SimpleResource
        return sourceName == other.sourceName && location == other.location
    }

    override fun hashCode() = 31 * sourceName.hashCode() + location.hashCode()

    override fun close() {
        inputStream.close()
        metadataStream?.close()
    }
}
