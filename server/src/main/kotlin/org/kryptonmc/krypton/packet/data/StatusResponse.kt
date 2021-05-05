/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.serializers.ComponentSerializer
import org.kryptonmc.krypton.serializers.UUIDSerializer
import java.util.UUID

/**
 * A serializable class sent as the response to a status request. Specifically, this is serialized
 * to JSON and sent by the [Status response][org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse]
 * packet.
 */
@Serializable
data class StatusResponse(
    val version: ServerVersion,
    val players: Players,
    @Serializable(with = ComponentSerializer::class) val description: Component
)

/**
 * Information about the server's version. This is used by the Notchian client to determine whether
 * we are compatible with it.
 */
@Serializable
data class ServerVersion(
    val name: String,
    val protocol: Int
)

/**
 * The players list. This is used by the Notchian client to display the current, maximum and sample
 * players currently on the server.
 */
@Serializable
data class Players(
    val max: Int,
    val online: Int,
    val sample: Set<PlayerInfo> = emptySet()
)

/**
 * A player's info, for status
 */
@Serializable
data class PlayerInfo(
    val name: String,
    @SerialName("id") @Serializable(with = UUIDSerializer::class) val uuid: UUID
)
