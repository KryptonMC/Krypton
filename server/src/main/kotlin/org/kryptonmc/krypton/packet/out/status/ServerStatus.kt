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

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import me.bardy.gsonkt.getAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.auth.KryptonGameProfile

class ServerStatus(
    val motd: Component,
    val players: Players,
    val favicon: String?
) {

    class Players(
        val max: Int,
        var online: Int,
        var sample: Array<KryptonGameProfile> = emptyArray()
    ) {

        companion object : TypeAdapter<Players>() {

            override fun read(`in`: JsonReader?): Players {
                throw UnsupportedOperationException("Reading server status players is not supported!")
            }

            override fun write(out: JsonWriter, value: Players) {
                out.beginObject()
                out.name("max")
                out.value(value.max)
                out.name("online")
                out.value(value.online)
                if (value.sample.isNotEmpty()) {
                    out.name("sample")
                    out.beginArray()
                    value.sample.forEach {
                        out.beginObject()
                        out.name("id")
                        out.value(it.uuid.toString())
                        out.name("name")
                        out.value(it.name)
                        out.endObject()
                    }
                    out.endArray()
                }
                out.endObject()
            }
        }
    }

    companion object : TypeAdapter<ServerStatus>() {

        private val COMPONENT_ADAPTER = GsonComponentSerializer.gson().serializer().getAdapter<Component>()

        override fun read(`in`: JsonReader?): ServerStatus {
            throw UnsupportedOperationException("Reading server status is not supported!")
        }

        override fun write(out: JsonWriter, value: ServerStatus) {
            out.beginObject()
            if (value.motd !== Component.empty()) {
                out.name("description")
                COMPONENT_ADAPTER.write(out, value.motd)
            }
            out.name("players")
            Players.write(out, value.players)
            out.name("version")
            out.beginObject()
            out.name("name")
            out.value(KryptonPlatform.minecraftVersion)
            out.name("protocol")
            out.value(KryptonPlatform.protocolVersion)
            out.endObject()
            value.favicon?.let {
                out.name("favicon")
                out.value(it)
            }
            out.endObject()
        }
    }
}
