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
 * https://github.com/PaperMC/Velocity/blob/959e75d16db352924e679fb5be545ee9b264fbd2/proxy/src/main/java/com/velocitypowered/proxy/plugin/VelocityPluginManager.java
 */
package org.kryptonmc.krypton.plugin

import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.event.KryptonGlobalEventNode
import java.nio.file.Path
import java.util.Collections
import java.util.IdentityHashMap

class KryptonPluginManager : PluginManager {

    private val pluginMap = LinkedHashMap<String, PluginContainer>()
    private val pluginInstances = IdentityHashMap<Any, PluginContainer>()
    private val parentEventNode = createPluginsEventNode()
    private val pluginEventNodes = HashMap<PluginContainer, EventNode<Event>>()

    override val plugins: Collection<PluginContainer> = Collections.unmodifiableCollection(pluginMap.values)

    private fun createPluginsEventNode(): EventNode<Event> {
        val node = EventNode.all("plugins")
        KryptonGlobalEventNode.addChild(node)
        return node
    }

    fun registerPlugin(plugin: PluginContainer, eventNode: EventNode<Event>) {
        pluginMap.put(plugin.description.id, plugin)
        plugin.instance?.let { pluginInstances.put(it, plugin) }

        parentEventNode.addChild(eventNode)
        pluginEventNodes.put(plugin, eventNode)
    }

    fun getEventNode(plugin: PluginContainer): EventNode<Event> {
        return checkNotNull(pluginEventNodes.get(plugin)) { "No event node found for plugin $plugin! This is a bug!" }
    }

    override fun fromInstance(instance: Any): PluginContainer? {
        if (instance is PluginContainer) return instance
        return pluginInstances.get(instance)
    }

    override fun getPlugin(id: String): PluginContainer? = pluginMap.get(id)

    override fun isLoaded(id: String): Boolean = pluginMap.containsKey(id)

    override fun addToClasspath(plugin: Any, path: Path) {
        val container = requireNotNull(fromInstance(plugin)) { "Plugin is not loaded!" }
        val instance = requireNotNull(container.instance) { "Plugin has no instance!" }

        val loader = instance.javaClass.classLoader
        if (loader !is PluginClassLoader) throw UnsupportedOperationException("Operation is not supported for non-Krypton plugins!")
        loader.addPath(path)
    }
}
