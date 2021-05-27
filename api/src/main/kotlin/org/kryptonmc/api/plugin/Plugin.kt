/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.Command
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

    // Helpers
    val server = context.server
    val folder = context.folder
    val description = context.description
    val logger = context.logger

    /**
     * This is called when the plugin is initialised.
     */
    open fun initialize() {}

    /**
     * This is called when the plugin is shut down.
     */
    open fun shutdown() {}

    /**
     * Register the given [command] with the command manager
     *
     * @param command the command to register
     */
    fun registerCommand(command: Command) = context.server.commandManager.register(command)

    /**
     * Register the given [commands] with the command manager
     *
     * @param commands the commands to register
     */
    fun registerCommands(vararg commands: Command) = context.server.commandManager.register(*commands)

    /**
     * Register the given [commands] with the command manager
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

    /**
     * Register a service with the [services manager][org.kryptonmc.api.service.ServicesManager].
     *
     * @param clazz the class of the service
     * @param service the service instance
     */
    fun <T> registerService(clazz: Class<T>, service: T) = server.servicesManager.register(this, clazz, service)
}

/**
 * Register a service with the [services manager][org.kryptonmc.api.service.ServicesManager].
 * This is a helper function that uses type reification.
 *
 * @param service the service instance
 */
inline fun <reified T> Plugin.registerService(service: T) = registerService(T::class.java, service)

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
