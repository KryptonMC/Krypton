package org.kryptonmc.krypton.api.plugin

import kotlinx.serialization.Serializable

@Serializable
data class PluginDescriptionFile(
    val name: String,
    val main: String,
    val version: String,
    val description: String = "",
    val authors: List<String> = emptyList(),
    val dependencies: List<String> = emptyList(),
    val permissions: Map<String, PermissionEntry> = emptyMap()
)

@Serializable
data class PermissionEntry(
    val default: Boolean = false,
    val children: Map<String, PermissionEntry> = emptyMap()
)