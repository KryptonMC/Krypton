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
package org.kryptonmc.plugins.bans

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.player.LoginEvent
import org.kryptonmc.plugins.bans.commands.BanCommandUtil
import org.kryptonmc.plugins.bans.storage.KryptonBanManager
import org.kryptonmc.plugins.bans.util.AddressConverter

class BanListener(private val logger: Logger, private val banManager: KryptonBanManager) {

    @Listener
    fun onLogin(event: LoginEvent) {
        println("Login event called")
        if (banManager.isBanned(event.profile)) {
            val ban = banManager.getBan(event.profile)!!
            event.denyWithResult(LoginEvent.Result(BanCommandUtil.createBanMessage("banned", ban.reason, ban.expirationDate)))
            logger.info("${event.profile.name} was disconnected as they are banned from this server.")
            return
        }
        val ip = AddressConverter.asString(event.address)
        if (banManager.isBanned(ip)) {
            val ban = banManager.getBan(ip)!!
            event.denyWithResult(LoginEvent.Result(BanCommandUtil.createBanMessage("banned_ip", ban.reason, ban.expirationDate)))
            logger.info("${event.profile.name} was disconnected as their IP address is banned from this server.")
        }
    }
}
