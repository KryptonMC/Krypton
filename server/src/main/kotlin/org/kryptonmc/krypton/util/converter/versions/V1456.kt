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
package org.kryptonmc.krypton.util.converter.versions

import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1456 {

    private const val VERSION = MCVersions.V17W49B + 1

    fun register() = MCTypeRegistry.ENTITY.addConverterForId("minecraft:item_frame", VERSION) { data, _, _ ->
        data.setByte("Facing", direction2dTo3d(data.getByte("Facing")))
        null
    }

    private fun direction2dTo3d(old: Byte) = when (old) {
        0.toByte() -> 3.toByte()
        1.toByte() -> 4.toByte()
        3.toByte() -> 5.toByte()
        else -> 2.toByte()
    }
}
