/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/meta/PluginDependency.java
 */
package org.kryptonmc.api.plugin

import org.kryptonmc.internal.annotations.ImmutableType
import java.nio.file.Path

/**
 * Represents the plugin.conf file that every plugin has.
 *
 * This holds details about the plugin, for example it's name, main class,
 * version, description, authors and dependencies.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface PluginDescription {

    /**
     * The unique ID of this plugin.
     */
    @get:JvmName("id")
    public val id: String

    /**
     * The name of this plugin. Defaults to empty.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The version of this plugin. Defaults to <UNDEFINED>.
     */
    @get:JvmName("version")
    public val version: String

    /**
     * A short description of this plugin.
     */
    @get:JvmName("description")
    public val description: String

    /**
     * The list of people who created this plugin.
     */
    @get:JvmName("authors")
    public val authors: Collection<String>

    /**
     * The list of dependencies of this plugin.
     */
    @get:JvmName("dependencies")
    public val dependencies: Collection<PluginDependency>

    /**
     * The source path that this plugin was loaded from.
     */
    @get:JvmName("source")
    public val source: Path

    /**
     * Gets the plugin dependency with the given [id], or returns null if there
     * is no dependency with the given [id].
     *
     * @param id the ID of the dependency
     * @return the dependency, or null if not present
     */
    public fun dependency(id: String): PluginDependency?
}
