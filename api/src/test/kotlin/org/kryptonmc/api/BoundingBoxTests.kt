/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.space.BoundingBox
import org.kryptonmc.api.space.Vector
import kotlin.test.Test
import kotlin.test.assertEquals

class BoundingBoxTests {

    @Test
    fun `test size of box calculations`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(6.0, box.size)
    }

    @Test
    fun `test volume calculation`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        val expectedVolume = 6.0 * 6.0 * 6.0 // size.x * size.y * size.z
        assertEquals(expectedVolume, box.volume)
    }

    @Test
    fun `test center calculation`() {
        val box = BoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(Vector.ZERO, box.center)
    }
}
