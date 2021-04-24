package org.kryptonmc.krypton.api

import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.dummy.DummyWorld
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.space.square
import org.kryptonmc.krypton.api.world.Location
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationTests {

    private val world = DummyWorld("dummy1")
    private val other = DummyWorld("dummy2")

    @Test
    fun `test location data retention`() {
        val location = Location(world, 10.0, 9.0, 8.0, 7F, 6F)
        assertEquals(world, location.world)
        assertEquals(10.0, location.x)
        assertEquals(9.0, location.y)
        assertEquals(8.0, location.z)
        assertEquals(7F, location.yaw)
        assertEquals(6F, location.pitch)
    }

    @Test
    fun `test operations with locations of differing worlds`() {
        val one = Location(world, 0.0, 0.0, 0.0)
        val two = Location(other, 0.0, 0.0, 0.0)
        assertThrows<IllegalArgumentException> { one + two }
        assertThrows<IllegalArgumentException> { one - two }
        assertThrows<IllegalArgumentException> { one * two }
        assertThrows<IllegalArgumentException> { one / two }
        assertThrows<IllegalArgumentException> { one % two }
    }

    @Test
    fun `test values`() {
        val location = Location(world, 10.0, 9.0, 8.0, 7F, 6F)
        assertEquals(sqrt(10.0.square() + 9.0.square() + 8.0.square()), location.length)
        assertEquals(10.0.square() + 9.0.square() + 8.0.square(), location.lengthSquared)
        assertEquals(floor(10.0).toInt(), location.blockX)
        assertEquals(floor(9.0).toInt(), location.blockY)
        assertEquals(floor(8.0).toInt(), location.blockZ)
    }

    @Test
    fun `test normalization`() {
        assertFalse((Location(world, 1.0, 0.0, 0.0) * 1.1).isNormalized)
        assertTrue(Location(world, 1.0, 1.0, 1.0).normalize().isNormalized)
        assertTrue(Location(world, 1.0, 0.0, 0.0).isNormalized)
    }
}
