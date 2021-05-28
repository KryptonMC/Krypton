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
package org.kryptonmc.krypton.plugin

import com.google.inject.Guice
import org.kryptonmc.api.plugin.Plugin
import org.kryptonmc.api.plugin.PluginContext
import org.kryptonmc.api.plugin.PluginDescriptionFile
import org.kryptonmc.api.plugin.PluginLoadState
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.module.GlobalModule
import org.kryptonmc.krypton.module.PluginModule
import org.kryptonmc.krypton.util.createDirectory
import org.kryptonmc.krypton.util.list
import org.kryptonmc.krypton.util.logger
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.FileNotFoundException
import java.lang.reflect.InvocationTargetException
import java.nio.file.Path
import java.util.jar.JarFile
import kotlin.io.path.isDirectory

class KryptonPluginManager(private val server: KryptonServer) : PluginManager {

    private val _plugins = object : MutableMap<Plugin, PluginContext> by mutableMapOf() {

        override fun get(key: Plugin) = getValue(key)
    }

    override val plugins: Set<Plugin>
        get() = _plugins.keys

    internal fun initialize() {
        Messages.PLUGIN.LOAD.INITIAL.info(LOGGER)
        CURRENT_DIRECTORY.resolve("plugins").apply {
            createDirectory()
            list().filter { !it.isDirectory() }.forEach {
                val (plugin, context) = load(this, it) ?: return@forEach

                val name = context.description.name
                val version = context.description.version
                Messages.PLUGIN.LOAD.SUCCESS.info(LOGGER, name, version)

                Messages.PLUGIN.INIT.START.info(LOGGER, name, version)
                plugin.initialize()
                Messages.PLUGIN.INIT.SUCCESS.info(LOGGER, name, version)
                plugin.loadState = PluginLoadState.INITIALIZED

                _plugins[plugin] = context
            }
        }
        Messages.PLUGIN.LOAD.DONE.info(LOGGER)
    }

    override fun isInitialized(plugin: Plugin) = plugin in plugins && plugin.loadState == PluginLoadState.INITIALIZED

    // if the cast here fails, something is seriously wrong
    override fun addToClasspath(plugin: Plugin, path: Path) = (plugin.javaClass.classLoader as PluginClassLoader).addPath(path)

    private fun load(folder: Path, path: Path): Pair<Plugin, PluginContext>? {
        val description = loadDescription(path)
        Messages.PLUGIN.LOAD.START.info(LOGGER, description.name, description.version)

        val dataFolder = folder.resolve(description.name).createDirectory()
        val context = PluginContext(
            server,
            dataFolder,
            description,
            logger("[${description.name}]")
        )

        val loader = PluginClassLoader(path)
        return try {
            val pluginClass = loader.loadClass(description.main).asSubclass(Plugin::class.java)
            Guice.createInjector(GlobalModule(server), PluginModule(context)).getInstance(pluginClass) to context
        } catch (exception: Exception) {
            when (exception) {
                is ClassNotFoundException -> Messages.PLUGIN.LOAD.ERROR.NO_MAIN.error(LOGGER, description.name, exception)
                is ClassCastException -> Messages.PLUGIN.LOAD.ERROR.DOES_NOT_EXTEND.error(LOGGER, description.name, exception)
                is NoSuchMethodException -> Messages.PLUGIN.LOAD.ERROR.NO_CONSTRUCTOR.error(LOGGER, description.name, exception)
                is IllegalAccessException -> Messages.PLUGIN.LOAD.ERROR.CONSTRUCTOR_CLOSED.error(LOGGER, description.name, exception)
                is InstantiationException -> Messages.PLUGIN.LOAD.ERROR.ABSTRACT.error(LOGGER, description.name, exception)
                is InvocationTargetException -> Messages.PLUGIN.LOAD.ERROR.INSTANTIATION.error(LOGGER, description.name, exception)
                else -> Messages.PLUGIN.LOAD.ERROR.UNEXPECTED.error(LOGGER, description.name, exception)
            }
            Messages.PLUGIN.SHUTDOWN.START.info(LOGGER, description.name, description.version)
            null
        }
    }

    private fun loadDescription(path: Path): PluginDescriptionFile = JarFile(path.toFile()).use { jar ->
        val entry = jar.getJarEntry("plugin.conf") ?: throw FileNotFoundException("Plugin's JAR does not contain a plugin.conf!")
        jar.getInputStream(entry).use { input ->
            val loader = HoconConfigurationLoader.builder().source { input.bufferedReader() }.build()
            val node = loader.load()
            val name = node.node("name").string ?: throw IllegalArgumentException("No name attribute in plugin.conf!")
            val main = node.node("main").string ?: throw IllegalArgumentException("No main attribute in plugin.conf!")
            val version = node.node("version").string ?: throw IllegalArgumentException("No version attribute in plugin.conf!")
            val description = node.node("description").getString("")
            val authors = node.node("authors").getList(String::class.java, emptyList())
            val dependencies = node.node("dependencies").getList(String::class.java, emptyList())
            PluginDescriptionFile(name, main, version, description, authors, dependencies)
        }
    }

    fun shutdown() = _plugins.forEach { (plugin, context) ->
        plugin.loadState = PluginLoadState.SHUTTING_DOWN
        Messages.PLUGIN.SHUTDOWN.START.info(LOGGER, context.description.name, context.description.version)
        plugin.shutdown()
        plugin.loadState = PluginLoadState.SHUT_DOWN
        Messages.PLUGIN.SHUTDOWN.SUCCESS.info(LOGGER, context.description.name, context.description.version)
    }

    fun contextOf(plugin: Plugin) = _plugins[plugin]

    companion object {

        private val LOGGER = logger("PluginManager")
    }
}
