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
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginDescription.java
 */
package org.kryptonmc.api.plugin

/**
 * Information for a plugin's dependency on another plugin.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PluginDependency {

    /**
     * The ID of the dependency.
     */
    @get:JvmName("id")
    public val id: String

    /**
     * If the dependency is optional or not.
     */
    public val isOptional: Boolean
}
