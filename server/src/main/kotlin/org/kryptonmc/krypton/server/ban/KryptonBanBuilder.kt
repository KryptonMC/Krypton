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
import org.kryptonmc.api.user.ban.BanTypes
import org.kryptonmc.krypton.util.asString
import java.net.InetAddress
import java.time.OffsetDateTime

class KryptonBanBuilder(private var type: BanType) : Ban.Builder {

    private var profile: GameProfile? = null
    private var address: InetAddress? = null
    private var source: Component? = null
    private var reason: Component? = null
    private var start: OffsetDateTime? = null
    private var end: OffsetDateTime? = null

    override fun profile(profile: GameProfile): Ban.Builder = apply {
        require(type === BanTypes.PROFILE) { "Ban type must be set to PROFILE to set the profile!" }
        this.profile = profile
    }

    override fun address(address: InetAddress): Ban.Builder = apply {
        require(type === BanTypes.IP) { "Ban type must be set to IP to set the address!" }
        this.address = address
    }

    override fun type(type: BanType): Ban.Builder = apply { this.type = type }

    override fun source(source: Component): Ban.Builder = apply { this.source = source }

    override fun reason(reason: Component?): Ban.Builder = apply { this.reason = reason }

    override fun creationDate(date: OffsetDateTime): Ban.Builder = apply { start = date }

    override fun expirationDate(date: OffsetDateTime?): Ban.Builder = apply { end = date }

    override fun build(): Ban = when (type) {
        BanTypes.PROFILE -> {
            checkNotNull(profile) { "Profile cannot be null when type is PROFILE!" }
            BannedPlayerEntry(profile!!, start ?: OffsetDateTime.now(), source ?: BanEntry.DEFAULT_SOURCE, end, reason ?: BanEntry.DEFAULT_REASON)
        }
        BanTypes.IP -> {
            checkNotNull(address) { "Address cannot be null when type is IP!" }
            val start = start ?: OffsetDateTime.now()
            BannedIpEntry(address!!.asString(), start, source ?: BanEntry.DEFAULT_SOURCE, end, reason ?: BanEntry.DEFAULT_REASON)
        }
        else -> error("Unsupported ban type $type!")
    }
}
