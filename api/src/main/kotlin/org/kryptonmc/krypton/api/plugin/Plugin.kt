package org.kryptonmc.krypton.api.plugin

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.Flow
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.event.Event
import org.kryptonmc.krypton.api.event.listen
import java.io.File

/**
 * A plugin. These are extensions to the server that can be "plugged in"
 * (hence the name) to provide extra functionality that is not part of
 * the standard server.
 *
 * @author Callum Seabrook
 */
abstract class Plugin(val context: PluginContext) {

    /**
     * The current load state of this plugin
     */
    var loadState = PluginLoadState.INITIALIZING

    /**
     * This is called when the plugin is initialised.
     */
    open fun initialize() {}

    /**
     * Registers the given [command] with the command manager
     */
    fun registerCommand(command: Command) = context.server.commandManager.register(command)

    /**
     * Registers the given [commands] with the command manager
     */
    fun registerCommands(vararg commands: Command) = context.server.commandManager.register(*commands)

    /**
     * Registers the given [commands] with the command manager
     */
    fun registerCommands(commands: Iterable<Command>) = context.server.commandManager.register(commands)

    /**
     * Listens for a specific [Event]
     */
    inline fun <reified T : Event> listen() = context.server.eventManager.listen<T>()

    /**
     * Listens for a specific [Event] and executes the specified [action] when the event is
     * emitted by using [Flow.collect]
     */
    suspend inline fun <reified T : Event> on(crossinline action: suspend (T) -> Unit) = listen<T>().collect(action)
}

data class PluginContext(
    val server: Server,
    val folder: File,
    val description: PluginDescriptionFile,
    val logger: Logger
)