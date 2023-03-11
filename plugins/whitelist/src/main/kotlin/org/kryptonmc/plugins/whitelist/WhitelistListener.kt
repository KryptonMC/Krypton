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
