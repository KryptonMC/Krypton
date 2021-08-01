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
package org.kryptonmc.krypton.server.ban

import com.google.gson.JsonObject
import org.kryptonmc.krypton.server.ServerConfigList
import org.kryptonmc.krypton.util.stringify
import java.net.SocketAddress
import java.nio.file.Path
import java.time.OffsetDateTime

class BannedIpList(path: Path) : ServerConfigList<String, BannedIpEntry>(path) {

    override fun fromJson(data: JsonObject): BannedIpEntry {
        val entry = BanEntry.fromJson(data.get("ip").asString, data) //Get better null checking
        return BannedIpEntry(entry.key!!, entry.creationDate, entry.source, entry.expiryDate, entry.reason)
    }

    fun isBanned(adress: SocketAddress) = contains(adress.stringify())

    fun clear() {
        forEach {
            it.expiryDate?.let { time ->
                if(time.isAfter(OffsetDateTime.now())) remove(it.key)
            }
        }
    }

    override fun validatePath() {
        super.validatePath()
        clear()
    }

    operator fun get(key: SocketAddress) = super.get(key.stringify())

}
