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
import java.nio.file.Path

/**
 * A plugin. These are extensions to the server that can be "plugged in"
 * (hence the name) to provide extra functionality that is not part of
 * the standard server.
 */
abstract class Plugin {

    /**
     * The current load state of this plugin
     */
    var loadState = PluginLoadState.INITIALIZING

    /**
     * This is called when the plugin is initialised.
     */
    open fun initialize() {}

    /**
     * This is called when the plugin is shut down.
     */
    open fun shutdown() {}
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
