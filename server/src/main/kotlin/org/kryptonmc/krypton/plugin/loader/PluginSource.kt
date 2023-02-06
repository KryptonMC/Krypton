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
package org.kryptonmc.krypton.plugin.loader

import com.google.inject.Module
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription

interface PluginSource {

    fun loadDescriptions(): Collection<PluginDescription>

    fun loadPlugin(candidate: PluginDescription): PluginDescription

    fun createPluginContainer(description: PluginDescription): PluginContainer

    fun createModule(container: PluginContainer): Module

    fun createPlugin(container: PluginContainer, vararg modules: Module)

    fun createEventNode(container: PluginContainer): EventNode<Event> {
        return EventNode.all(container.description.id)
    }

    fun onPluginLoaded(container: PluginContainer) {
        // Do nothing by default
    }

    fun onPluginsLoaded(containers: Collection<PluginContainer>) {
        // Do nothing by default
    }
}
