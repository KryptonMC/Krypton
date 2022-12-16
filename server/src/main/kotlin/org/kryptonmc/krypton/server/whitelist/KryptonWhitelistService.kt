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
import org.kryptonmc.api.user.whitelist.WhitelistService
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.event.user.whitelist.KryptonRemoveWhitelistedIpEvent
import org.kryptonmc.krypton.event.user.whitelist.KryptonRemoveWhitelistedProfileEvent
import org.kryptonmc.krypton.event.user.whitelist.KryptonWhitelistIpEvent
import org.kryptonmc.krypton.event.user.whitelist.KryptonWhitelistProfileEvent
import org.kryptonmc.krypton.util.AddressUtil
import java.net.InetAddress

class KryptonWhitelistService(private val server: KryptonServer) : WhitelistService {

    private val whitelist = server.playerManager.whitelistManager
    override val isEnabled: Boolean
        get() = whitelist.isEnabled()

    override fun isWhitelisted(profile: GameProfile): Boolean = whitelist.isWhitelisted(profile)

    override fun isWhitelisted(address: InetAddress): Boolean = whitelist.isWhitelisted(AddressUtil.asString(address))

    override fun whitelist(profile: GameProfile) {
        server.eventManager.fire(KryptonWhitelistProfileEvent(profile)).thenApplyAsync { if (it.result.isAllowed) whitelist.add(profile) }
    }

    override fun whitelist(address: InetAddress) {
        server.eventManager.fire(KryptonWhitelistIpEvent(address))
            .thenApplyAsync { if (it.result.isAllowed) whitelist.add(AddressUtil.asString(address)) }
    }

    override fun removeWhitelisted(profile: GameProfile) {
        server.eventManager.fire(KryptonRemoveWhitelistedProfileEvent(profile))
            .thenApplyAsync { if (it.result.isAllowed) whitelist.remove(profile) }
    }

    override fun removeWhitelisted(address: InetAddress) {
        server.eventManager.fire(KryptonRemoveWhitelistedIpEvent(address))
            .thenApplyAsync { if (it.result.isAllowed) whitelist.remove(AddressUtil.asString(address)) }
    }
}
