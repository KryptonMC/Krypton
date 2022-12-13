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
package org.kryptonmc.krypton.server.ban

import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.api.user.ban.BanType
import org.kryptonmc.krypton.util.AddressUtil
import java.net.InetAddress
import java.time.OffsetDateTime

class KryptonBanBuilder : Ban.Builder, Ban.Builder.EndStep {

    private var type: BanType? = null
    private var profile: GameProfile? = null
    private var address: InetAddress? = null
    private var source: Component = KryptonBan.DEFAULT_SOURCE
    private var reason: Component = KryptonBan.DEFAULT_REASON
    private var start: OffsetDateTime? = null
    private var end: OffsetDateTime? = null

    override fun profile(profile: GameProfile): KryptonBanBuilder = apply {
        type = BanType.PROFILE
        this.profile = profile
    }

    override fun address(address: InetAddress): KryptonBanBuilder = apply {
        type = BanType.IP
        this.address = address
    }

    override fun source(source: Component): KryptonBanBuilder = apply { this.source = source }

    override fun reason(reason: Component?): KryptonBanBuilder = apply { this.reason = reason ?: KryptonBan.DEFAULT_REASON }

    override fun creationDate(date: OffsetDateTime): KryptonBanBuilder = apply { start = date }

    override fun expirationDate(date: OffsetDateTime?): KryptonBanBuilder = apply { end = date }

    override fun build(): Ban = when (type!!) {
        BanType.PROFILE -> KryptonProfileBan(profile!!, source, reason, start ?: OffsetDateTime.now(), end)
        BanType.IP -> KryptonIpBan(AddressUtil.asString(address!!), source, reason, start ?: OffsetDateTime.now(), end)
    }
}
