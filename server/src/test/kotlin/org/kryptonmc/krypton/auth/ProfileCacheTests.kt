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

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfileCacheTests {

    @Test
    fun `test retrieval of profiles`() {
        val cache = KryptonProfileCache(EMPTY_PATH).apply { addProfile(PROFILE) }
        assertEquals(PROFILE, cache.getProfile(ID))
        assertNull(cache.getProfile(UUID.fromString("aaaabbbb-cccc-dddd-eeee-ffffaaaabbbb")))
        assertEquals(PROFILE, cache.getProfile("Dave"))
        assertNull(cache.getProfile("dave")) // Check case sensitivity
    }

    @Test
    fun `test profile collections`() {
        val cache = KryptonProfileCache(EMPTY_PATH).apply { addProfile(PROFILE) }
        val iterator = cache.iterator()
        assertTrue(iterator.hasNext())
        assertTrue(cache.profiles.isNotEmpty())
    }

    @Test
    fun `test profile loading`() {
        assertTrue(createAndInit(SINGLE_PATH).profiles.isNotEmpty())
        assertTrue(createAndInit(EMPTY_PATH).profiles.isEmpty())
        assertTrue(createAndInit(NO_QUOTES_PATH).profiles.isEmpty())
        assertTrue(createAndInit(NO_ARRAY_PATH).profiles.isEmpty())
        assertTrue(createAndInit(BOGUS_PATH).profiles.isEmpty())
    }

    @Test
    fun `test profile saving`() {
        val cache = createAndInit(OUTPUT_PATH)
        cache.addProfile(PROFILE)
        cache.addProfile(KryptonGameProfile.basic(UUID.randomUUID(), "Joe"))
        cache.save()
        assertTrue(OUTPUT_PATH.readText().isNotBlank())
    }

    @Test
    fun `ensure add marks dirty`() {
        val cache = spyk(KryptonProfileCache(NULL_PATH))
        cache.addProfile(KryptonGameProfile.basic(UUID.randomUUID(), "test"))
        cache.saveIfNeeded()
        verify { cache.save() }
    }

    companion object {

        private val FILE_SYSTEM = Jimfs.newFileSystem(Configuration.unix())
        private val NULL_PATH = FILE_SYSTEM.getPath("null_sink") // Used when we want to test saving without actually saving anything
        private val EMPTY_PATH = FILE_SYSTEM.getPath("empty")
        private val SINGLE_PATH = FILE_SYSTEM.getPath("single")
        private val NO_QUOTES_PATH = FILE_SYSTEM.getPath("malformed_profile")
        private val NO_ARRAY_PATH = FILE_SYSTEM.getPath("malformed_array")
        private val BOGUS_PATH = FILE_SYSTEM.getPath("bogus")
        private val OUTPUT_PATH = FILE_SYSTEM.getPath("output")

        private val ID = UUID.randomUUID()
        private val PROFILE = KryptonGameProfile.basic(ID, "Dave")

        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        private val TIME = ZonedDateTime.of(2021, 2, 13, 17, 2, 36, 0, ZoneOffset.UTC)
        private val TIME_FORMATTED = FORMATTER.format(TIME)

        init {
            EMPTY_PATH.writeText("[]")
            SINGLE_PATH.writeText("[{\"name\":\"Dave\",\"uuid\":\"$ID\",\"expiresOn\":\"$TIME_FORMATTED\"}]")
            NO_QUOTES_PATH.writeText("[{name:Dave,uuid:$ID,expiresOn:$TIME_FORMATTED}]")
            NO_ARRAY_PATH.writeText("{\"name\":\"Dave\",\"uuid\":\"$ID\",\"expiresOn\":\"$TIME_FORMATTED\"}")
        }

        @JvmStatic
        private fun createAndInit(path: Path): KryptonProfileCache = KryptonProfileCache(path).apply { loadAll() }
    }
}
