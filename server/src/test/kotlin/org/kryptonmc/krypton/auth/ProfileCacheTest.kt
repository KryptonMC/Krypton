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
        val cache = GameProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertEquals(PROFILE, cache.getProfile(ID))
    }

    @Test
    fun `ensure add profile makes profile available by name`() {
        val cache = GameProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertEquals(PROFILE, cache.getProfile(NAME))
    }

    @Test
    fun `ensure get profile by name is case sensitive`() {
        val cache = GameProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertNull(cache.getProfile("dave"))
    }

    @Test
    fun `ensure add profile appears in iterator`() {
        val cache = GameProfileCache(DUMMY_PATH)
        cache.addProfile(PROFILE)
        assertTrue(cache.iterator().hasNext())
    }

    companion object {

        private val DUMMY_PATH = Jimfs.newFileSystem(Configuration.unix()).getPath("dummy")
        private val ID = UUID.randomUUID()
        private const val NAME = "Dave"
        private val PROFILE = KryptonGameProfile.basic(ID, NAME)
    }
}
