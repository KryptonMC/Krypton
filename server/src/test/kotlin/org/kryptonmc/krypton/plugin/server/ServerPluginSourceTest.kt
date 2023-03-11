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
package org.kryptonmc.krypton.plugin.server

import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class ServerPluginSourceTest {

    // TODO: Not a great test. Has too much setup and too many invariants.
    @Test
    fun `ensure simple metadata file is loaded properly`() {
        val fs = Jimfs.newFileSystem()
        val simpleFile = fs.getPath("test.json")
        val description = "A simple test module"
        Files.writeString(simpleFile, """
            {
                "id": "test-module",
                "description": "$description",
                "main": "dummy.test.MainClass"
            }
        """.trimIndent())
        val discoverer = DummyDiscoverer(simpleFile)
        val source = ServerPluginSource(discoverer, fs.getPath("modules_dummy"), ServerModules(setOf("test"), setOf("test")))
        val loaded = source.loadDescriptions().single()
        assertEquals("test-module", loaded.id)
        assertEquals(description, loaded.description)
    }

    private class DummyDiscoverer(vararg val paths: Path) : ModuleDiscoverer {

        override fun discover(): Collection<Path> = paths.toSet()
    }
}
