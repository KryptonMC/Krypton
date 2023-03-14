/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.auth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID
import org.kryptonmc.api.auth.GameProfile

/**
 * This holds a game profile and other information that we need when storing
 * it, such as the expiry date, and the last time it was accessed.
 *
 * The reason why we store the last access time is so we can order profiles by
 * it, because we only store the [GameProfileCache.MRU_LIMIT] most recent
 * profiles to disk.
 */
class ProfileHolder(val profile: GameProfile, val expiryDate: ZonedDateTime) : Comparable<ProfileHolder> {

    @Volatile
    private var lastAccess = 0L

    fun setLastAccess(value: Long) {
        lastAccess = value
    }

    override fun compareTo(other: ProfileHolder): Int = other.lastAccess.compareTo(lastAccess)

    override fun equals(other: Any?): Boolean =
        this === other || other is ProfileHolder && profile == other.profile && expiryDate == other.expiryDate

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + profile.hashCode()
        result = 31 * result + expiryDate.hashCode()
        return result
    }

    override fun toString(): String = "ProfileHolder(profile=$profile, expiryDate=$expiryDate)"

    object Adapter : TypeAdapter<ProfileHolder>() {

        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")

        override fun read(reader: JsonReader): ProfileHolder? {
            reader.beginObject()

            var uuid: UUID? = null
            var name: String? = null
            var expiryDate: ZonedDateTime? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "uuid" -> uuid = UUID.fromString(reader.nextString())
                    "name" -> name = reader.nextString()
                    "expiresOn" -> expiryDate = try {
                        ZonedDateTime.parse(reader.nextString(), DATE_FORMATTER)
                    } catch (ignored: DateTimeParseException) {
                        null
                    }
                }
            }

            reader.endObject()
            if (uuid == null || name == null || expiryDate == null) return null
            return ProfileHolder(KryptonGameProfile.basic(uuid, name), expiryDate)
        }

        override fun write(writer: JsonWriter, value: ProfileHolder) {
            writer.beginObject()
            writer.name("uuid")
            writer.value(value.profile.uuid.toString())
            writer.name("name")
            writer.value(value.profile.name)
            writer.name("expiresOn")
            writer.value(DATE_FORMATTER.format(value.expiryDate))
            writer.endObject()
        }
    }
}
