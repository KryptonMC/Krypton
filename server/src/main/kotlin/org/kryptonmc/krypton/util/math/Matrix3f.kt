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

class Matrix3f(
    private val m00: Float, private val m01: Float, private val m02: Float,
    private val m10: Float, private val m11: Float, private val m12: Float,
    private val m20: Float, private val m21: Float, private val m22: Float
) {

    class Builder {

        private var m00 = 0F
        private var m01 = 0F
        private var m02 = 0F
        private var m10 = 0F
        private var m11 = 0F
        private var m12 = 0F
        private var m20 = 0F
        private var m21 = 0F
        private var m22 = 0F

        fun set(x: Int, y: Int, value: Float): Builder = apply {
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

        fun m00(value: Float): Builder = apply { m00 = value }

        fun m11(value: Float): Builder = apply { m11 = value }

        fun m22(value: Float): Builder = apply { m22 = value }

        fun multiply(other: Matrix3f): Builder = apply {
            m00 = m00 * other.m00 + m01 * other.m01 + m02 * other.m20
            m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21
            m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22
            m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20
            m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21
            m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22
            m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20
            m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21
            m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22
        }

        fun build(): Matrix3f = Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22)
    }

    companion object {

        @JvmStatic
        fun builder(): Builder = Builder()
    }
}
