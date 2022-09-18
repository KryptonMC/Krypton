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
package org.kryptonmc.krypton.plugin

import kotlinx.collections.immutable.persistentMapOf
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
    dependencies: Collection<PluginDependency>,
    final override val source: Path
) : PluginDescription {

    private val dependencyMap = dependencies.associateByTo(persistentMapOf<String, PluginDependency>().builder(), PluginDependency::id).build()
    final override val dependencies: Collection<PluginDependency>
        get() = dependencyMap.values

    final override fun dependency(id: String): PluginDependency? = dependencyMap[id]

    companion object {

        private const val DESCRIPTION = "A plugin representing the server. Used for internal things, such as service implementations."
        @JvmField
        val SERVER: KryptonPluginDescription =
            KryptonPluginDescription("krypton", "Krypton", KryptonPlatform.version, DESCRIPTION, setOf("KryptonMC"), emptySet(), Path.of(""))
    }
}
