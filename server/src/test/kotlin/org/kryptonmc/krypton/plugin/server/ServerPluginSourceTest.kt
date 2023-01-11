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
