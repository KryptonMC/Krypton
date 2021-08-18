/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmName("Vectors")
package org.kryptonmc.api.util

import org.spongepowered.math.vector.Vector3i

private val PACKED_X_Z = 1 + 30000000.roundUpPow2().log2()
private val PACKED_Y = 64 - PACKED_X_Z * 2
private val PACKED_X_Z_MASK = (1L shl PACKED_X_Z) - 1L
private val PACKED_Y_MASK = (1L shl PACKED_Y) - 1L
private val X_OFFSET = PACKED_Y + PACKED_X_Z
private val Z_OFFSET = PACKED_Y

/**
 * Encodes this vector in to a single encoded long.
 *
 * Useful for efficiently storing coordinates.
 */
fun Vector3i.asLong(): Long = asLong(x(), y(), z())

/**
 * Decodes this long to an integer vector.
 *
 * Useful for efficiently storing coordinates.
 */
@Suppress("MagicNumber")
@JvmName("fromLong")
fun Long.toVector(): Vector3i = Vector3i(
    (this shl 64 - X_OFFSET - PACKED_X_Z shr 64 - PACKED_X_Z).toInt(),
    (this shl 64 - PACKED_Y shr 64 - PACKED_Y).toInt(),
    (this shl 64 - Z_OFFSET - PACKED_X_Z shr 64 - PACKED_X_Z).toInt()
)

@JvmSynthetic
internal fun asLong(x: Int, y: Int, z: Int): Long {
    var temp = 0L
    temp = temp or (x.toLong() and PACKED_X_Z_MASK shl X_OFFSET)
    temp = temp or (y.toLong() and PACKED_Y_MASK)
    return temp or (z.toLong() and PACKED_X_Z_MASK shl Z_OFFSET)
}
