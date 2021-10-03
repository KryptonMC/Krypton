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

import org.spongepowered.math.vector.Vector3i

fun Vector3i.asLong(): Long {
    var temp = 0L
    temp = temp or (x().toLong() and Vectors.PACKED_X_Z_MASK shl Vectors.X_OFFSET)
    temp = temp or (y().toLong() and Vectors.PACKED_Y_MASK)
    return temp or (z().toLong() and Vectors.PACKED_X_Z_MASK shl Vectors.Z_OFFSET)
}

fun Long.toVector(): Vector3i = Vector3i(
    (this shl 64 - Vectors.X_OFFSET - Vectors.PACKED_X_Z shr 64 - Vectors.PACKED_X_Z).toInt(),
    (this shl 64 - Vectors.PACKED_Y shr 64 - Vectors.PACKED_Y).toInt(),
    (this shl 64 - Vectors.Z_OFFSET - Vectors.PACKED_X_Z shr 64 - Vectors.PACKED_X_Z).toInt()
)

fun Long.decodeBlockPosition() = Vector3i((this shr 38).toInt(), (this and 0xFFF).toInt(), (this shl 26 shr 38).toInt())
