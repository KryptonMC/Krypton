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
package org.kryptonmc.krypton.auth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import net.kyori.adventure.identity.Identity
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import org.kryptonmc.krypton.util.array
import org.kryptonmc.krypton.util.readListTo
import java.util.UUID

@JvmRecord
data class KryptonGameProfile(
    override val name: String,
    override val uuid: UUID,
    override val properties: PersistentList<ProfileProperty>
) : GameProfile {

    override fun withProperties(properties: Iterable<ProfileProperty>): GameProfile = copy(properties = properties.toPersistentList())

    override fun withProperty(property: ProfileProperty): GameProfile = copy(properties = properties.add(property))

    override fun withoutProperty(index: Int): GameProfile = copy(properties = properties.removeAt(index))

    override fun withoutProperty(property: ProfileProperty): GameProfile = copy(properties = properties.remove(property))

    override fun identity(): Identity = Identity.identity(uuid)

    object Factory : GameProfile.Factory {

        override fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile =
            KryptonGameProfile(name, uuid, properties.toPersistentList())
    }

    companion object : TypeAdapter<GameProfile>() {

        override fun read(reader: JsonReader): GameProfile? {
            reader.beginObject()

            var uuid: UUID? = null
            var name: String? = null
            val properties = persistentListOf<ProfileProperty>().builder()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> uuid = MojangUUIDTypeAdapter.read(reader)
                    "name" -> name = reader.nextString()
                    "properties" -> reader.readListTo(properties, KryptonProfileProperty::read)
                }
            }

            reader.endObject()
            if (uuid == null || name == null) return null
            return KryptonGameProfile(name, uuid, properties.build())
        }

        override fun write(writer: JsonWriter, value: GameProfile) {
            writer.beginObject()
            writer.name("id")
            MojangUUIDTypeAdapter.write(writer, value.uuid)
            writer.name("name")
            writer.value(value.name)

            if (value.properties.isNotEmpty()) {
                writer.name("properties")
                writer.array(value.properties, KryptonProfileProperty::write)
            }
            writer.endObject()
        }
    }
}
