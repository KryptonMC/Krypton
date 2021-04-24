package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.block.BoundingBox
import org.kryptonmc.krypton.api.space.Vector
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
        //
        val expectedCentre = Vector((-3.0 + 6.0 * 0.5), (-3.0 + 6.0 * 0.5), (-3.0 + 6.0 * 0.5))
        assertEquals(box.center, expectedCentre)
    }
}
