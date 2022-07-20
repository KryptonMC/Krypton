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
package org.kryptonmc.krypton

import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.auth.ProfileHolder
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileSerializationTests {

    @Test
    fun `test profile holder serialization`() {
        val result = ProfileHolder.Adapter.toJson(ProfileHolder(KryptonGameProfile(NAME, ID, persistentListOf()), TIME))
        val expected = "{\"name\":\"$NAME\",\"uuid\":\"$ID\",\"expiresOn\":\"$TIME_FORMATTED\"}"
        assertEquals(expected, result)
    }

    @Test
    fun `test profile holder deserialization`() {
        val expected = ProfileHolder(KryptonGameProfile(NAME, ID, persistentListOf()), TIME)
        val result = ProfileHolder.Adapter.fromJson("{\"name\":\"$NAME\",\"uuid\":\"$ID\",\"expiresOn\":\"$TIME_FORMATTED\"}")
        assertEquals(expected, result)
        assertNull(ProfileHolder.Adapter.fromJson("{\"name\":\"$NAME\",\"uuid\":\"$ID\",\"expiresOn\":\"adacdgadsgdgdstgas\"}"))
    }

    @Test
    fun `test game profile serialization`() {
        val result = KryptonGameProfile.Adapter.toJson(KryptonGameProfile(NAME, ID, persistentListOf()))
        assertEquals("{\"id\":\"${MojangUUIDTypeAdapter.toString(ID)}\",\"name\":\"$NAME\"}", result)

        // With properties
        val withProperties = KryptonGameProfile.Adapter.toJson(KryptonGameProfile(NAME, ID, PROPERTIES))
        val propertiesString = PROPERTIES.joinToString(",") { KryptonProfileProperty.Adapter.toJson(it) }
        assertEquals("{\"id\":\"${MojangUUIDTypeAdapter.toString(ID)}\",\"name\":\"$NAME\",\"properties\":[$propertiesString]}", withProperties)
    }

    @Test
    fun `test game profile deserialization`() {
        val json = "{\"id\":\"${MojangUUIDTypeAdapter.toString(ID)}\",\"name\":\"$NAME\"}"
        assertEquals(KryptonGameProfile(NAME, ID, persistentListOf()), KryptonGameProfile.Adapter.fromJson(json))

        // With properties
        val propertiesString = PROPERTIES.joinToString(",") { KryptonProfileProperty.Adapter.toJson(it) }
        val propertiesJson = "{\"id\":\"${MojangUUIDTypeAdapter.toString(ID)}\",\"name\":\"$NAME\",\"properties\":[$propertiesString]}"
        assertEquals(KryptonGameProfile(NAME, ID, PROPERTIES), KryptonGameProfile.Adapter.fromJson(propertiesJson))
    }

    @Test
    fun `test property serialization`() {
        val result = KryptonProfileProperty.Adapter.toJson(KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd"))
        assertEquals("{\"name\":\"Hello\",\"value\":\"World\",\"signature\":\"aaaabbbbccccdddd\"}", result)
    }

    @Test
    fun `test property deserialization`() {
        val json = "{\"name\":\"Hello\",\"value\":\"World\",\"signature\":\"aaaabbbbccccdddd\"}"
        assertEquals(KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd"), KryptonProfileProperty.Adapter.fromJson(json))
    }

    companion object {

        private const val NAME = "Dave"
        private val ID = UUID.randomUUID()
        private val PROPERTIES = persistentListOf(
            KryptonProfileProperty("Hello", "World", "aaaabbbbccccdddd"),
            KryptonProfileProperty("World", "Hello", "ddddccccbbbbaaaa")
        )

        private val TIME = ZonedDateTime.of(2021, 2, 13, 17, 2, 36, 0, ZoneOffset.UTC)
        private val TIME_FORMATTED = ProfileHolder.DATE_FORMATTER.format(TIME)
    }
}
