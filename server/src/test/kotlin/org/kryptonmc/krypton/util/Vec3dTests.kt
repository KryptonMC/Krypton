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

import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.coordinate.KryptonVec3d
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.krypton.util.math.Maths
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Vec3dTests {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(KryptonVec3d::class.java).verify()
    }

    @Test
    fun `verify comparisons`() {
        val pos1a = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos1b = KryptonVec3d(3.1543, 2.938, 1.342)
        assertEquals(0, pos1a.compareTo(pos1b))
        assertEquals(0, pos1b.compareTo(pos1a))
        assertEquals(0, pos1a.compareTo(pos1a))
        assertEquals(0, pos1b.compareTo(pos1b))
        val pos2 = KryptonVec3d(4.662, 5.921, 6.122)
        assertEquals(-1, pos1a.compareTo(pos2))
        assertEquals(-1, pos1b.compareTo(pos2))
        assertEquals(1, pos2.compareTo(pos1a))
        assertEquals(1, pos2.compareTo(pos1b))
    }

    @Test
    fun `test add and subtract with zero returns same`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertSame(pos, pos.add(0.0, 0.0, 0.0))
        assertSame(pos, pos.subtract(0.0, 0.0, 0.0))
    }

    @Test
    fun `test add and subtract`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(KryptonVec3d(pos.x + x, pos.y + y, pos.z + z), pos.add(x, y, z))
        assertEquals(KryptonVec3d(pos.x - x, pos.y - y, pos.z - z), pos.subtract(x, y, z))
    }

    @Test
    fun `test multiply and divide with one returns same`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertSame(pos, pos.multiply(1.0, 1.0, 1.0))
        assertSame(pos, pos.multiply(1.0))
        assertSame(pos, pos.divide(1.0, 1.0, 1.0))
        assertSame(pos, pos.divide(1.0))
    }

    @Test
    fun `test multiply and divide`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(KryptonVec3d(pos.x * x, pos.y * y, pos.z * z), pos.multiply(x, y, z))
        assertEquals(KryptonVec3d(pos.x * 5.342, pos.y * 5.342, pos.z * 5.342), pos.multiply(5.342))
        assertEquals(KryptonVec3d(pos.x / x, pos.y / y, pos.z / z), pos.divide(x, y, z))
        assertEquals(KryptonVec3d(pos.x / 5.342, pos.y / 5.342, pos.z / 5.342), pos.divide(5.342))
    }

    @Test
    fun `test dot product`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(pos.x * x + pos.y * y + pos.z * z, pos.dot(x, y, z))
    }

    @Test
    fun `test cross product`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(KryptonVec3d(pos.y * z - pos.z * y, pos.z * x - pos.x * z, pos.x * y - pos.y * x), pos.cross(x, y, z))
    }

    @Test
    fun `test negation`() {
        val x = 1.342
        val y = 2.938
        val z = 3.1543
        assertEquals(KryptonVec3d(-x, -y, -z), KryptonVec3d(x, y, z).negate())
    }

    @Test
    fun `test distance and distance squared`() {
        val pos1 = KryptonVec3d(1.342, 2.938, 3.1543)
        val pos2 = KryptonVec3d(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqDistance, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
        assertEquals(sqrt(sqDistance), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `test length and length squared`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val sqLength = pos.x * pos.x + pos.y * pos.y + pos.z * pos.z
        assertEquals(sqLength, pos.lengthSquared())
        assertEquals(sqrt(sqLength), pos.length())
    }

    @Test
    fun `test normalization`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        val length = pos.length()
        assertEquals(KryptonVec3d(pos.x / length, pos.y / length, pos.z / length), pos.normalize())
    }

    @Test
    fun `test floor values`() {
        val pos = KryptonVec3d(1.342, 2.938, 3.1543)
        assertEquals(Maths.floor(1.342), pos.floorX())
        assertEquals(Maths.floor(2.938), pos.floorY())
        assertEquals(Maths.floor(3.1543), pos.floorZ())
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories`() {
            Bootstrapping.loadFactories()
        }
    }
}
