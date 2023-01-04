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
package org.kryptonmc.krypton.commands

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.server.ban.KryptonBan
import java.time.OffsetDateTime
import java.util.regex.Pattern

/**
 * Common code shared between ban, ban-ip, pardon, and pardon-ip.
 */
object BanCommandUtil {

    // https://www.oreilly.com/library/view/regular-expressions-cookbook/9780596802837/ch07s16.html
    @JvmField
    val IP_ADDRESS_PATTERN: Pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")

    @JvmStatic
    fun createBanMessage(key: String, reason: Component, expiry: OffsetDateTime?): Component {
        val result = Component.text()
        result.append(Component.translatable("multiplayer.disconnect.$key.reason", reason))
        if (expiry != null) {
            val expiration = Component.text(KryptonBan.DATE_FORMATTER.format(expiry))
            result.append(Component.translatable("multiplayer.disconnect.$key.expiration", expiration))
        }
        return result.build()
    }
}
