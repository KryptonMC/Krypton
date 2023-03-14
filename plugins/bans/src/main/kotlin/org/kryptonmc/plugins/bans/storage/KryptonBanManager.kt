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

    override fun banProfile(ban: ProfileBan): Boolean {
        if (isBanned(ban.profile)) return false
        val event = server.eventNode.fire(BanProfileEvent(ban))
        if (!event.isAllowed()) return false
        storage.addBan(ban)
        return true
    }

    override fun banIp(ban: IpBan): Boolean {
        if (isBanned(ban.ip)) return false
        val event = server.eventNode.fire(BanIpEvent(ban))
        if (!event.isAllowed()) return false
        storage.addBan(ban)
        return true
    }

    fun removeBan(profile: GameProfile) {
        if (!isBanned(profile)) return
        storage.removeBan(profile)
    }

    fun removeBan(ip: String) {
        if (!isBanned(ip)) return
        storage.removeBan(ip)
    }

    override fun pardonProfile(profile: GameProfile): Boolean {
        if (!isBanned(profile)) return false
        val event = server.eventNode.fire(PardonProfileEvent(storage.getBan(profile)!!))
        if (!event.isAllowed()) return false
        storage.removeBan(profile)
        return true
    }

    override fun pardonIp(ip: String): Boolean {
        if (!isBanned(ip)) return false
        val event = server.eventNode.fire(PardonIpEvent(storage.getBan(ip)!!))
        if (!event.isAllowed()) return false
        storage.removeBan(ip)
        return true
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
