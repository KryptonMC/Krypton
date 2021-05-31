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

/**
 * Represents a protocol angle, measured in 256ths of a full turn
 */
@JvmInline
value class Angle(val value: UByte) : Comparable<Angle> {

    override fun compareTo(other: Angle) = if (value == other.value) 0 else if (value < other.value) -1 else 1

    fun toDegrees() = value.toFloat() / 256.0f * 360.0f

    companion object {

        val ZERO = Angle(0u)
    }
}

/**
 * Convert a float value (in degrees) to a protocol [Angle] by dividing its value by 360 and multiplying
 * the result by 256, then rounding to an integer
 */
fun Float.toAngle() = Angle((div(360.0f) * 256.0f).toInt().toUByte())
