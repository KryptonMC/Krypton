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

fun Vector3i.toProtocol() = x().toLong() and 0x3FFFFFF shl 38 or (z().toLong() and 0x3FFFFFF shl 12) or (y().toLong() and 0xFFF)

fun Long.toVector3i() = Vector3i((this shr 38).toInt(), (this and 0xFFF).toInt(), (this shl 26 shr 38).toInt())
