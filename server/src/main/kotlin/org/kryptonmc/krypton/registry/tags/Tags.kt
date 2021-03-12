package org.kryptonmc.krypton.registry.tags

import kotlinx.serialization.Serializable

data class Tag(
    val name: String,
    val data: TagData
)

@Serializable
data class TagData(
    val replace: Boolean,
    val values: List<String>
)