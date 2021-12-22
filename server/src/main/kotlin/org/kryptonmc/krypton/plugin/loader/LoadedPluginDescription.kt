/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/proxy/src/main/java/com/velocitypowered/proxy/plugin/loader/java/JavaVelocityPluginDescription.java
 */
package org.kryptonmc.krypton.plugin.loader

import kotlinx.collections.immutable.ImmutableCollection
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import java.nio.file.Path

@JvmRecord
data class LoadedPluginDescription(
    override val id: String,
    override val name: String,
    override val version: String,
    override val description: String,
    override val authors: ImmutableCollection<String>,
    override val dependencies: ImmutableCollection<PluginDependency>,
    override val source: Path,
    val mainClass: Class<*>
) : PluginDescription
