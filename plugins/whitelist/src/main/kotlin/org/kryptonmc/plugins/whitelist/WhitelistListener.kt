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
package org.kryptonmc.plugins.whitelist

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.player.PlayerLoginEvent
import org.kryptonmc.plugins.whitelist.storage.WhitelistStorage
import org.kryptonmc.plugins.whitelist.util.AddressConverter

class WhitelistListener(private val logger: Logger, private val storage: WhitelistStorage) {

    @Listener
    fun onLogin(event: PlayerLoginEvent) {
        if (!storage.isEnabled()) return

        // Check if the profile is whitelisted
        if (storage.isWhitelisted(event.profile)) return

        // Check if the profile is IP whitelisted
        val address = AddressConverter.asString(event.address)
        if (storage.isWhitelisted(address)) return

        // Not whitelisted, throw them out
        event.denyWithResult(PlayerLoginEvent.Result(Component.translatable("multiplayer.disconnect.not_whitelisted")))
        logger.info("${event.profile.name} was disconnected as this server is whitelisted and they are not on the whitelist.")
    }
}
