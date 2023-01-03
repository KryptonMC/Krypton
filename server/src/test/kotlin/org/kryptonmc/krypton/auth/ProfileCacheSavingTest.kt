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

import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.nio.file.Files
import java.util.UUID

class ProfileCacheSavingTest {

    @Test
    fun `ensure save with full profile saves correct data`() {
        val path = FS.getPath("output_full")
        val cache = KryptonProfileCache(path)
        val uuid = UUID.randomUUID()
        cache.addProfile(KryptonGameProfile.basic(uuid, "Dave"))
        cache.save()
        JSONAssert.assertEquals("""[{"name":"Dave","uuid":"$uuid"}]""", Files.readString(path), false)
    }

    @Test
    fun `ensure add marks dirty`() {
        val path = FS.getPath("output_dirty")
        val cache = KryptonProfileCache(path)
        val uuid = UUID.randomUUID()
        cache.addProfile(KryptonGameProfile.basic(uuid, "Steve"))
        cache.saveIfNeeded()
        JSONAssert.assertEquals("""[{"name":"Steve","uuid":"$uuid"}]""", Files.readString(path), false)
    }

    companion object {

        private val FS = Jimfs.newFileSystem()
    }
}
