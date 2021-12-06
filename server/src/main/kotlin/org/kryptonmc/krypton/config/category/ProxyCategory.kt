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
package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

private const val MODE_COMMENT = """
The method to use for forwarding a connecting user's information on
to Krypton from a proxy server. Supported values are:
  - NONE - Disable forwarding support completely
  - LEGACY - Use the BungeeCord/pre-1.13 method
  - MODERN - Use Velocity's modern forwarding protocol
  - TCPSHIELD - Use TCPShield's forwarding protocol
When any mode other than NONE or TCPSHIELD is used, the server will be forced
offline and will ONLY accept connections from proxies. No users will be able to
direct connect.
"""

@ConfigSerializable
@JvmRecord
data class ProxyCategory(
    @Comment(MODE_COMMENT)
    val mode: ForwardingMode = ForwardingMode.NONE,
    @Comment("The forwarding secret from Velocity. Only used in the MODERN forwarding protocol.")
    val secret: String = ""
)

enum class ForwardingMode(val authenticatesUsers: Boolean) {

    NONE(false),
    LEGACY(true),
    MODERN(true),
    TCPSHIELD(false)
}
