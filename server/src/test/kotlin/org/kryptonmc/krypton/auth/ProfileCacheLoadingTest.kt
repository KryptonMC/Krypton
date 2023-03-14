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

import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ProfileCacheLoadingTest {

    @Test
    fun `ensure single path loads to single profile`() {
        val path = FS.getPath("single")
        Files.newBufferedWriter(path).use { it.append("""[{"name":"$NAME","uuid":"$ID","expiresOn":"$TIME_FORMATTED"}]""") }
        val cache = GameProfileCache(path)
        cache.loadAll()
        assertEquals(KryptonGameProfile.basic(ID, NAME), cache.getProfile(ID))
    }

    @Test
    fun `ensure empty path loads no profiles`() {
        val path = FS.getPath("empty")
        Files.newBufferedWriter(path).use { it.append("[]") }
        val cache = GameProfileCache(path)
        cache.loadAll()
        assertFalse(cache.iterator().hasNext())
    }

    @Test
    fun `ensure no quotes data does not load due to error`() {
        val path = FS.getPath("no_quotes")
        Files.newBufferedWriter(path).use { it.append("[{name:$NAME,uuid:$ID,expiresOn:$TIME_FORMATTED}]") }
        val cache = GameProfileCache(path)
        cache.loadAll()
        assertFalse(cache.iterator().hasNext())
    }

    @Test
    fun `ensure data with no array does not load due to error`() {
        val path = FS.getPath("no_array")
        Files.newBufferedWriter(path).use { it.append("""{"name":"$NAME","uuid":"$ID","expiresOn":"$TIME_FORMATTED"}""") }
        val cache = GameProfileCache(path)
        cache.loadAll()
        assertFalse(cache.iterator().hasNext())
    }

    @Test
    fun `ensure no data path loads no profiles`() {
        val cache = GameProfileCache(FS.getPath("bogus"))
        cache.loadAll()
        assertFalse(cache.iterator().hasNext())
    }

    companion object {

        private val FS = Jimfs.newFileSystem()

        private val ID = UUID.randomUUID()
        private const val NAME = "Dave"

        // +10 points to anyone who knows what happened at this time.
        // No one? Tip: look at Krypton's Git history.
        private val TIME = ZonedDateTime.of(2021, 2, 13, 17, 2, 36, 0, ZoneOffset.UTC)
        private val TIME_FORMATTED = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z").format(TIME)
    }
}
