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

import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World

/**
 * Convert a [Vector] to a single long with its X coordinate packed into the most significant 26
 * bits, its Z packed into the middle 26 bits, and its Y packed into the least significant 12 bits
 *
 * @author Callum Seabrook
 */
fun Position.toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

/**
 * Convert a single long to a [Vector]
 *
 * @author Callum Seabrook
 */
fun Long.toVector() = Vector((this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())

/**
 * @see Long.toVector
 */
fun Long.toLocation(world: World) = Location(world, (this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())
