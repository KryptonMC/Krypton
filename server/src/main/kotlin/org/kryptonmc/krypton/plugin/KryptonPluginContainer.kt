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
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/loader/VelocityPluginContainer.java
 */
package org.kryptonmc.krypton.plugin

import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription

class KryptonPluginContainer(override val description: PluginDescription) : PluginContainer {

    override var instance: Any? = null
}
