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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.uuid.MojangUUIDTypeAdapter
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.krypton.util.gson.array
import org.kryptonmc.krypton.util.gson.readListTo
import java.util.UUID

class KryptonGameProfile private constructor(
    override val uuid: UUID,
    override val name: String,
    override val properties: PersistentList<ProfileProperty>
) : GameProfile {

    override fun withProperties(properties: Iterable<ProfileProperty>): GameProfile = KryptonGameProfile(uuid, name, properties.toPersistentList())

    override fun withProperty(property: ProfileProperty): GameProfile = KryptonGameProfile(uuid, name, properties.add(property))

    override fun withoutProperty(index: Int): GameProfile = KryptonGameProfile(uuid, name, properties.removeAt(index))

    override fun withoutProperty(property: ProfileProperty): GameProfile = KryptonGameProfile(uuid, name, properties.remove(property))

    override fun equals(other: Any?): Boolean =
        this === other || other is KryptonGameProfile && uuid == other.uuid && name == other.name && properties == other.properties

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + uuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + properties.hashCode()
        return result
    }

    override fun toString(): String = "GameProfile(uuid=$uuid, name=$name, properties=$properties)"

    object Factory : GameProfile.Factory {

        override fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile = full(uuid, name, properties)
    }

    object Adapter : TypeAdapter<GameProfile>() {

        override fun read(reader: JsonReader): GameProfile? {
            reader.beginObject()

            var uuid: UUID? = null
            var name: String? = null
            val properties = persistentListOf<ProfileProperty>().builder()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> uuid = MojangUUIDTypeAdapter.read(reader)
                    "name" -> name = reader.nextString()
                    "properties" -> reader.readListTo(properties, KryptonProfileProperty.Adapter::read)
                }
            }

            reader.endObject()
            if (uuid == null || name == null) return null
            return full(uuid, name, properties.build())
        }

        override fun write(writer: JsonWriter, value: GameProfile) {
            writer.beginObject()
            writer.name("id")
            MojangUUIDTypeAdapter.write(writer, value.uuid)
            writer.name("name")
            writer.value(value.name)

            if (value.properties.isNotEmpty()) {
                writer.name("properties")
                writer.array(value.properties, KryptonProfileProperty.Adapter::write)
            }
            writer.endObject()
        }
    }

    companion object {

        @JvmStatic
        fun partial(name: String): GameProfile = basic(UUIDUtil.NIL_UUID, name)

        @JvmStatic
        fun partial(uuid: UUID): GameProfile = basic(uuid, "")

        @JvmStatic
        fun basic(uuid: UUID, name: String): GameProfile = KryptonGameProfile(uuid, name, persistentListOf())

        @JvmStatic
        fun full(uuid: UUID, name: String, properties: List<ProfileProperty>): GameProfile =
            KryptonGameProfile(uuid, name, properties.toPersistentList())
    }
}
