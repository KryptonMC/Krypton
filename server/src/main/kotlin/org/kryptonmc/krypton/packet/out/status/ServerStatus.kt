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
package org.kryptonmc.krypton.packet.out.status

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.auth.GameProfile
import java.lang.reflect.Type

class ServerStatus(
    var motd: Component,
    val players: Players,
    val favicon: String?
) {

    class Players(
        val max: Int,
        var online: Int,
        var sample: Array<GameProfile> = emptyArray()
    ) {

        companion object : JsonSerializer<Players> {

            override fun serialize(src: Players, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = JsonObject().apply {
                addProperty("max", src.max)
                addProperty("online", src.online)
                if (src.sample.isNotEmpty()) add("sample", JsonArray().apply {
                    src.sample.forEach {
                        val data = JsonObject().apply {
                            addProperty("id", it.uuid.toString())
                            addProperty("name", it.name)
                        }
                        add(data)
                    }
                })
            }
        }
    }

    companion object : JsonSerializer<ServerStatus> {

        override fun serialize(src: ServerStatus, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = JsonObject().apply {
            if (src.motd != Component.empty()) add("description", context.serialize(src.motd))
            add("players", context.serialize(src.players))
            add("version", JsonObject().apply {
                addProperty("name", KryptonServerInfo.minecraftVersion)
                addProperty("protocol", ServerInfo.PROTOCOL)
            })
            if (src.favicon != null) addProperty("favicon", src.favicon)
        }
    }
}
