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
import java.util.Collections

class KryptonBanService(private val server: KryptonServer) : BanService {

    private val banManager = server.playerManager.banManager
    override val profileBans: Collection<Ban.Profile>
        get() = Collections.unmodifiableCollection(banManager.profiles())
    override val ipBans: Collection<Ban.IP>
        get() = Collections.unmodifiableCollection(banManager.ips())

    override fun createBuilder(): Ban.Builder = KryptonBanBuilder()

    override fun isRegistered(ban: Ban): Boolean = when (ban) {
        is Ban.Profile -> banManager.isBanned(ban.profile)
        is Ban.IP -> banManager.isBanned(ban.address.asString())
        else -> error("Unsupported ban type ${ban.type}!")
    }

    override fun get(profile: GameProfile): Ban.Profile? = banManager.get(profile)

    override fun get(address: InetAddress): Ban.IP? = banManager.get(address.asString())

    override fun pardon(profile: GameProfile) {
        get(profile)?.let(::remove)
    }

    override fun pardon(address: InetAddress) {
        get(address)?.let(::remove)
    }

    override fun add(ban: Ban) {
        addOrRemove(ban, ::KryptonBanProfileEvent, ::KryptonBanIpEvent, banManager::add, banManager::add)
    }

    override fun remove(ban: Ban) {
        addOrRemove(ban, ::KryptonPardonProfileEvent, ::KryptonPardonIpEvent, { banManager.remove(it.profile) }, { banManager.remove(it.ip) })
    }

    private inline fun addOrRemove(
        ban: Ban,
        profileEvent: (Ban.Profile) -> BanEvent<Ban.Profile>,
        ipEvent: (Ban.IP) -> BanEvent<Ban.IP>,
        crossinline profileAction: (KryptonProfileBan) -> Unit,
        crossinline ipAction: (KryptonIpBan) -> Unit
    ) {
        when (ban) {
            is KryptonProfileBan -> server.eventManager.fire(profileEvent(ban)).thenAcceptAsync { if (it.result.isAllowed) profileAction(ban) }
            is KryptonIpBan -> server.eventManager.fire(ipEvent(ban)).thenAcceptAsync { if (it.result.isAllowed) ipAction(ban) }
            else -> error("Unsupported ban $ban!")
        }
    }
}
