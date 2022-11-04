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
package org.kryptonmc.krypton.server.ban

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.user.ban.BanEvent
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.api.user.ban.BanService
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.event.user.ban.KryptonBanIpEvent
import org.kryptonmc.krypton.event.user.ban.KryptonBanProfileEvent
import org.kryptonmc.krypton.event.user.ban.KryptonPardonIpEvent
import org.kryptonmc.krypton.event.user.ban.KryptonPardonProfileEvent
import org.kryptonmc.krypton.util.asString
import java.net.InetAddress

class KryptonBanService(private val server: KryptonServer) : BanService {

    override val profileBans: Collection<Ban.Profile>
        get() = server.playerManager.bannedPlayers.values
    override val ipBans: Collection<Ban.IP>
        get() = server.playerManager.bannedIps.values

    override fun createBuilder(): Ban.Builder = KryptonBanBuilder()

    override fun isRegistered(ban: Ban): Boolean = when (ban) {
        is Ban.Profile -> server.playerManager.bannedPlayers.contains(ban.profile)
        is Ban.IP -> server.playerManager.bannedIps.contains(ban.address.asString())
        else -> error("Unsupported ban type ${ban.type}!")
    }

    override fun get(profile: GameProfile): Ban.Profile? = server.playerManager.bannedPlayers.get(profile)

    override fun get(address: InetAddress): Ban.IP? = server.playerManager.bannedIps.get(address.asString())

    override fun pardon(profile: GameProfile) {
        val ban = get(profile) ?: return
        remove(ban)
    }

    override fun pardon(address: InetAddress) {
        val ban = get(address) ?: return
        remove(ban)
    }

    override fun add(ban: Ban) {
        addOrRemove(ban, ::KryptonBanProfileEvent, ::KryptonBanIpEvent, BannedPlayerList::add, BannedIpList::add)
    }

    override fun remove(ban: Ban) {
        addOrRemove(ban, ::KryptonPardonProfileEvent, ::KryptonPardonIpEvent, BannedPlayerList::remove, BannedIpList::remove)
    }

    private inline fun addOrRemove(
        ban: Ban,
        profileEvent: (Ban.Profile) -> BanEvent<Ban.Profile>,
        ipEvent: (Ban.IP) -> BanEvent<Ban.IP>,
        crossinline profileAction: (BannedPlayerList, BannedPlayerEntry) -> Unit,
        crossinline ipAction: (BannedIpList, BannedIpEntry) -> Unit
    ) {
        when (ban) {
            is BannedPlayerEntry -> server.eventManager.fire(profileEvent(ban))
                .thenAcceptAsync { if (it.result.isAllowed) profileAction(server.playerManager.bannedPlayers, ban) }
            is BannedIpEntry -> server.eventManager.fire(ipEvent(ban))
                .thenAcceptAsync { if (it.result.isAllowed) ipAction(server.playerManager.bannedIps, ban) }
            else -> error("Unsupported ban $ban!")
        }
    }
}
