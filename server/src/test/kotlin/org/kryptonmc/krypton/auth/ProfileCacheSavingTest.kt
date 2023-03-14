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
import org.skyscreamer.jsonassert.JSONAssert
import java.nio.file.Files
import java.util.UUID

class ProfileCacheSavingTest {

    @Test
    fun `ensure save with full profile saves correct data`() {
        val path = FS.getPath("output_full")
        val cache = GameProfileCache(path)
        val uuid = UUID.randomUUID()
        cache.addProfile(KryptonGameProfile.basic(uuid, "Dave"))
        cache.save()
        JSONAssert.assertEquals("""[{"name":"Dave","uuid":"$uuid"}]""", Files.readString(path), false)
    }

    @Test
    fun `ensure add marks dirty`() {
        val path = FS.getPath("output_dirty")
        val cache = GameProfileCache(path)
        val uuid = UUID.randomUUID()
        cache.addProfile(KryptonGameProfile.basic(uuid, "Steve"))
        cache.saveIfNeeded()
        JSONAssert.assertEquals("""[{"name":"Steve","uuid":"$uuid"}]""", Files.readString(path), false)
    }

    companion object {

        private val FS = Jimfs.newFileSystem()
    }
}
