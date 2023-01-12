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
package org.kryptonmc.plugins.whitelist.storage

import org.kryptonmc.api.Server
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.whitelist.api.WhitelistManager
import org.kryptonmc.plugins.whitelist.event.RemoveWhitelistedIpEvent
import org.kryptonmc.plugins.whitelist.event.RemoveWhitelistedProfileEvent
import org.kryptonmc.plugins.whitelist.event.WhitelistIpEvent
import org.kryptonmc.plugins.whitelist.event.WhitelistProfileEvent
import java.util.Collections
import java.util.concurrent.CompletableFuture

class KryptonWhitelistManager(private val server: Server, private val storage: WhitelistStorage) : WhitelistManager {

    override fun isWhitelisted(profile: GameProfile): Boolean = storage.isWhitelisted(profile)

    override fun isWhitelisted(ip: String): Boolean = storage.isWhitelisted(ip)

    override fun whitelistProfile(profile: GameProfile): CompletableFuture<Boolean> {
        if (isWhitelisted(profile)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(WhitelistProfileEvent(profile)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.whitelist(profile)
            true
        }
    }

    override fun whitelistIp(ip: String): CompletableFuture<Boolean> {
        if (isWhitelisted(ip)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(WhitelistIpEvent(ip)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.whitelist(ip)
            true
        }
    }

    override fun removeWhitelistedProfile(profile: GameProfile): CompletableFuture<Boolean> {
        if (!isWhitelisted(profile)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(RemoveWhitelistedProfileEvent(profile)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.removeWhitelisted(profile)
            true
        }
    }

    override fun removeWhitelistedIp(ip: String): CompletableFuture<Boolean> {
        if (!isWhitelisted(ip)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(RemoveWhitelistedIpEvent(ip)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.removeWhitelisted(ip)
            true
        }
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
