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
package org.kryptonmc.krypton.packet.out.status

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.KryptonPlatform

@JvmRecord
data class ServerStatus(val motd: Component, val players: Players, val favicon: String?) {

    class Players(val max: Int, var online: Int, var sample: Array<GameProfile> = emptyArray()) {

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

        private val COMPONENT_ADAPTER = GsonComponentSerializer.gson().serializer().getAdapter(Component::class.java)

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
