/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin

/**
 * A wrapper around a loaded plugin.
 */
interface PluginContainer {

    /**
     * The description of this plugin.
     */
    val description: PluginDescription

    /**
     * The instance of this loaded plugin if it is available, null otherwise.
     */
    val instance: Any?
}
