/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import kotlinx.collections.immutable.persistentSetOf
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.KryptonPlatform
import java.nio.file.Path

/**
 * A plugin description that describes the currently running server.
 */
object ServerPluginDescription : PluginDescription {

    override val id: String = "krypton"
    override val name: String = "Krypton"
    override val version: String = KryptonPlatform.version
    override val description: String = "A plugin representing the server."
    override val authors: Set<String> = persistentSetOf("KryptonMC")
    override val dependencies: Set<PluginDependency> = emptySet()
    override val source: Path = Path.of("")
}
