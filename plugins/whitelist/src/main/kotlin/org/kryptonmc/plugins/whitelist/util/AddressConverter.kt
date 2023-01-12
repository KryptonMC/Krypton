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
package org.kryptonmc.plugins.whitelist.util

import java.net.SocketAddress

object AddressConverter {

    @JvmStatic
    fun asString(address: SocketAddress): String = formatAddressString(address.toString())

    @JvmStatic
    private fun formatAddressString(input: String): String {
        var temp = input
        if (temp.contains('/')) temp = temp.substring(temp.indexOf('/') + 1)
        if (temp.contains(':')) temp = temp.substring(0, temp.indexOf(':'))
        return temp
    }
}
