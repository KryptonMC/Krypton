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
import me.bardy.gsonkt.fromJson
import org.kryptonmc.api.plugin.InvalidPluginException
import org.kryptonmc.api.plugin.PluginDependency
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.api.plugin.ap.SerializedDependency
import org.kryptonmc.api.plugin.ap.SerializedPluginDescription
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.plugin.PluginClassLoader
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.security.AccessController
import java.security.PrivilegedAction
import java.util.jar.JarInputStream
import kotlin.io.path.inputStream

class PluginLoader {

    fun loadDescription(source: Path): LoadedPluginDescriptionCandidate {
        val serialized = source.findMetadata() ?: throw InvalidPluginException("Could not find a valid krypton-plugin-meta.json or plugin.conf file!")
        if (!serialized.id.matches(SerializedPluginDescription.ID_REGEX)) throw InvalidPluginException("Plugin ID ${serialized.id} is invalid!")
        return serialized.toCandidate(source)
    }

    fun loadPlugin(description: PluginDescription): LoadedPluginDescription {
        require(description is LoadedPluginDescriptionCandidate) { "Description provided isn't a loaded candidate!" }
        val loader = doPrivileged { PluginClassLoader(description.source) }.apply { addToLoaders() }
        val mainClass = loader.loadClass(description.mainClass)
        return description.toFull(mainClass)
    }

    fun createPlugin(description: LoadedPluginDescription, vararg modules: Module): Any {
        val injector = Guice.createInjector(*modules)
        return injector.getInstance(description.mainClass) ?: error("Got nothing from injector for plugin ${description.name}!")
    }

    private fun Path.findMetadata(): SerializedPluginDescription? = JarInputStream(BufferedInputStream(inputStream())).use { input ->
        var foundBungeeBukkitPluginFile = false
        generateSequence { input.nextJarEntry }.forEach { entry ->
            if (entry.name == "plugin.yml" || entry.name == "bungee.yml") foundBungeeBukkitPluginFile = true
            if (entry.name == "plugin.conf") return input.readPluginConfig()
            if (entry.name == "krypton-plugin-meta.json") InputStreamReader(input, Charsets.UTF_8).use { return GSON.fromJson(it) }
        }

        if (foundBungeeBukkitPluginFile) throw InvalidPluginException("The plugin file $fileName appears to be a Bukkit or BungeeCord plugin. Krypton does not support Bukkit or BungeeCord plugins.")
        return null
    }

    private fun InputStream.readPluginConfig(): SerializedPluginDescription = reader().use {
        val node = HoconConfigurationLoader.builder().source { it.buffered() }.build().load()
        val id = node.node("id").string ?: throw InvalidPluginException("No ID specified in plugin.conf!")
        val name = node.node("name").getString("")
        val main = node.node("main").string ?: throw InvalidPluginException("No main key found in plugin.conf!")
        val version = node.node("version").getString("<UNDEFINED>")
        val description = node.node("description").getString("")
        val authors = node.node("authors").getList(String::class.java, emptyList())
        val dependencies = node.node("dependencies")
        val required = dependencies.node("required").getList(String::class.java, emptyList()).map { SerializedDependency(it, false) }
        val optional = dependencies.node("optional").getList(String::class.java, emptyList()).map { SerializedDependency(it, true) }
        return SerializedPluginDescription(id, name, version, description, authors, required + optional, main)
    }

    private fun SerializedPluginDescription.toCandidate(source: Path) = LoadedPluginDescriptionCandidate(
        id,
        name ?: "",
        version ?: "<UNDEFINED>",
        description ?: "",
        authors ?: emptyList(),
        dependencies?.map { PluginDependency(it.id, it.optional) } ?: emptyList(),
        source,
        main
    )
}

private fun <T> doPrivileged(action: () -> T): T = AccessController.doPrivileged(PrivilegedAction(action))
