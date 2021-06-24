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
package org.kryptonmc.krypton.util.nbt

import net.kyori.adventure.nbt.CompoundBinaryTag
import java.util.UUID

fun CompoundBinaryTag.Builder.putUUID(name: String, uuid: UUID) = apply {
    val most = uuid.mostSignificantBits
    val least = uuid.leastSignificantBits
    putIntArray(name, intArrayOf((most shr 32).toInt(), most.toInt(), (least shr 32).toInt(), least.toInt()))
}

private const val UNSIGNED_INT_MAX_VALUE = 4294967295L

fun CompoundBinaryTag.getUUID(name: String): UUID {
    val array = getIntArray(name)
    require(array.size == 4) { "Expected UUID array to be of length 4, but was ${array.size}!" }
    return UUID(array[0].toLong() shl 32 or (array[1].toLong() and UNSIGNED_INT_MAX_VALUE), array[2].toLong() shl 32 or (array[3].toLong() and UNSIGNED_INT_MAX_VALUE))
}
