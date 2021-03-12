package org.kryptonmc.krypton.plugin

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.plugin.*
import org.kryptonmc.krypton.extension.logger
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.jar.JarFile

class KryptonPluginManager(private val server: KryptonServer) : PluginManager {

    override val plugins = mutableSetOf<Plugin>()

    private val pluginsFolder = File(ROOT_FOLDER, "/plugins/").apply {
        mkdir()
        walk().filter { !it.isDirectory }.forEach {
            val plugin = load(it) ?: return@forEach

            val name = plugin.context.description.name
            val version = plugin.context.description.version
            LOGGER.info("Successfully loaded $name version $version")

            LOGGER.info("Initializing $name version $version...")
            plugin.initialize()
            LOGGER.info("Successfully initialized $name version $version")
            plugin.loadState = PluginLoadState.INITIALIZED

            plugins += plugin
        }
    }

    override fun isInitialized(plugin: Plugin) = plugin in plugins && plugin.loadState == PluginLoadState.INITIALIZED

    private fun load(file: File): Plugin? {
        val description = loadDescription(file)
        LOGGER.info("Loading ${description.name} version ${description.version}...")

        val dataFolder = File(pluginsFolder, description.name)
        dataFolder.mkdir()

        val context = PluginContext(
            server,
            dataFolder,
            description,
            logger("Plugin - " + description.name)
        )

        val mainClassName = JarFile(file).use {
            return@use it.manifest.mainAttributes.getValue("Main-Class")
                ?: throw IllegalStateException("The manifest does not contain a Main-Class attribute!")
        }

        val loader = URLClassLoader(arrayOf(file.toURI().toURL()))

        return try {
            val jarClass = loader.loadClass(mainClassName)
            val pluginClass = jarClass.asSubclass(Plugin::class.java)

            pluginClass.getDeclaredConstructor(PluginContext::class.java).newInstance(context)
        } catch (exception: ClassNotFoundException) {
            LOGGER.error("Could not find main class for plugin ${description.name}!")
            LOGGER.info("Shutting down ${description.name} version ${description.version}...")
            null
        } catch (exception: ClassCastException) {
            LOGGER.error("Main class of ${description.name} does not extend Plugin!")
            LOGGER.info("Shutting down ${description.name} version ${description.version}")
            null
        }
    }

    private fun loadDescription(file: File): PluginDescriptionFile {
        val jar = JarFile(file)
        val entry = jar.getJarEntry("plugin.conf")
            ?: throw FileNotFoundException("Plugin's JAR does not contain a plugin.conf!")

        val stream = jar.getInputStream(entry)

        try {
            return HOCON.decodeFromConfig(ConfigFactory.parseReader(InputStreamReader(stream)))
        } finally {
            jar.close()
            stream.close()
        }
    }

    companion object {

        private val HOCON = Hocon {}
        private val ROOT_FOLDER = Path.of("").toAbsolutePath().toFile()

        private val LOGGER = logger("PluginManager")
    }
}