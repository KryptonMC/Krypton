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
     * The source path that this plugin was loaded from, or null if this plugin
     * was not loaded from a file.
     */
    @get:JvmName("source")
    public val source: Path?

    /**
     * Gets the plugin dependency with the given [id], or returns null if there
     * is no dependency with the given [id].
     *
     * @param id the ID of the dependency
     * @return the dependency, or null if not present
     */
    public fun getDependency(id: String): PluginDependency?
}
