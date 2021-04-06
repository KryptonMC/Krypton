package org.kryptonmc.krypton.registry.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.kryptonmc.krypton.api.registry.NamespacedKey

@Serializable
data class RegistryEntry(
    @SerialName("protocol_id") val id: Int,
    val entries: Map<NamespacedKey, RegistryIdHolder>
)

@Serializable
data class RegistryIdHolder(
    @SerialName("protocol_id") val id: Int
)