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
package org.kryptonmc.krypton.server.whitelist

import com.google.gson.JsonObject
import org.kryptonmc.krypton.server.ServerConfigList
import java.net.SocketAddress
import java.nio.file.Path

class WhitelistedIps(path: Path) : ServerConfigList<String, WhitelistIpEntry>(path) {

    override fun fromJson(data: JsonObject) = WhitelistIpEntry(data.get("ip").asString)

    fun isWhitelisted(adress: SocketAddress) = contains(stringifyAddress(adress))

    operator fun get(key: SocketAddress) = super.get(stringifyAddress(key))

    private fun stringifyAddress(socketAddress: SocketAddress): String {
        var string = socketAddress.toString()
        if (string.contains("/")) {
            string = string.substring(string.indexOf(47.toChar()) + 1)
        }
        if (string.contains(":")) {
            string = string.substring(0, string.indexOf(58.toChar()))
        }
        return string
    }
}
