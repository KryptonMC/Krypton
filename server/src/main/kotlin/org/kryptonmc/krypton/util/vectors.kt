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

import com.mojang.serialization.Codec
import org.kryptonmc.api.util.log2
import org.kryptonmc.api.util.roundUpPow2
import org.spongepowered.math.vector.Vector3i
import java.util.stream.IntStream

val VECTOR3I_CODEC: Codec<Vector3i> = Codec.INT_STREAM.comapFlatMap(
    { stream -> stream.fixedSize(3).map { Vector3i(it[0], it[1], it[2]) } },
    { IntStream.of(it.x(), it.y(), it.z()) }
).stable()

val PACKED_X_Z = 1 + 30000000.roundUpPow2().log2()
val PACKED_Y = 64 - PACKED_X_Z * 2
private val PACKED_X_Z_MASK = (1L shl PACKED_X_Z) - 1L
private val PACKED_Y_MASK = (1L shl PACKED_Y) - 1L
private val X_OFFSET = PACKED_Y + PACKED_X_Z
private val Z_OFFSET = PACKED_Y

val Long.x: Int
    get() = (this shl 64 - X_OFFSET - PACKED_X_Z shr 64 - PACKED_X_Z).toInt()

val Long.y: Int
    get() = (this shl 64 - PACKED_Y shr 64 - PACKED_Y).toInt()

val Long.z: Int
    get() = (this shl 64 - Z_OFFSET - PACKED_X_Z shr 64 - PACKED_X_Z).toInt()

fun asLong(x: Int, y: Int, z: Int): Long {
    var temp = 0L
    temp = temp or (x.toLong() and PACKED_X_Z_MASK shl X_OFFSET)
    temp = temp or (y.toLong() and PACKED_Y_MASK)
    return temp or (z.toLong() and PACKED_X_Z_MASK shl Z_OFFSET)
}
