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
package org.kryptonmc.krypton.server.whitelist

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.user.whitelist.RemoveWhitelistedIpEvent
import org.kryptonmc.api.event.user.whitelist.RemoveWhitelistedProfileEvent
import org.kryptonmc.api.event.user.whitelist.WhitelistIpEvent
import org.kryptonmc.api.event.user.whitelist.WhitelistProfileEvent
import org.kryptonmc.api.user.whitelist.WhitelistService
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.server.whitelist.WhitelistEntry
import org.kryptonmc.krypton.server.whitelist.WhitelistIpEntry
import org.kryptonmc.krypton.util.asString
import java.net.InetAddress

class KryptonWhitelistService(private val server: KryptonServer) : WhitelistService {

    override val isEnabled: Boolean
        get() = server.playerManager.whitelistEnabled

    override fun isWhitelisted(profile: GameProfile): Boolean = server.playerManager.whitelist.contains(profile)

    override fun isWhitelisted(address: InetAddress): Boolean = server.playerManager.whitelistedIps.contains(address.asString())

    override fun add(profile: GameProfile) {
        server.eventManager.fire(WhitelistProfileEvent(profile))
            .thenApplyAsync { if (it.result.isAllowed) server.playerManager.whitelist.add(WhitelistEntry(profile)) }
    }

    override fun add(address: InetAddress) {
        server.eventManager.fire(WhitelistIpEvent(address))
            .thenApplyAsync { if (it.result.isAllowed) server.playerManager.whitelistedIps.add(WhitelistIpEntry(address.asString())) }
    }

    override fun remove(profile: GameProfile) {
        server.eventManager.fire(RemoveWhitelistedProfileEvent(profile))
            .thenApplyAsync { if (it.result.isAllowed) server.playerManager.whitelist.remove(profile) }
    }

    override fun remove(address: InetAddress) {
        server.eventManager.fire(RemoveWhitelistedIpEvent(address))
            .thenApplyAsync { if (it.result.isAllowed) server.playerManager.whitelistedIps.remove(address.asString()) }
    }
}
