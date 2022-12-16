/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.user

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.user.User
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.nbt.CompoundTag
import java.time.Instant
import java.util.UUID

class KryptonUser(override val profile: GameProfile, var data: CompoundTag, private val server: KryptonServer) : User {

    private var firstJoinedCached: Instant? = null
    private var lastJoinedCached: Instant? = null

    override val name: String
        get() = profile.name
    override val uuid: UUID
        get() = profile.uuid
    override val isOnline: Boolean
        get() = asPlayer() != null
    override val hasJoinedBefore: Boolean
        get() = asPlayer()?.hasJoinedBefore ?: !data.isEmpty
    override val firstJoined: Instant
        get() = asPlayer()?.firstJoined ?: getAndUpdateJoined("firstJoined", firstJoinedCached) { firstJoinedCached = it }
    override val lastJoined: Instant
        get() = asPlayer()?.lastJoined ?: getAndUpdateJoined("lastJoined", lastJoinedCached) { lastJoinedCached = it }
    override val isVanished: Boolean
        get() = asPlayer()?.isVanished ?: false

    override fun asPlayer(): Player? = server.getPlayer(uuid)

    private inline fun getAndUpdateJoined(key: String, cached: Instant?, updateCached: (Instant) -> Unit): Instant {
        if (cached != null) return cached
        val instant = Instant.ofEpochMilli(data.getLong(key))
        updateCached(instant)
        return instant
    }
}
