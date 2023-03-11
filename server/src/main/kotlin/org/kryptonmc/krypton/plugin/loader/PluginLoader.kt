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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.plugin.PluginDependencies
import org.kryptonmc.krypton.plugin.module.GlobalModule
import org.kryptonmc.krypton.plugin.server.ClasspathModuleDiscoverer
import org.kryptonmc.krypton.plugin.server.ServerModules
import org.kryptonmc.krypton.plugin.server.ServerPluginSource
import java.nio.file.Files
import java.nio.file.Path

class PluginLoader(private val pluginManager: KryptonPluginManager, private val sources: List<PluginSource>) {

    fun loadPlugins(server: KryptonServer) {
        val found = ArrayList<PluginDescription>()
        val sourceByPluginId = HashMap<String, PluginSource>()

        sources.forEach { source ->
            val descriptions = source.loadDescriptions()
            found.addAll(descriptions)
            descriptions.forEach { description -> sourceByPluginId.put(description.id, source) }
        }
        if (found.isEmpty()) return // no plugins

        val sortedPlugins = PluginDependencies.sortCandidates(found)
        val loadedPluginIds = HashSet<String>()
        val loadedPlugins = ArrayList<LoadedPlugin>()

        // Load the plugins!
        sortedPlugins.forEach pluginLoad@{ candidate ->
            candidate.dependencies.forEach {
                if (it.isOptional || loadedPluginIds.contains(it.id)) return@forEach
                LOGGER.error("Failed to load plugin ${candidate.id} due to missing dependency on plugin ${it.id}!")
                return@pluginLoad
            }

            val source = sourceByPluginId.get(candidate.id) ?: error("No source for candidate $candidate! This is a bug!")
            try {
                val description = source.loadPlugin(candidate)
                val container = source.createPluginContainer(description)
                val pluginModule = source.createModule(container)
                val eventNode = source.createEventNode(container)
                loadedPlugins.add(LoadedPlugin(container, pluginModule, eventNode))
                loadedPluginIds.add(description.id)
            } catch (exception: Exception) {
                LOGGER.error("Failed to create Guice module for plugin ${candidate.id}!", exception)
            }
        }

        val containers = loadedPlugins.map { it.container }
        val commonModule = GlobalModule(server, containers)

        loadedPlugins.forEach { (container, module, eventNode) ->
            val description = container.description
            val source = sourceByPluginId.get(description.id) ?: error("No source for container plugin ${description.id}! This is a bug!")

            try {
                source.createPlugin(container, module, commonModule)
            } catch (exception: Exception) {
                LOGGER.error("Error whilst attempting to load plugin ${description.id}!", exception)
                return@forEach
            }

            source.onPluginLoaded(container)
            pluginManager.registerPlugin(container, eventNode)
        }
        sources.forEach { source -> source.onPluginsLoaded(containers) }
    }

    @JvmRecord
    private data class LoadedPlugin(val container: PluginContainer, val module: Module, val eventNode: EventNode<Event>)

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        fun createDefault(server: KryptonServer): PluginLoader {
            val sources = ArrayList<PluginSource>()
            createModulesSource(ServerModules.createDefault(server.config.modules.enabled), Path.of("modules"))?.let { sources.add(it) }
            createPluginsSource(Path.of("plugins"))?.let { sources.add(it) }
            return PluginLoader(server.pluginManager, sources)
        }

        @JvmStatic
        private fun createPluginsSource(path: Path): JarPluginSource? {
            if (!Files.exists(path)) {
                try {
                    Files.createDirectory(path)
                } catch (exception: Exception) {
                    LOGGER.warn("Failed to create the plugins directory! Plugins will not be loaded!", exception)
                    return null
                }
            }
            if (!Files.isDirectory(path)) {
                LOGGER.warn("Plugins path $path is not a directory! Plugins will not be loaded!")
                return null
            }
            return JarPluginSource(path)
        }

        @JvmStatic
        private fun createModulesSource(modules: ServerModules, path: Path): ServerPluginSource? {
            if (!Files.exists(path)) {
                try {
                    Files.createDirectory(path)
                } catch (exception: Exception) {
                    LOGGER.warn("Failed to create the modules directory! Modules will not be loaded!", exception)
                    return null
                }
            }
            if (!Files.isDirectory(path)) {
                LOGGER.warn("Modules path $path is not a directory! Modules will not be loaded!")
                return null
            }
            return ServerPluginSource(ClasspathModuleDiscoverer.createDefault(), path, modules)
        }
    }
}
