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

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.uuid.MojangUUIDTypeAdapter
import java.util.UUID
import kotlin.test.assertEquals

class GameProfileJsonTests {

    @Test
    fun `test game profile serialization`() {
        assertEquals(basicProfileToJsonString(BASIC_PROFILE), KryptonGameProfile.Adapter.toJson(BASIC_PROFILE))
        assertEquals(profileToJsonString(FULL_PROFILE), KryptonGameProfile.Adapter.toJson(FULL_PROFILE))
    }

    @Test
    fun `test game profile deserialization`() {
        assertEquals(BASIC_PROFILE, KryptonGameProfile.Adapter.fromJson(basicProfileToJsonString(BASIC_PROFILE)))
        assertEquals(FULL_PROFILE, KryptonGameProfile.Adapter.fromJson(profileToJsonString(FULL_PROFILE)))
    }

    @Test
    fun `test property serialization`() {
        assertEquals(propertyToJsonString(PROPERTY_1), KryptonProfileProperty.Adapter.toJson(PROPERTY_1))
    }

    @Test
    fun `test property deserialization`() {
        assertEquals(PROPERTY_1, KryptonProfileProperty.Adapter.fromJson(propertyToJsonString(PROPERTY_1)))
    }

    companion object {

        private val PROPERTY_1 = KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd")
        private val PROPERTY_2 = KryptonProfileProperty("World", "Hello", "ddddccccbbbbaaaa")
        private val PROPERTIES = persistentListOf(PROPERTY_1, PROPERTY_2)

        private const val NAME = "Dave"
        private val ID = UUID.fromString("12345678-1234-1234-1234-123456789012")
        private val BASIC_PROFILE = KryptonGameProfile.basic(ID, NAME)
        private val FULL_PROFILE = KryptonGameProfile.full(ID, NAME, PROPERTIES)

        @JvmStatic
        private fun propertyToJsonString(property: ProfileProperty): String {
            return """{"name":"${property.name}","value":"${property.value}","signature":"${property.signature!!}"}"""
        }

        @JvmStatic
        private fun profileToJsonString(profile: GameProfile): String {
            val id = MojangUUIDTypeAdapter.toString(profile.uuid)
            val properties = profile.properties.joinToString(",") { propertyToJsonString(it) }
            return """{"id":"$id","name":"${profile.name}","properties":[$properties]}"""
        }

        @JvmStatic
        private fun basicProfileToJsonString(profile: GameProfile): String {
            return """{"id":"${MojangUUIDTypeAdapter.toString(profile.uuid)}","name":"${profile.name}"}"""
        }
    }
}
