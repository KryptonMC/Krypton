package org.kryptonmc.krypton.api.plugin

import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.command.Command
import java.nio.file.Path

/**
 * A plugin. These are extensions to the server that can be "plugged in"
 * (hence the name) to provide extra functionality that is not part of
 * the standard server.
 */
abstract class Plugin(val context: PluginContext) {

    /**
     * The current load state of this plugin
     */
    var loadState = PluginLoadState.INITIALIZING

    /**
     * This is called when the plugin is initialised.
     *
     * This function should be preferred over the init block, as this
     * is called within its own plugin scope, whereas the init block is
     * called synchronously.
     */
    open fun initialize() {}

    /**
     * This is called when the plugin is shut down.
     */
    open fun shutdown() {}

    /**
     * Registers the given [command] with the command manager
     *
     * @param command the command to register
     */
    fun registerCommand(command: Command) = context.server.commandManager.register(command)

    /**
     * Registers the given [commands] with the command manager
     *
     * @param commands the commands to register
     */
    fun registerCommands(vararg commands: Command) = context.server.commandManager.register(*commands)

    /**
     * Registers the given [commands] with the command manager
     *
     * @param commands the commands to register
     */
    fun registerCommands(commands: Iterable<Command>) = context.server.commandManager.register(commands)

    /**
     * Register a new event listener
     *
     * @param listener the listener to register
     */
    fun registerListener(listener: Any) = context.server.eventBus.register(listener)
}

/**
 * Holder for the context that a plugin was loaded in.
 *
 * @param server the server this plugin is being plugged in to
 * @param folder the folder that this plugin can use to store files that it may need, e.g. configs
 * @param description an object representation of this plugin's plugin.conf file
 * @param logger the logger instance for this plugin
 */
data class PluginContext(
    val server: Server,
    val folder: Path,
    val description: PluginDescriptionFile,
    val logger: Logger
)
