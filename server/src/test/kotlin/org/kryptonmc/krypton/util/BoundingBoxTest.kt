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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoundingBoxTest {

    @Test
    fun `test size of box calculations`() {
        val box = KryptonBoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(6.0, box.size)
    }

    @Test
    fun `test volume calculation`() {
        val box = KryptonBoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        val expectedVolume = 6.0 * 6.0 * 6.0 // size.x * size.y * size.z
        assertEquals(expectedVolume, box.volume)
    }

    @Test
    fun `ensure center of box with negative and equal minimum values and equal maximum values is zero`() {
        val box = KryptonBoundingBox(-3.0, -3.0, -3.0, 3.0, 3.0, 3.0)
        assertEquals(0.0, box.centerX)
        assertEquals(0.0, box.centerY)
        assertEquals(0.0, box.centerZ)
    }
}
