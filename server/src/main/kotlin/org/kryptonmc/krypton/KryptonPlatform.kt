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
package org.kryptonmc.krypton

import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import org.kryptonmc.api.Platform

object KryptonPlatform : Platform {

    private val JSON = GSON.fromJson<JsonObject>(Thread.currentThread().contextClassLoader.getResourceAsStream("info.json")?.reader()
        ?: error("Could not find info.json in JAR!"))
    private val KRYPTON_JSON = JSON["krypton"].asJsonObject
    private val MINECRAFT_JSON = JSON["minecraft"].asJsonObject

    override val name = KRYPTON_JSON["name"].asString!!
    override val version = KRYPTON_JSON["version"].asString!!
    override val isStable = KRYPTON_JSON["stable"].asBoolean
    override val minecraftVersion = MINECRAFT_JSON["name"].asString!!
    const val isStableMinecraft = true
    override val worldVersion = JSON["world_version"].asInt
    override val protocolVersion = JSON["protocol_version"].asInt
    override val dataPackVersion = JSON["data_pack_version"].asInt
}
