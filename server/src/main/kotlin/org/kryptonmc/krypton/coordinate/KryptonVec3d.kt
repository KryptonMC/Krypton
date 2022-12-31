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
package org.kryptonmc.krypton.coordinate

import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.util.math.Maths
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

class KryptonVec3d(override val x: Double, override val y: Double, override val z: Double) : Vec3d {

    override fun add(x: Double, y: Double, z: Double): Vec3d {
        if (x == 0.0 && y == 0.0 && z == 0.0) return this
        return KryptonVec3d(this.x + x, this.y + y, this.z + z)
    }

    override fun subtract(x: Double, y: Double, z: Double): Vec3d {
        if (x == 0.0 && y == 0.0 && z == 0.0) return this
        return KryptonVec3d(this.x - x, this.y - y, this.z - z)
    }

    override fun multiply(x: Double, y: Double, z: Double): Vec3d {
        if (x == 1.0 && y == 1.0 && z == 1.0) return this
        return KryptonVec3d(this.x * x, this.y * y, this.z * z)
    }

    override fun multiply(factor: Double): Vec3d {
        if (factor == 1.0) return this
        return KryptonVec3d(x * factor, y * factor, z * factor)
    }

    override fun divide(x: Double, y: Double, z: Double): Vec3d {
        if (x == 1.0 && y == 1.0 && z == 1.0) return this
        return KryptonVec3d(this.x / x, this.y / y, this.z / z)
    }

    override fun divide(factor: Double): Vec3d {
        if (factor == 1.0) return this
        return KryptonVec3d(x / factor, y / factor, z / factor)
    }

    override fun dot(x: Double, y: Double, z: Double): Double = this.x * x + this.y * y + this.z * z

    override fun cross(x: Double, y: Double, z: Double): Vec3d =
        KryptonVec3d(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x)

    override fun pow(power: Double): Vec3d = KryptonVec3d(x.pow(power), y.pow(power), z.pow(power))

    override fun abs(): Vec3d = KryptonVec3d(abs(x), abs(y), abs(z))

    override fun negate(): Vec3d = KryptonVec3d(-x, -y, -z)

    override fun distanceSquared(x: Double, y: Double, z: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return dx * dx + dy * dy + dz * dz
    }

    override fun distance(x: Double, y: Double, z: Double): Double = sqrt(distanceSquared(x, y, z))

    override fun floorX(): Int = Maths.floor(x)

    override fun floorY(): Int = Maths.floor(y)

    override fun floorZ(): Int = Maths.floor(z)

    override fun lengthSquared(): Double = x * x + y * y + z * z

    override fun length(): Double = sqrt(lengthSquared())

    override fun normalize(): Vec3d {
        val length = length()
        if (length < EPSILON) return ZERO
        return KryptonVec3d(x / length, y / length, z / length)
    }

    override fun equals(other: Any?): Boolean =
        this === other || other is Vec3d && x.compareTo(other.x) == 0 && y.compareTo(other.y) == 0 && z.compareTo(other.z) == 0

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String = "($x, $y, $z)"

    override fun compareTo(other: Vec3d): Int = sign(lengthSquared() - other.lengthSquared()).toInt()

    object Factory : Vec3d.Factory {

        override fun of(x: Double, y: Double, z: Double): Vec3d {
            if (x == 0.0 && y == 0.0 && z == 0.0) return ZERO
            return KryptonVec3d(x, y, z)
        }
    }

    companion object {

        private const val EPSILON = 1.0E-4
        @JvmField
        val ZERO: Vec3d = KryptonVec3d(0.0, 0.0, 0.0)
    }
}
