/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.space.square
import org.kryptonmc.api.world.Location
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationTests {

    @Test
    fun `test location data retention`() {
        val location = Location(10.0, 9.0, 8.0, 7F, 6F)
        assertEquals(10.0, location.x)
        assertEquals(9.0, location.y)
        assertEquals(8.0, location.z)
        assertEquals(7F, location.yaw)
        assertEquals(6F, location.pitch)
    }

    @Test
    fun `test values`() {
        val location = Location(10.0, 9.0, 8.0, 7F, 6F)
        assertEquals(sqrt(10.0.square() + 9.0.square() + 8.0.square()), location.length)
        assertEquals(10.0.square() + 9.0.square() + 8.0.square(), location.lengthSquared)
        assertEquals(floor(10.0).toInt(), location.blockX)
        assertEquals(floor(9.0).toInt(), location.blockY)
        assertEquals(floor(8.0).toInt(), location.blockZ)
    }

    @Test
    fun `test normalization`() {
        assertFalse((Location(1.0, 0.0, 0.0) * 1.1).isNormalized)
        assertTrue(Location(1.0, 1.0, 1.0).normalize().isNormalized)
        assertTrue(Location(1.0, 0.0, 0.0).isNormalized)
    }
}
