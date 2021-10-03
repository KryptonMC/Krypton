/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginContainer.java
 */
package org.kryptonmc.api.plugin

/**
 * A wrapper around a loaded plugin.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PluginContainer {

    /**
     * The description of this plugin.
     */
    @get:JvmName("description")
    public val description: PluginDescription

    /**
     * The instance of this loaded plugin if it is available, null otherwise.
     */
    @get:JvmName("instance")
    public val instance: Any?
}
