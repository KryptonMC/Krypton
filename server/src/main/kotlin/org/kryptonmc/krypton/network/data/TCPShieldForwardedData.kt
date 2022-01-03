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

@JvmRecord
data class TCPShieldForwardedData(
    override val forwardedAddress: String,
    override val forwardedPort: Int,
    val timestamp: Int,
    val signature: String
) : ForwardedData {

    companion object {

        @JvmStatic
        fun parse(string: String): TCPShieldForwardedData? {
            val split = string.split("///")
            if (split.size < 4) return null
            val ipData = split[1].split(':')
            if (ipData.size != 2) return null

            val ip = ipData[0]
            val port = ipData[1].toIntOrNull() ?: return null
            val timestamp = split[2].toIntOrNull() ?: return null
            val signature = split[3]
            return TCPShieldForwardedData(ip, port, timestamp, signature)
        }
    }
}
