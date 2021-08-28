/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original files that this file is derived from, see:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginDescription.java
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/meta/PluginDependency.java
 */
package org.kryptonmc.api.plugin

import java.nio.file.Path

/**
 * Represents the plugin.conf file that every plugin has.
 *
 * This holds details about the plugin, for example it's name, main class, version, description,
 * authors and dependencies.
 */
public interface PluginDescription {

    /**
     * The unique ID of this plugin.
     */
    public val id: String

    /**
     * The name of this plugin. Defaults to empty.
     */
    public val name: String

    /**
     * The version of this plugin. Defaults to <UNDEFINED>.
     */
    public val version: String

    /**
     * A short description of this plugin.
     */
    public val description: String

    /**
     * The list of people who created this plugin.
     */
    public val authors: List<String>

    /**
     * The list of dependencies of this plugin.
     */
    public val dependencies: Collection<PluginDependency>

    /**
     * The source path that this plugin was loaded from.
     */
    public val source: Path
}

/**
 * Holder class for plugin dependency metadata.
 *
 * @param id the ID of the dependency
 * @param isOptional if this dependency is optional
 */
public data class PluginDependency(
    public val id: String,
    public val isOptional: Boolean
)
