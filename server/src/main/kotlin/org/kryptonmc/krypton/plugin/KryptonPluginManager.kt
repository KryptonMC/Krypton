/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the Apache License v2.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
