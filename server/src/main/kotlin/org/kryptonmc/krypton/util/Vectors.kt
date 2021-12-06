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

object Vectors {

    private val PACKED_X_Z = 1 + GenericMath.roundUpPow2(30000000).log2()
    val PACKED_Y = 64 - PACKED_X_Z * 2
    val PACKED_X_Z_MASK = (1L shl PACKED_X_Z) - 1L
    val PACKED_Y_MASK = (1L shl PACKED_Y) - 1L
    val X_OFFSET = PACKED_Y + PACKED_X_Z
    val Z_OFFSET = PACKED_Y
}
