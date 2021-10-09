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
 * A wrapper around a loaded plugin, that may be injected to gain access to
 * some details that may otherwise be unavailable.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PluginContainer {

    /**
     * The description of this plugin. This contains information that describes
     * this plugin.
     *
     * For more information, see [PluginDescription].
     */
    @get:JvmName("description")
    public val description: PluginDescription

    /**
     * The instance of the main class that has been created and injected in to.
     *
     * This may be null in the case that this is injected in to the constructor
     * of your main class, due to the instance not being available until that
     * injection taking place.
     * This option here avoids the chicken and egg problem that would otherwise
     * ensue.
     */
    @get:JvmName("instance")
    public val instance: Any?
}
