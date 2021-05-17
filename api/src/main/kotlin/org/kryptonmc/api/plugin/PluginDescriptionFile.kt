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
 * Represents the plugin.conf file that every plugin has.
 *
 * This holds details about the plugin, for example it's name, main class, version, descrpiton,
 * authors and dependencies
 */
data class PluginDescriptionFile(
    val name: String,
    val main: String,
    val version: String,
    val description: String = "",
    val authors: List<String> = emptyList(),
    val dependencies: List<String> = emptyList()
)
