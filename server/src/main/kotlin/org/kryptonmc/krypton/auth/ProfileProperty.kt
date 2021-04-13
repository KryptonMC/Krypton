package org.kryptonmc.krypton.auth

import kotlinx.serialization.Serializable

/**
 * Represents a property of a [GameProfile]
 *
 * Only used for skin data
 *
 * @param name the name of the property
 * @param value the value of the property
 * @param signature the Yggdrasil signed hash for this property
 *
 * @author Callum Seabrook
 */
@Serializable
data class ProfileProperty(
    val name: String,
    val value: String,
    val signature: String
)