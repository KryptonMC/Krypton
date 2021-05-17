/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin

import java.nio.file.Path

/**
 * The plugin manager.
 */
interface PluginManager {

    /**
     * The list of currently loaded plugins
     */
    val plugins: Set<Plugin>

    /**
     * If this plugin is present in the list and is enabled, this will
     * return true. Otherwise, it will return false.
     *
     * @param plugin the plugin to check
     * @return if the plugin is loaded and enabled
     */
    fun isInitialized(plugin: Plugin): Boolean

    /**
     * Add the specified [path] to the server's classpath using the given
     * [plugin]'s class loader
     *
     * @param plugin the plugin who's loader to load the [path] with
     * @param path the path to load
     */
    fun addToClasspath(plugin: Plugin, path: Path)
}
