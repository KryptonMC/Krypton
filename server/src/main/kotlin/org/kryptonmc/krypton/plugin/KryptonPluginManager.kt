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
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/VelocityPluginManager.java
 */
package org.kryptonmc.krypton.plugin

import com.google.inject.Module
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.plugin.loader.PluginLoader
import org.kryptonmc.krypton.plugin.module.GlobalModule
import org.kryptonmc.krypton.plugin.module.PluginModule
import java.nio.file.Files
import java.nio.file.Path
import java.util.Collections
import java.util.IdentityHashMap

class KryptonPluginManager : PluginManager {

    private val pluginMap = LinkedHashMap<String, PluginContainer>()
    private val pluginInstances = IdentityHashMap<Any, PluginContainer>()
    override val plugins: Collection<PluginContainer> = Collections.unmodifiableCollection(pluginMap.values)

    fun loadPlugins(directory: Path, server: KryptonServer) {
        val found = ArrayList<PluginDescription>()

        Files.newDirectoryStream(directory) { Files.isRegularFile(it) && it.toString().endsWith(".jar") }.use { stream ->
            stream.forEach {
                try {
                    val description = PluginLoader.loadDescription(it)
                    if (description.id == "spark") {
                        // If we don't stop the standalone one loading, we could end up with conflicts on the classpath.
                        LOGGER.warn("Ignoring attempt to load standalone Spark plugin, as this plugin is already bundled.")
                        return@forEach
                    }
                    found.add(description)
                } catch (exception: Exception) {
                    LOGGER.error("Failed to load plugin at $it!", exception)
                }
            }
        }
        if (found.isEmpty()) return // no plugins

        val sortedPlugins = PluginDependencies.sortCandidates(found)
        val loadedPluginsById = HashSet<String>()
        val pluginContainers = LinkedHashMap<PluginContainer, Module>()

        // Load the plugins!
        sortedPlugins.forEach pluginLoad@{ candidate ->
            // Verify dependencies
            candidate.dependencies.forEach {
                if (it.isOptional || loadedPluginsById.contains(it.id)) return@forEach
                LOGGER.error("Failed to load plugin ${candidate.id} due to missing dependency on plugin ${it.id}!")
                return@pluginLoad
            }

            try {
                val description = PluginLoader.loadPlugin(candidate)
                val container = KryptonPluginContainer(description)
                pluginContainers.put(container, PluginModule(description, container, directory))
                loadedPluginsById.add(description.id)
            } catch (exception: Exception) {
                LOGGER.error("Failed to create Guice module for plugin ${candidate.id}!", exception)
            }
        }
        val commonModule = GlobalModule(server, pluginContainers.keys)

        pluginContainers.forEach { (container, module) ->
            val description = container.description

            try {
                PluginLoader.createPlugin(container, module, commonModule)
            } catch (exception: Exception) {
                LOGGER.error("Attempting to load plugin ${description.id} generated an exception!", exception)
                return@forEach
            }

            LOGGER.info("Successfully loaded plugin ${description.id} version ${description.version} by ${description.authors.joinToString()}")
            registerPlugin(container)
        }
    }

    private fun registerPlugin(plugin: PluginContainer) {
        pluginMap.put(plugin.description.id, plugin)
        plugin.instance?.let { pluginInstances.put(it, plugin) }
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

    companion object {

        private val LOGGER = LogManager.getLogger("PluginManager")
    }
}
