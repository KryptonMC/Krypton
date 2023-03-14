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
package org.kryptonmc.plugins.whitelist.storage

import org.kryptonmc.api.Server
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.whitelist.api.WhitelistManager
import org.kryptonmc.plugins.whitelist.event.RemoveWhitelistedIpEvent
import org.kryptonmc.plugins.whitelist.event.RemoveWhitelistedProfileEvent
import org.kryptonmc.plugins.whitelist.event.WhitelistIpEvent
import org.kryptonmc.plugins.whitelist.event.WhitelistProfileEvent
import java.util.Collections

class KryptonWhitelistManager(private val server: Server, private val storage: WhitelistStorage) : WhitelistManager {

    override fun isWhitelisted(profile: GameProfile): Boolean = storage.isWhitelisted(profile)

    override fun isWhitelisted(ip: String): Boolean = storage.isWhitelisted(ip)

    override fun whitelistProfile(profile: GameProfile): Boolean {
        if (isWhitelisted(profile)) return false
        val event = server.eventNode.fire(WhitelistProfileEvent(profile))
        if (!event.isAllowed()) return false
        storage.whitelist(profile)
        return true
    }

    override fun whitelistIp(ip: String): Boolean {
        if (isWhitelisted(ip)) return false
        val event = server.eventNode.fire(WhitelistIpEvent(ip))
        if (!event.isAllowed()) return false
        storage.whitelist(ip)
        return true
    }

    override fun removeWhitelistedProfile(profile: GameProfile): Boolean {
        if (!isWhitelisted(profile)) return false
        val event = server.eventNode.fire(RemoveWhitelistedProfileEvent(profile))
        if (!event.isAllowed()) return false
        storage.removeWhitelisted(profile)
        return true
    }

    override fun removeWhitelistedIp(ip: String): Boolean {
        if (!isWhitelisted(ip)) return false
        val event = server.eventNode.fire(RemoveWhitelistedIpEvent(ip))
        if (!event.isAllowed()) return false
        storage.removeWhitelisted(ip)
        return true
    }

    override fun allWhitelistedProfiles(): Collection<GameProfile> = Collections.unmodifiableCollection(storage.whitelistedProfiles())

    override fun allWhitelistedIps(): Collection<String> = Collections.unmodifiableCollection(storage.whitelistedIps())

    fun load() {
        storage.load()
    }

    fun save() {
        storage.saveIfNeeded()
    }
}
