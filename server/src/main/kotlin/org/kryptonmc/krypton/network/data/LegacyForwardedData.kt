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
package org.kryptonmc.krypton.network.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import java.util.UUID

@JvmRecord
data class LegacyForwardedData(
    val originalAddress: String,
    override val forwardedAddress: String,
    override val uuid: UUID,
    override val properties: PersistentList<ProfileProperty>
) : ForwardedData {

    override val forwardedPort: Int
        get() = -1

    companion object {

        @JvmStatic
        fun parse(string: String): LegacyForwardedData? {
            val split = string.split('\u0000')
            // We need to have the original IP, forwarded IP, and the UUID at bare minimum.
            if (split.size < 3) return null
            val properties = if (split.size > 3) KryptonProfileProperty.fromJsonList(split.get(3)) else persistentListOf()
            return LegacyForwardedData(split.get(0), split.get(1), MojangUUIDTypeAdapter.fromString(split.get(2)), properties)
        }
    }
}
