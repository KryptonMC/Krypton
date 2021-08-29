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

import com.google.gson.stream.JsonReader
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.server.ServerConfigList
import java.nio.file.Path
import java.util.UUID

class Whitelist(path: Path) : ServerConfigList<KryptonGameProfile, WhitelistEntry>(path) {

    override fun read(reader: JsonReader): WhitelistEntry? {
        reader.beginObject()

        var name: String? = null
        var uuid: UUID? = null
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "name" -> name = reader.nextString()
                "uuid" -> uuid = UUID.fromString(reader.nextString())
            }
        }

        reader.endObject()
        if (name == null || uuid == null) return null
        return WhitelistEntry(KryptonGameProfile(uuid, name, emptyList()))
    }
}
