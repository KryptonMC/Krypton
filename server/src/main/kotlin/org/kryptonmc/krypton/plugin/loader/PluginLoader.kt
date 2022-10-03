/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/loader/java/JavaPluginLoader.java
 */
package org.kryptonmc.krypton.plugin.loader

import com.google.gson.stream.JsonReader
import com.google.inject.Module
import kotlinx.collections.immutable.toImmutableList
import org.kryptonmc.api.plugin.InvalidPluginException
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.plugin.KryptonPluginContainer
import org.kryptonmc.krypton.plugin.KryptonPluginDependency
import org.kryptonmc.krypton.plugin.PluginClassLoader
import org.kryptonmc.krypton.util.NoSpread
import org.kryptonmc.krypton.util.mapPersistentList
import org.kryptonmc.processor.SerializedPluginDescription
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarInputStream

/**
 * Various utilities for loading and creating plugins.
 */
object PluginLoader {

    @JvmStatic
    fun loadDescription(source: Path): LoadedPluginDescriptionCandidate {
        val serialized = findMetadata(source) ?: throw InvalidPluginException("Could not find a valid krypton-plugin-meta.json file!")
        if (!serialized.id.matches(SerializedPluginDescription.ID_REGEX)) throw InvalidPluginException("Plugin ID ${serialized.id} is invalid!")
        return serialized.toCandidate(source)
    }

    @JvmStatic
    fun loadPlugin(description: PluginDescription): LoadedPluginDescription {
        require(description is LoadedPluginDescriptionCandidate) { "Description provided isn't a loaded candidate!" }
        val loader = PluginClassLoader(description.source).addToLoaders()
        val mainClass = loader.loadClass(description.mainClass)
        return description.toFull(mainClass)
    }

    @JvmStatic
    fun createPlugin(container: PluginContainer, vararg modules: Module) {
        require(container is KryptonPluginContainer) { "Container provided isn't compatible with this loader!" }
        val description = container.description
        require(description is LoadedPluginDescription) { "Description provided isn't compatible with this loader!" }

        val injector = NoSpread.guiceCreateInjector(modules)
        val instance = requireNotNull(injector.getInstance(description.mainClass)) { "Got nothing from injector for plugin ${description.id}!" }

        container.instance = instance
    }

    @JvmStatic
    private fun findMetadata(path: Path): SerializedPluginDescription? = JarInputStream(Files.newInputStream(path)).use { input ->
        var foundBungeeBukkitPluginFile = false
        generateSequence { input.nextJarEntry }.forEach { entry ->
            if (entry.name == "plugin.yml" || entry.name == "bungee.yml") foundBungeeBukkitPluginFile = true
            if (entry.name == "krypton-plugin-meta.json") {
                JsonReader(input.reader()).use { return SerializedPluginDescription.read(it) }
            }
        }

        if (foundBungeeBukkitPluginFile) {
            throw InvalidPluginException("The plugin file ${path.fileName} appears to be a Bukkit or BungeeCord plugin. Krypton does not support" +
                    "Bukkit or BungeeCord plugins.")
        }
        return null
    }
}

private fun SerializedPluginDescription.toCandidate(source: Path): LoadedPluginDescriptionCandidate {
    val dependencies = dependencies.mapPersistentList { KryptonPluginDependency(it.id, it.optional) }
    return LoadedPluginDescriptionCandidate(id, name, version, description, authors.toImmutableList(), dependencies, source, main)
}
