/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.plugins.bans

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.player.PlayerLoginEvent
import org.kryptonmc.plugins.bans.commands.BanCommandUtil
import org.kryptonmc.plugins.bans.storage.KryptonBanManager
import org.kryptonmc.plugins.bans.util.AddressConverter

class BanListener(private val logger: Logger, private val banManager: KryptonBanManager) {

    @Listener
    fun onLogin(event: PlayerLoginEvent) {
        if (banManager.isBanned(event.profile)) {
            val ban = banManager.getBan(event.profile)!!
            event.denyWithResult(PlayerLoginEvent.Result(BanCommandUtil.createBanMessage("banned", ban.reason, ban.expirationDate)))
            logger.info("${event.profile.name} was disconnected as they are banned from this server.")
            return
        }
        val ip = AddressConverter.asString(event.address)
        if (banManager.isBanned(ip)) {
            val ban = banManager.getBan(ip)!!
            event.denyWithResult(PlayerLoginEvent.Result(BanCommandUtil.createBanMessage("banned_ip", ban.reason, ban.expirationDate)))
            logger.info("${event.profile.name} was disconnected as their IP address is banned from this server.")
        }
    }
}
