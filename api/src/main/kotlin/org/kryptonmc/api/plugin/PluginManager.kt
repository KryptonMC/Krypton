/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the Apache License v2.0
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginManager.java
 */
package org.kryptonmc.api.plugin

import java.nio.file.Path

/**
 * The plugin manager.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PluginManager {

    /**
     * The list of currently loaded plugins.
     */
    public val plugins: Collection<PluginContainer>

    /**
     * Get the plugin container for the specified plugin [instance], or null if
     * there isn't one.
     *
     * @param instance the plugin instance
     * @return the container for the instance, or null if there isn't one
     */
    public fun fromInstance(instance: Any): PluginContainer?

    /**
     * Get a plugin's container by its [id], or null if there isn't a plugin
     * loaded with the specified [id].
     *
     * @param id the id of the plugin
     * @return the plugin container with the specified [id], or null if there
     * isn't one
     */
    public fun getPlugin(id: String): PluginContainer?

    /**
     * Check if a plugin with the specified [id] is currently loaded.
     *
     * @param id the unique id of the plugin
     * @return true if there is a plugin with the name and it is loaded, false
     * otherwise
     */
    public fun isLoaded(id: String): Boolean

    /**
     * Add the specified [path] to the server's classpath using the given
     * [plugin]'s class loader.
     *
     * @param plugin the plugin who's loader to load the [path] with
     * @param path the path to load
     */
    public fun addToClasspath(plugin: Any, path: Path)
}
