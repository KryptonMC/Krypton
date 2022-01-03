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
package org.kryptonmc.krypton.util

import org.spongepowered.math.vector.Vector3i

/**
 * The encoding here is decently efficient. It places the X in the first 26
 * bits (most significant), then the Z in the "middle" 26 bits, followed by
 * the Y value in the last 12 bits (least significant).
 *
 * For example, for X = 13, Y = 2, and Z = 2021, the encoded form would be
 * calculated by:
 * `((x & 0x3FFFFFF) >> 38) | ((Z & 0x3FFFFFF) << 12) | (y & 0xFFF)`.
 *
 * See [here](https://wiki.vg/Protocol#Position)
 */
fun Vector3i.asLong(): Long = ((x().toLong() and 0x3FFFFFF) shl 38) or ((z().toLong() and 0x3FFFFFF) shl 12) or (y().toLong() and 0xFFF)

fun Long.toVector(): Vector3i = Vector3i((this shr 38).toInt(), (this and 0xFFF).toInt(), (this shl 26 shr 38).toInt())

fun Long.decodeBlockX(): Int = (this shr 38).toInt()

fun Long.decodeBlockY(): Int = (this and 0xFFF).toInt()

fun Long.decodeBlockZ(): Int = (this shl 26 shr 38).toInt()
