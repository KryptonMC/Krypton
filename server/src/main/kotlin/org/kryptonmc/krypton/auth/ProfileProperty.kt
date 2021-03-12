package org.kryptonmc.krypton.auth

import kotlinx.serialization.Serializable

@Serializable
data class ProfileProperty(
    val name: String,
    val value: String,
    val signature: String
)