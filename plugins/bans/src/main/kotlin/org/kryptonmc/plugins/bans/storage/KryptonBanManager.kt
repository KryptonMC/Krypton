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
package org.kryptonmc.plugins.bans.storage

import com.google.common.collect.Collections2
import org.kryptonmc.api.Server
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.bans.api.BanManager
import org.kryptonmc.plugins.bans.api.IpBan
import org.kryptonmc.plugins.bans.api.ProfileBan
import org.kryptonmc.plugins.bans.event.BanIpEvent
import org.kryptonmc.plugins.bans.event.BanProfileEvent
import org.kryptonmc.plugins.bans.event.PardonIpEvent
import org.kryptonmc.plugins.bans.event.PardonProfileEvent
import java.util.Collections
import java.util.concurrent.CompletableFuture

class KryptonBanManager(private val server: Server, private val storage: BanStorage) : BanManager {

    private val profileNames = Collections.unmodifiableCollection(Collections2.transform(storage.profileBans()) { it.profile.name })
    private val ipStrings = Collections.unmodifiableCollection(Collections2.transform(storage.ipBans()) { it.ip })

    override fun isBanned(profile: GameProfile): Boolean = storage.isBanned(profile)

    override fun isBanned(ip: String): Boolean = storage.isBanned(ip)

    override fun getBan(profile: GameProfile): ProfileBan? = storage.getBan(profile)

    override fun getBan(ip: String): IpBan? = storage.getBan(ip)

    fun addBan(ban: ProfileBan) {
        if (isBanned(ban.profile)) return
        storage.addBan(ban)
    }

    fun addBan(ban: IpBan) {
        if (isBanned(ban.ip)) return
        storage.addBan(ban)
    }

    override fun banProfile(ban: ProfileBan): CompletableFuture<Boolean> {
        if (isBanned(ban.profile)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(BanProfileEvent(ban)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.addBan(ban)
            true
        }
    }

    override fun banIp(ban: IpBan): CompletableFuture<Boolean> {
        if (isBanned(ban.ip)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(BanIpEvent(ban)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.addBan(ban)
            true
        }
    }

    fun removeBan(profile: GameProfile) {
        if (!isBanned(profile)) return
        storage.removeBan(profile)
    }

    fun removeBan(ip: String) {
        if (!isBanned(ip)) return
        storage.removeBan(ip)
    }

    override fun pardonProfile(profile: GameProfile): CompletableFuture<Boolean> {
        if (!isBanned(profile)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(PardonProfileEvent(storage.getBan(profile)!!)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.removeBan(profile)
            true
        }
    }

    override fun pardonIp(ip: String): CompletableFuture<Boolean> {
        if (!isBanned(ip)) return CompletableFuture.completedFuture(false)
        return server.eventManager.fire(PardonIpEvent(storage.getBan(ip)!!)).thenApply {
            if (!it.result.isAllowed) return@thenApply false
            storage.removeBan(ip)
            true
        }
    }

    override fun allProfileBans(): Collection<ProfileBan> = Collections.unmodifiableCollection(storage.profileBans())

    override fun allIpBans(): Collection<IpBan> = Collections.unmodifiableCollection(storage.ipBans())

    fun profileNames(): Collection<String> = profileNames

    fun ipStrings(): Collection<String> = ipStrings

    fun load() {
        storage.load()
    }

    fun save() {
        storage.saveIfNeeded()
    }
}
