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
package org.kryptonmc.krypton.user

import net.kyori.adventure.identity.Identity
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.user.User
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.nbt.CompoundTag
import java.time.Instant
import java.util.UUID

class KryptonUser(override val profile: GameProfile, var data: CompoundTag, private val server: KryptonServer) : User {

    private val identity = Identity.identity(uuid)
    private var firstJoinedCached: Instant? = null
    private var lastJoinedCached: Instant? = null

    override val name: String
        get() = profile.name
    override val uuid: UUID
        get() = profile.uuid
    override val isOnline: Boolean
        get() = player != null
    override val player: KryptonPlayer?
        get() = server.player(uuid)
    override val hasJoinedBefore: Boolean
        get() {
            val player = player
            if (player != null) return player.hasJoinedBefore
            return data.isNotEmpty()
        }
    override val firstJoined: Instant
        get() {
            val player = player
            if (player != null) return player.firstJoined
            if (firstJoinedCached != null) return firstJoinedCached!!
            val instant = Instant.ofEpochMilli(data.getLong("firstJoined"))
            firstJoinedCached = instant
            return instant
        }
    override val lastJoined: Instant
        get() {
            val player = player
            if (player != null) return player.lastJoined
            if (lastJoinedCached != null) return lastJoinedCached!!
            val instant = Instant.ofEpochMilli(data.getLong("lastJoined"))
            lastJoinedCached = instant
            return instant
        }
    override val isVanished: Boolean
        get() = player?.isVanished ?: false

    override fun identity(): Identity = identity
}
