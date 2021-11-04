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
package org.kryptonmc.krypton.server.ban

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.api.user.ban.BanManager
import org.kryptonmc.api.user.ban.BanTypes
import org.kryptonmc.api.event.user.ban.BanIpEvent
import org.kryptonmc.api.event.user.ban.BanProfileEvent
import org.kryptonmc.api.event.user.ban.PardonIpEvent
import org.kryptonmc.api.event.user.ban.PardonProfileEvent
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.asString
import java.net.InetAddress

class KryptonBanManager(private val server: KryptonServer) : BanManager {

    override val profileBans: Collection<Ban.Profile>
        get() = server.playerManager.bannedPlayers.values
    override val ipBans: Collection<Ban.IP>
        get() = server.playerManager.bannedIps.values

    override fun contains(ban: Ban): Boolean = when (ban.type) {
        BanTypes.PROFILE -> server.playerManager.bannedPlayers.contains((ban as Ban.Profile).profile)
        BanTypes.IP -> server.playerManager.bannedIps.contains((ban as Ban.IP).address.asString())
        else -> error("Unsupported ban type ${ban.type}!")
    }

    override fun get(profile: GameProfile): Ban.Profile? = server.playerManager.bannedPlayers[profile]

    override fun get(address: InetAddress): Ban.IP? = server.playerManager.bannedIps[address.asString()]

    override fun pardon(profile: GameProfile) {
        val ban = get(profile) ?: return
        remove(ban)
    }

    override fun pardon(address: InetAddress) {
        val ban = get(address) ?: return
        remove(ban)
    }

    override fun add(ban: Ban) {
        if (ban !is BanEntry<*>) return
        when (ban.type) {
            BanTypes.PROFILE -> server.eventManager.fire(BanProfileEvent(ban as BannedPlayerEntry))
                .thenApplyAsync { if (it.result.isAllowed) server.playerManager.bannedPlayers.add(ban) }
            BanTypes.IP -> server.eventManager.fire(BanIpEvent(ban as BannedIpEntry))
                .thenApplyAsync { if (it.result.isAllowed) server.playerManager.bannedIps.add(ban) }
            else -> error("Unsupported ban type ${ban.type}!")
        }
    }

    override fun remove(ban: Ban) {
        if (ban !is BanEntry<*>) return
        when (ban.type) {
            BanTypes.PROFILE -> server.eventManager.fire(PardonProfileEvent(ban as BannedPlayerEntry))
                .thenApplyAsync { if (it.result.isAllowed) server.playerManager.bannedPlayers.remove(ban.key) }
            BanTypes.IP -> server.eventManager.fire(PardonIpEvent(ban as BannedIpEntry))
                .thenApplyAsync { if (it.result.isAllowed) server.playerManager.bannedIps.remove(ban.key) }
            else -> error("Unsupported ban type ${ban.type}!")
        }
    }
}
