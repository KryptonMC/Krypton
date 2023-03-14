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

import com.google.gson.stream.JsonReader
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.io.StringReader
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileHolderTest {

    @Test
    fun `verify equals and hashcode`() {
        EqualsVerifier.forClass(ProfileHolder::class.java)
            .withIgnoredFields("lastAccess")
            .verify()
    }

    @Test
    fun `ensure holders with all same data compare equal`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME)
        assertEquals(0, holder1.compareTo(holder2))
        assertEquals(0, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different profiles and same last access compare equal`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(KryptonGameProfile.basic(UUID.randomUUID(), "Steve"), TIME)
        assertEquals(0, holder1.compareTo(holder2))
        assertEquals(0, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different expiry dates and same last access compare equal`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME.plusDays(1))
        assertEquals(0, holder1.compareTo(holder2))
        assertEquals(0, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with same data and less last access compare less`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME)
        holder1.setLastAccess(1L)
        assertEquals(-1, holder1.compareTo(holder2))
        assertEquals(1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different time and less last access compare less`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME.plusDays(1))
        holder1.setLastAccess(1L)
        assertEquals(-1, holder1.compareTo(holder2))
        assertEquals(1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different profile and less last access compare less`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(KryptonGameProfile.basic(UUID.randomUUID(), "Steve"), TIME)
        holder1.setLastAccess(1L)
        assertEquals(-1, holder1.compareTo(holder2))
        assertEquals(1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with same data and greater last access compare greater`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME)
        holder2.setLastAccess(1L)
        assertEquals(1, holder1.compareTo(holder2))
        assertEquals(-1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different time and greater last access compare greater`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(PROFILE, TIME.plusDays(1))
        holder2.setLastAccess(1L)
        assertEquals(1, holder1.compareTo(holder2))
        assertEquals(-1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure holders with different profile and greater last access compare greater`() {
        val holder1 = ProfileHolder(PROFILE, TIME)
        val holder2 = ProfileHolder(KryptonGameProfile.basic(UUID.randomUUID(), "Steve"), TIME)
        holder2.setLastAccess(1L)
        assertEquals(1, holder1.compareTo(holder2))
        assertEquals(-1, holder2.compareTo(holder1))
    }

    @Test
    fun `ensure empty json object deserializes to null`() {
        assertNull(ProfileHolder.Adapter.read(JsonReader(StringReader("{}"))))
    }

    @Test
    fun `ensure correctly formatted json object with all properties deserializes correctly`() {
        val json = formatToJsonString(ID, NAME, TIME_FORMATTED)
        assertEquals(ProfileHolder(PROFILE, TIME), ProfileHolder.Adapter.read(JsonReader(StringReader(json))))
    }

    @Test
    fun `ensure full profile holder serializes to correct json`() {
        JSONAssert.assertEquals(formatToJsonString(ID, NAME, TIME_FORMATTED), ProfileHolder.Adapter.toJson(ProfileHolder(PROFILE, TIME)), true)
    }

    @Test
    fun `ensure correct profile holder json deserializes correctly`() {
        assertEquals(ProfileHolder(PROFILE, TIME), ProfileHolder.Adapter.fromJson(formatToJsonString(ID, NAME, TIME_FORMATTED)))
    }

    @Test
    fun `ensure profile holder json with bad expiry date fails to deserialize`() {
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
