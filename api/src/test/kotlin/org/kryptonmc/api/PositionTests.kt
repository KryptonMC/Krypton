/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.space.square
import kotlin.math.acos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PositionTests {

    private val position = Vector.random()
    private val other = Vector.random()

    @Test
    fun `test lengths`() {
        val lengthSquared = position.x.square() + position.y.square() + position.z.square()
        assertEquals(lengthSquared, position.lengthSquared)
        assertEquals(sqrt(lengthSquared), position.length)
    }

    @Test
    fun `test addition and subtraction`() {
        assertEquals(Vector(position.x + other.x, position.y + other.y, position.z + other.z), position + other)
        assertEquals(Vector(position.x - other.x, position.y - other.y, position.z - other.z), position - other)
    }

    @Test
    fun `test multiplication and division`() {
        val multiplied = Vector(position.x * 2, position.y * 2, position.z * 2)
        assertEquals(Vector(position.x * other.x, position.y * other.y, position.z * other.z), position * other)
        assertEquals(multiplied, position * 2)
        assertEquals(multiplied, position * 2.0)
        assertEquals(multiplied, position * 2F)

        val divided = Vector(position.x / 2, position.y / 2, position.z / 2)
        assertEquals(Vector(position.x / other.x, position.y / other.y, position.z / other.z), position / other)
        assertEquals(divided, position / 2)
        assertEquals(divided, position / 2.0)
        assertEquals(divided, position / 2F)

        val modulo = Vector(position.x % 2, position.y % 2, position.z % 2)
        assertEquals(Vector(position.x % other.x, position.y % other.y, position.z % other.z), position % other)
        assertEquals(modulo, position % 2)
        assertEquals(modulo, position % 2.0)
        assertEquals(modulo, position % 2F)
    }

    @Test
    fun `test negation, incrementing and decrementing`() {
        assertEquals(Vector(-position.x, -position.y, -position.z), -position)
        assertEquals(Vector(position.x + 1, position.y + 1, position.z + 1), position.inc())
        assertEquals(Vector(position.x - 1, position.y - 1, position.z - 1), position.dec())
    }

    @Test
    fun `test distance and distance squared`() {
        val distanceSquared = (position.x - other.x).square() + (position.y - other.y).square() + (position.z - other.z).square()
        assertEquals(sqrt(distanceSquared), position.distance(other))
        assertEquals(distanceSquared, position.distanceSquared(other))
    }

    @Test
    fun `test angle, midpoint, dot and cross product`() {
        val dot = position.x * other.x + position.y * other.y + position.z * other.z
        val cross = Vector(position.y * other.z - other.y * position.z, position.z * other.x - other.z * position.x, position.x * other.y - other.x * position.y)
        val angle = acos(min(max(dot / (position.length * other.length), -1.0), 1.0))
        val midpoint = Vector((position.x + other.x) / 2, (position.y + other.y) / 2, (position.z + other.z) / 2)

        assertEquals(angle, position.angle(other))
        assertEquals(midpoint, position.midpoint(other))
        assertEquals(dot, position.dot(other))
        assertEquals(cross, position.cross(other))
    }

    @Test
    fun `test normalization`() {
        assertEquals(Vector(position.x / position.length, position.y / position.length, position.z / position.length), position.normalize())

        assertFalse((Vector(1.0, 0.0, 0.0) * 1.1).isNormalized)
        assertTrue(Vector(1.0, 1.0, 1.0).normalize().isNormalized)
        assertTrue(Vector(1.0, 0.0, 0.0).isNormalized)
    }

    @Test
    fun `test floor division`() {
        assertEquals(floor(position.x).toInt(), position.blockX)
        assertEquals(floor(position.y).toInt(), position.blockY)
        assertEquals(floor(position.z).toInt(), position.blockZ)

        val known = Vector(128.5, 128.5, 128.5)
        assertEquals(128, known.blockX)
        assertEquals(128, known.blockY)
        assertEquals(128, known.blockZ)
    }

    @Test
    fun `check equality`() {
        val x = Random.nextDouble()
        val y = Random.nextDouble()
        val z = Random.nextDouble()
        val one = Vector(x, y, z)
        val two = Vector(x, y, z)

        assertEquals(one, two)
        assertEquals(one.hashCode(), two.hashCode())
    }
}
