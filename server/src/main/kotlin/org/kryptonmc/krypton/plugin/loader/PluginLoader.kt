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
package org.kryptonmc.krypton.plugin.loader

import com.google.inject.Guice
import com.google.inject.Module
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.krypton.plugin.PluginClassLoader
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.FileNotFoundException
import java.nio.file.Path
import java.security.AccessController
import java.security.PrivilegedAction
import java.util.jar.JarFile

class PluginLoader {

    fun loadDescription(source: Path) = JarFile(source.toFile()).use { jar ->
        val entry = jar.getJarEntry("plugin.conf") ?: throw FileNotFoundException("Plugin's JAR does not contain a plugin.conf!")
        jar.getInputStream(entry).use {
            val node = HoconConfigurationLoader.builder().source { it.bufferedReader() }.build().load()
            val id = requireNotNull(node.node("id").string) { "No ID specified in plugin.conf!" }
            val name = node.node("name").getString("")
            val main = requireNotNull(node.node("main").string) { "No main key found in plugin.conf!" }
            val version = node.node("version").getString("<UNDEFINED>")
            val description = node.node("description").getString("")
            val authors = node.node("authors").getList(String::class.java, emptyList())
            val dependencies = node.node("dependencies")
            val required = dependencies.node("required").getList(String::class.java, emptyList()).map { PluginDependency(it, false) }
            val optional = dependencies.node("optional").getList(String::class.java, emptyList()).map { PluginDependency(it, true) }
            LoadedPluginDescriptionCandidate(id, name, version, description, authors, required + optional, source, main)
        }
    }

    fun loadPlugin(description: PluginDescription): LoadedPluginDescription {
        require(description is LoadedPluginDescriptionCandidate) { "Description provided isn't a loaded candidate!" }
        val loader = privileged { PluginClassLoader(description.source) }.apply { addToLoaders() }
        val mainClass = loader.loadClass(description.mainClass)
        return description.toFull(mainClass)
    }

    fun createPlugin(description: LoadedPluginDescription, vararg modules: Module): Any {
        val injector = Guice.createInjector(*modules)
        return injector.getInstance(description.mainClass) ?: error("Got nothing from injector for plugin ${description.name}!")
    }
}

private fun <T> privileged(action: () -> T): T = AccessController.doPrivileged(PrivilegedAction(action))
