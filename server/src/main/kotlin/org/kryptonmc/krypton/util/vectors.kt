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
package org.kryptonmc.krypton.util

import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3i

private val PACKED_X_LENGTH = 1 + GenericMath.roundUpPow2(30000000).log2()
private val PACKED_Z_LENGTH = PACKED_X_LENGTH
val PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH

private val PACKED_X_MASK = (1L shl PACKED_X_LENGTH) - 1L
private val PACKED_Y_MASK = (1L shl PACKED_Y_LENGTH) - 1L
private val PACKED_Z_MASK = (1L shl PACKED_Z_LENGTH) - 1L

private const val Y_OFFSET = 0
private val Z_OFFSET = PACKED_Y_LENGTH
private val X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH

fun Vector3i.asLong() = asLong(x(), y(), z())

fun asLong(x: Int, y: Int, z: Int): Long {
    var temp = 0L
    temp = temp or (x.toLong() and PACKED_X_MASK) shl X_OFFSET
    temp = temp or (y.toLong() and PACKED_Y_MASK) shl Y_OFFSET
    return temp or (z.toLong() and PACKED_Z_MASK) shl Z_OFFSET
}
