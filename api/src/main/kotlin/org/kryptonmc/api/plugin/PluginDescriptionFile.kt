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
 * This holds details about the plugin, for example it's name, main class, version, description,
 * authors and dependencies.
 *
 * @param name the name of this plugin
 * @param main the main class of this plugin
 * @param version the version of this plugin
 * @param description the description of this plugin, empty if not present
 * @param authors the list of authors for this plugin, empty if not present
 * @param dependencies the list of plugins this plugin depends on
 */
data class PluginDescriptionFile(
    val name: String,
    val main: String,
    val version: String,
    val description: String = "",
    val authors: List<String> = emptyList(),
    val dependencies: List<String> = emptyList()
)
