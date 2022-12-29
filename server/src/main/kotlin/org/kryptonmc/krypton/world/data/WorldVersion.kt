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
package org.kryptonmc.krypton.world.data

import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.serialization.Dynamic

class WorldVersion(
    val storageVersion: Int,
    val lastPlayed: Long,
    val minecraftVersionName: String,
    val minecraftVersion: DataVersion,
    val snapshot: Boolean
) {

    companion object {

        @JvmStatic
        fun parse(data: Dynamic<*>): WorldVersion {
            val storageVersion = data.get("version").asInt(0)
            val lastPlayed = data.get("LastPlayed").asLong(0L)
            val version = data.get("Version")
            if (!version.result().isPresent) return WorldVersion(storageVersion, lastPlayed, "", DataVersion(0), false)
            return WorldVersion(
                storageVersion,
                lastPlayed,
                version.get("Name").asString(KryptonPlatform.minecraftVersion),
                DataVersion(version.get("Id").asInt(KryptonPlatform.worldVersion), version.get("Series").asString(DataVersion.MAIN_SERIES)),
                version.get("Snapshot").asBoolean(!KryptonPlatform.isStableMinecraft)
            )
        }
    }
}
