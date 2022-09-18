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
package org.kryptonmc.krypton.util

import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.api.plugin.PluginManager
import java.nio.file.Path

class MockPluginManager : PluginManager {

    override val plugins: Collection<PluginContainer> = setOf(CONTAINER_A, CONTAINER_B)

    override fun fromInstance(instance: Any): PluginContainer? = when (instance) {
        PLUGIN_A -> CONTAINER_A
        PLUGIN_B -> CONTAINER_B
        else -> null
    }

    override fun plugin(id: String): PluginContainer? = when (id) {
        A_ID -> CONTAINER_A
        B_ID -> CONTAINER_B
        else -> null
    }

    override fun isLoaded(id: String): Boolean = id == A_ID || id == B_ID

    override fun addToClasspath(plugin: Any, path: Path) {
        throw UnsupportedOperationException()
    }

    private class FakePluginContainer(id: String, override val instance: Any?) : PluginContainer {

        override val description: PluginDescription = FakePluginDescription(id)
    }

    @JvmRecord
    private data class FakePluginDescription(override val id: String) : PluginDescription {

        override val name: String
            get() = ""
        override val version: String
            get() = ""
        override val description: String
            get() = ""
        override val authors: Collection<String>
            get() = emptySet()
        override val dependencies: Collection<PluginDependency>
            get() = emptySet()
        override val source: Path
            get() = Path.of("")

        override fun dependency(id: String): PluginDependency? = null
    }

    companion object {

        private const val A_ID = "a"
        private const val B_ID = "B"
        @JvmField
        val PLUGIN_A: Any = Any()
        @JvmField
        val PLUGIN_B: Any = Any()
        private val CONTAINER_A = FakePluginContainer(A_ID, PLUGIN_A)
        private val CONTAINER_B = FakePluginContainer(B_ID, PLUGIN_B)
    }
}
