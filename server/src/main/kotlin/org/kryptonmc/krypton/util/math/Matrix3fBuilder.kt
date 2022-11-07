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
package org.kryptonmc.krypton.util.math

import org.spongepowered.math.matrix.Matrix3f

class Matrix3fBuilder {

    private var m00 = 0F
    private var m01 = 0F
    private var m02 = 0F
    private var m10 = 0F
    private var m11 = 0F
    private var m12 = 0F
    private var m20 = 0F
    private var m21 = 0F
    private var m22 = 0F

    fun set(x: Int, y: Int, value: Float): Matrix3fBuilder = apply {
        when (x) {
            0 -> when (y) {
                0 -> m00 = value
                1 -> m01 = value
                else -> m02 = value
            }
            1 -> when (y) {
                0 -> m10 = value
                1 -> m11 = value
                else -> m12 = value
            }
            else -> when (y) {
                0 -> m20 = value
                1 -> m21 = value
                else -> m22 = value
            }
        }
    }

    fun m00(value: Float): Matrix3fBuilder = apply { m00 = value }

    fun m11(value: Float): Matrix3fBuilder = apply { m11 = value }

    fun m22(value: Float): Matrix3fBuilder = apply { m22 = value }

    fun mul(other: Matrix3f): Matrix3fBuilder = apply {
        m00 = m00 * other.get(0, 0) + m01 * other.get(1, 0) + m02 * other.get(2, 0)
        m01 = m00 * other.get(0, 1) + m01 * other.get(1, 1) + m02 * other.get(2, 1)
        m02 = m00 * other.get(0, 2) + m01 * other.get(1, 2) + m02 * other.get(2, 2)
        m10 = m10 * other.get(0, 0) + m11 * other.get(1, 0) + m12 * other.get(2, 0)
        m11 = m10 * other.get(0, 1) + m11 * other.get(1, 1) + m12 * other.get(2, 1)
        m12 = m10 * other.get(0, 2) + m11 * other.get(1, 2) + m12 * other.get(2, 2)
        m20 = m20 * other.get(0, 0) + m21 * other.get(1, 0) + m22 * other.get(2, 0)
        m21 = m20 * other.get(0, 1) + m21 * other.get(1, 1) + m22 * other.get(2, 1)
        m22 = m20 * other.get(0, 2) + m21 * other.get(1, 2) + m22 * other.get(2, 2)
    }

    fun build(): Matrix3f = Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22)
}
