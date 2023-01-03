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
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfileCacheTest {

    @Test
    fun `ensure add profile makes profile available by id`() {
        val cache = KryptonProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertEquals(PROFILE, cache.getProfile(ID))
    }

    @Test
    fun `ensure add profile makes profile available by name`() {
        val cache = KryptonProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertEquals(PROFILE, cache.getProfile(NAME))
    }

    @Test
    fun `ensure get profile by name is case sensitive`() {
        val cache = KryptonProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertNull(cache.getProfile("dave"))
    }

    @Test
    fun `ensure add profile appears in iterator`() {
        val cache = KryptonProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        val iterator = cache.iterator()
        assertTrue(iterator.hasNext())
    }

    companion object {

        private val DUMMY_PATH = Jimfs.newFileSystem(Configuration.unix()).getPath("dummy")
        private val ID = UUID.randomUUID()
        private const val NAME = "Dave"
        private val PROFILE = KryptonGameProfile.basic(ID, NAME)
    }
}
