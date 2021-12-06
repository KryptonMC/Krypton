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
package org.kryptonmc.krypton.network.data

import com.google.gson.GsonBuilder
import me.bardy.gsonkt.fromJson
import me.bardy.gsonkt.registerTypeHierarchyAdapter
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import java.util.UUID

@JvmRecord
data class LegacyForwardedData(
    val originalAddress: String,
    override val forwardedAddress: String,
    override val uuid: UUID,
    override val properties: List<KryptonProfileProperty>
) : ForwardedData {

    override val forwardedPort: Int
        get() = -1

    companion object {

        // The reason why we register a final class as a hierarchy adapter is because the
        // properties list above gets resolved as a List<? extends KryptonProfileProperty>
        // on the JVM, and having this use the standard adapter gives us issues in deserialization.
        private val GSON = GsonBuilder()
            .registerTypeHierarchyAdapter<KryptonProfileProperty>(KryptonProfileProperty)
            .create()

        @JvmStatic
        fun parse(string: String): LegacyForwardedData? {
            val split = string.split('\u0000')
            // We need to have the original IP, forwarded IP, and the UUID at bare minimum.
            if (split.size < 3) return null
            return LegacyForwardedData(
                split[0],
                split[1],
                MojangUUIDTypeAdapter.fromString(split[2]),
                if (split.size > 3) GSON.fromJson(split[3]) else emptyList()
            )
        }
    }
}
