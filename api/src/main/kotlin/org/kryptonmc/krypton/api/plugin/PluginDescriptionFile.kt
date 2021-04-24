package org.kryptonmc.krypton.api.plugin

import kotlinx.serialization.Serializable

/**
 * Represents the plugin.conf file that every plugin has.
 *
 * This holds details about the plugin, for example it's name, main class, version, descrpiton,
 * authors and dependencies
 */
@Serializable
data class PluginDescriptionFile(
    val name: String,
    val main: String,
    val version: String,
    val description: String = "",
    val authors: List<String> = emptyList(),
    val dependencies: List<String> = emptyList()
)
