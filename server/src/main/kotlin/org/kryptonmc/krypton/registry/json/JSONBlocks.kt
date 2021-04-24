package org.kryptonmc.krypton.registry.json

import kotlinx.serialization.Serializable

@Serializable
data class RegistryBlock(
    val properties: Map<String, List<String>> = emptyMap(),
    val states: List<RegistryBlockState>
)

@Serializable
data class RegistryBlockState(
    val id: Int,
    val default: Boolean = false,
    val properties: Map<String, String> = emptyMap()
)
