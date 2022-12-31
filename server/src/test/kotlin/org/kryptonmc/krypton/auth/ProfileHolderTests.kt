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

import com.google.gson.stream.JsonReader
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileHolderTests {

    @Test
    fun `verify equals and hashcode`() {
        EqualsVerifier.forClass(ProfileHolder::class.java)
            .withIgnoredFields("lastAccess")
            .verify()
    }

    @Test
    fun `verify equal objects compare equal`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(PROFILE, time)
        val holder2 = ProfileHolder(PROFILE, time)
        assertEquals(0, holder1.compareTo(holder1))
        assertEquals(0, holder2.compareTo(holder2))
        assertEquals(0, holder1.compareTo(holder2))
        assertEquals(0, holder2.compareTo(holder1))
    }

    @Test
    fun `verify newer last access less than older last access`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(PROFILE, time)
        val holder2 = ProfileHolder(PROFILE, time)
        holder1.setLastAccess(1L)
        assertEquals(-1, holder1.compareTo(holder2))
        assertEquals(1, holder2.compareTo(holder1))
    }

    @Test
    fun `verify older last access greater than newer last access`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(PROFILE, time)
        val holder2 = ProfileHolder(PROFILE, time)
        holder2.setLastAccess(1L)
        assertEquals(1, holder1.compareTo(holder2))
        assertEquals(-1, holder2.compareTo(holder1))
    }

    @Test
    fun `verify to string consistency`() {
        val time = ZonedDateTime.now()
        assertEquals("ProfileHolder(profile=$PROFILE, expiryDate=$time)", ProfileHolder(PROFILE, time).toString())
    }

    @Test
    fun `test reading empty object with no properties`() {
        assertNull(ProfileHolder.Adapter.read(JsonReader(StringReader("{}"))))
    }

    @Test
    fun `test reading object with all properties`() {
        assertEquals(ProfileHolder(PROFILE, TIME),
            ProfileHolder.Adapter.read(JsonReader(StringReader(formatToJsonString(ID, NAME, TIME_FORMATTED)))))
    }

    @Test
    fun `test profile holder serialization`() {
        assertEquals(formatToJsonString(ID, NAME, TIME_FORMATTED), ProfileHolder.Adapter.toJson(ProfileHolder(PROFILE, TIME)))
    }

    @Test
    fun `test profile holder deserialization`() {
        assertEquals(ProfileHolder(PROFILE, TIME), ProfileHolder.Adapter.fromJson(formatToJsonString(ID, NAME, TIME_FORMATTED)))
        assertNull(ProfileHolder.Adapter.fromJson(formatToJsonString(ID, NAME, "adacdgadsgdgdstgas")))
    }

    companion object {

        private const val NAME = "Dave"
        private val ID = UUID.fromString("12345678-1234-1234-1234-123456789012")
        private val PROFILE = KryptonGameProfile.basic(ID, NAME)

        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        // May seem random, but if you do some digging in the commit history, you'll find this is the Krypton epoch.
        private val TIME = ZonedDateTime.of(2021, 2, 13, 17, 2, 36, 0, ZoneOffset.UTC)
        private val TIME_FORMATTED = FORMATTER.format(TIME)

        @JvmStatic
        private fun formatToJsonString(uuid: UUID, name: String, expiry: String): String {
            return """{"uuid":"$uuid","name":"$name","expiresOn":"$expiry"}"""
        }
    }
}
