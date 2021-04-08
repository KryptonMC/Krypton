package org.kryptonmc.krypton.plugin

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.plugin.*
import org.kryptonmc.krypton.extension.logger
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.nio.file.Path
import java.util.jar.JarFile

class KryptonPluginManager(private val server: KryptonServer) : PluginManager {

    override val plugins = mutableSetOf<Plugin>()

    internal fun initialize() {
        File(ROOT_FOLDER, "plugins/").apply {
            mkdir()
            (listFiles() ?: return).filter { !it.isDirectory }.forEach {
                val plugin = load(this, it) ?: return@forEach

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
    }

    override fun isInitialized(plugin: Plugin) = plugin in plugins && plugin.loadState == PluginLoadState.INITIALIZED

    // if the cast here fails, something is seriously wrong
    override fun addToClasspath(plugin: Plugin, path: Path) = (plugin.javaClass.classLoader as PluginClassLoader).addPath(path)

    private fun load(folder: File, file: File): Plugin? {
        val description = loadDescription(file)
        LOGGER.info("Loading ${description.name} version ${description.version}...")

        val dataFolder = File(folder, description.name)
        dataFolder.mkdir()

        val context = PluginContext(
            server,
            dataFolder,
            description,
            logger("[${description.name}]")
        )

        val loader = PluginClassLoader(file.toURI().toURL())

        return try {
            val jarClass = loader.loadClass(description.main)
            val pluginClass = jarClass.asSubclass(Plugin::class.java)
            pluginClass.getDeclaredConstructor(PluginContext::class.java).newInstance(context)
        } catch (exception: Exception) {
            LOGGER.error(when (exception) {
                is ClassNotFoundException -> "Could not find main class for plugin ${description.name}!"
                is ClassCastException -> "Main class of ${description.name} does not extend Plugin!"
                is NoSuchMethodException -> "Main class of ${description.name} does not have a constructor that accepts a plugin context!"
                is IllegalAccessException -> "Main class of ${description.name}'s primary constructor is not open!"
                is InstantiationException -> "Main class of ${description.name} must not be abstract!"
                is InvocationTargetException -> "Main class of ${description.name} threw an exception!"
                else -> "An unexpected exception occurred when attempting to load the main class of ${description.name}"
            }, exception)
            LOGGER.info("Shutting down ${description.name} version ${description.version}...")
            null
        }
    }

    private fun loadDescription(file: File): PluginDescriptionFile = JarFile(file).use { jar ->
        val entry = jar.getJarEntry("plugin.conf")
            ?: throw FileNotFoundException("Plugin's JAR does not contain a plugin.conf!")
        jar.getInputStream(entry).use {
            HOCON.decodeFromConfig(ConfigFactory.parseReader(InputStreamReader(it)))
        }
    }

    fun shutdown() = plugins.forEach {
        it.loadState = PluginLoadState.SHUTTING_DOWN
        LOGGER.info("Shutting down ${it.context.description.name} version ${it.context.description.version}")
        it.shutdown()
        it.loadState = PluginLoadState.SHUT_DOWN
    }

    companion object {

        private val HOCON = Hocon {}
        private val ROOT_FOLDER = Path.of("").toAbsolutePath().toFile()

        private val LOGGER = logger("PluginManager")
    }
}