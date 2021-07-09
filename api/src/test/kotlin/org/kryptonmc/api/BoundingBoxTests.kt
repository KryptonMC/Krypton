/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.block.BoundingBox
import org.kryptonmc.api.space.Vector
import kotlin.test.Test
import kotlin.test.assertEquals

class BoundingBoxTests {

    @Test
    fun `test size of box calculations`() {
        val box = BoundingBox(Vector(-3, -3, -3), Vector(3, 3, 3))
        val expectedSize = Vector(6, 6, 6)
        assertEquals(box.size, expectedSize)
    }

    @Test
    fun `test volume calculation`() {
        val box = BoundingBox(Vector(-3, -3, -3), Vector(3, 3, 3))
        val expectedVolume = 6.0 * 6.0 * 6.0 // size.x * size.y * size.z
        assertEquals(box.volume, expectedVolume)
    }

    @Test
    fun `test center calculation`() {
        val box = BoundingBox(Vector(-3, -3, -3), Vector(3, 3, 3))
        val expectedCentre = Vector(-3.0 + 6.0 * 0.5, -3.0 + 6.0 * 0.5, -3.0 + 6.0 * 0.5)
        assertEquals(box.center, expectedCentre)
    }
}
