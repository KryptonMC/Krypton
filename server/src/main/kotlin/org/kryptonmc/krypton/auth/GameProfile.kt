/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.auth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

/**
 * Represents a Mojang game profile received from the session server on successful
 * authentication of a specific user
 *
 * @param uuid the universally unique identifier for this user
 * @param name the user's username
 * @param properties optional list of properties related to this profile (e.g. skins)
 */
@Serializable
data class GameProfile(
    @SerialName("id") @Serializable(with = MojangUUIDSerializer::class) val uuid: UUID,
    val name: String,
    val properties: List<ProfileProperty>
)

/**
 * Used to (de)serialize the UUID format that Yggdrasil uses (has no dashes in it)
 */
object MojangUUIDSerializer : KSerializer<UUID> {

    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(
        StringBuilder(decoder.decodeString())
            .insert(20, '-')
            .insert(16, '-')
            .insert(12, '-')
            .insert(8, '-')
            .toString())

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(StringBuilder(value.toString())
            .deleteCharAt(8)
            .deleteCharAt(12)
            .deleteCharAt(16)
            .deleteCharAt(20)
            .toString())
    }
}
