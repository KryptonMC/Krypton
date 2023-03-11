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
package org.kryptonmc.krypton.testutil

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

    override fun getPlugin(id: String): PluginContainer? = when (id) {
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
        override val source: Path?
            get() = null

        override fun getDependency(id: String): PluginDependency? = null
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
