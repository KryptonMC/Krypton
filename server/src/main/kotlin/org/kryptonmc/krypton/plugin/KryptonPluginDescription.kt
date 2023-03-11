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
package org.kryptonmc.krypton.plugin

import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.KryptonPlatform
import java.nio.file.Path

open class KryptonPluginDescription(
    final override val id: String,
    final override val name: String,
    final override val version: String,
    final override val description: String,
    final override val authors: Collection<String>,
    final override val dependencies: Collection<PluginDependency>,
    final override val source: Path?
) : PluginDescription {

    final override fun getDependency(id: String): PluginDependency? = dependencies.firstOrNull { it.id == id }

    companion object {

        private const val DESCRIPTION = "A plugin representing the server. Used for internal things, such as service implementations."
        @JvmField
        val SERVER: KryptonPluginDescription =
            KryptonPluginDescription("krypton", "Krypton", KryptonPlatform.version, DESCRIPTION, setOf("KryptonMC"), emptySet(), Path.of(""))
    }
}
