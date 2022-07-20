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
import java.util.UUID

@JvmRecord
data class KryptonGameProfile(
    override val name: String,
    override val uuid: UUID,
    override val properties: PersistentList<ProfileProperty>
) : GameProfile {

    override fun with(builder: GameProfile.Builder.() -> Unit): GameProfile = Builder(this).apply(builder).build()

    override fun withProperties(properties: Iterable<ProfileProperty>): GameProfile = copy(properties = properties.toPersistentList())

    override fun withProperty(property: ProfileProperty): GameProfile = copy(properties = properties.add(property))

    override fun withoutProperty(index: Int): GameProfile = copy(properties = properties.removeAt(index))

    override fun withoutProperty(property: ProfileProperty): GameProfile = copy(properties = properties.remove(property))

    override fun identity(): Identity = Identity.identity(uuid)

    override fun toBuilder(): GameProfile.Builder = Builder(this)

    class Builder(private val profile: GameProfile) : GameProfile.Builder {

        private val properties = persistentListOf<ProfileProperty>().builder()

        override fun properties(properties: Iterable<ProfileProperty>): GameProfile.Builder = apply {
            this.properties.clear()
            this.properties.addAll(properties)
        }

        override fun addProperty(property: ProfileProperty): GameProfile.Builder = apply { properties.add(property) }

        override fun removeProperty(index: Int): GameProfile.Builder = apply { properties.removeAt(index) }

        override fun removeProperty(property: ProfileProperty): GameProfile.Builder = apply { properties.remove(property) }

        override fun build(): GameProfile = KryptonGameProfile(profile.name, profile.uuid, properties.build())
    }

    object Factory : GameProfile.Factory {

        override fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile =
            KryptonGameProfile(name, uuid, properties.toPersistentList())
    }

    object Adapter : TypeAdapter<KryptonGameProfile>() {

        override fun read(reader: JsonReader): KryptonGameProfile? {
            reader.beginObject()

            var uuid: UUID? = null
            var name: String? = null
            val properties = persistentListOf<KryptonProfileProperty>().builder()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> uuid = MojangUUIDTypeAdapter.read(reader)
                    "name" -> name = reader.nextString()
                    "properties" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val property = KryptonProfileProperty.Adapter.read(reader)
                            if (property != null) properties.add(property)
                        }
                        reader.endArray()
                    }
                }
            }

            reader.endObject()
            if (uuid == null || name == null) return null
            return KryptonGameProfile(name, uuid, properties.build())
        }

        override fun write(writer: JsonWriter, value: KryptonGameProfile) {
            writer.beginObject()
            writer.name("id")
            MojangUUIDTypeAdapter.write(writer, value.uuid)
            writer.name("name")
            writer.value(value.name)

            if (value.properties.isNotEmpty()) {
                writer.name("properties")
                writer.beginArray()
                value.properties.forEach { KryptonProfileProperty.Adapter.write(writer, it) }
                writer.endArray()
            }
            writer.endObject()
        }
    }
}
