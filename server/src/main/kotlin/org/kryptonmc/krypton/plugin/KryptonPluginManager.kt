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

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.plugin.Plugin
import org.kryptonmc.api.plugin.PluginContext
import org.kryptonmc.api.plugin.PluginDescriptionFile
import org.kryptonmc.api.plugin.PluginLoadState
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.createDirectory
import org.kryptonmc.krypton.util.list
import org.kryptonmc.krypton.util.logger
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import java.io.FileNotFoundException
import java.lang.reflect.InvocationTargetException
import java.nio.file.Path
import java.util.jar.JarFile
import kotlin.io.path.isDirectory

class KryptonPluginManager(private val server: KryptonServer) : PluginManager {

    override val plugins = mutableSetOf<Plugin>()

    internal fun initialize() {
        Messages.PLUGIN.LOAD.INITIAL.info(LOGGER)
        CURRENT_DIRECTORY.resolve("plugins").apply {
            createDirectory()
            list().filter { !it.isDirectory() }.forEach {
                val plugin = load(this, it) ?: return@forEach

                val name = plugin.context.description.name
                val version = plugin.context.description.version
                Messages.PLUGIN.LOAD.SUCCESS.info(LOGGER, name, version)

                Messages.PLUGIN.INIT.START.info(LOGGER, name, version)
                plugin.initialize()
                Messages.PLUGIN.INIT.SUCCESS.info(LOGGER, name, version)
                plugin.loadState = PluginLoadState.INITIALIZED

                plugins += plugin
            }
        }
        Messages.PLUGIN.LOAD.DONE.info(LOGGER)
    }

    override fun isInitialized(plugin: Plugin) = plugin in plugins && plugin.loadState == PluginLoadState.INITIALIZED

    // if the cast here fails, something is seriously wrong
    override fun addToClasspath(plugin: Plugin, path: Path) = (plugin.javaClass.classLoader as PluginClassLoader).addPath(path)

    private fun load(folder: Path, path: Path): Plugin? {
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
            val jarClass = loader.loadClass(description.main)
            val pluginClass = jarClass.asSubclass(Plugin::class.java)
            pluginClass.getDeclaredConstructor(PluginContext::class.java).newInstance(context)
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
            loader.load().get()!!
        }
    }

    fun shutdown() = plugins.forEach {
        it.loadState = PluginLoadState.SHUTTING_DOWN
        Messages.PLUGIN.SHUTDOWN.START.info(LOGGER, it.context.description.name, it.context.description.version)
        it.shutdown()
        it.loadState = PluginLoadState.SHUT_DOWN
        Messages.PLUGIN.SHUTDOWN.SUCCESS.info(LOGGER, it.context.description.name, it.context.description.version)
    }

    companion object {

        private val LOGGER = logger("PluginManager")
    }
}
