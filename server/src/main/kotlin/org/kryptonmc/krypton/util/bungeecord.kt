/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util

import me.bardy.gsonkt.fromJson
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.auth.ProfileProperty
import java.util.UUID

fun String.splitData(): BungeeCordHandshakeData? {
    val split = split('\u0000')
    if (split.size <= 2) return null
    return BungeeCordHandshakeData(
        split[0],
        split[1],
        UUID.fromString(split[2].replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5")),
        if (split.size > 3) GSON.fromJson(split[3]) else emptyList()
    )
}

data class BungeeCordHandshakeData(
    val originalIp: String,
    val forwardedIp: String,
    val uuid: UUID,
    val properties: List<ProfileProperty>
)
