package org.kryptonmc.krypton.util

import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Vec3dTests {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(Vec3dImpl::class.java).verify()
    }

    @Test
    fun `verify comparisons`() {
        val pos1a = Vec3dImpl(1.342, 2.938, 3.1543)
        val pos1b = Vec3dImpl(3.1543, 2.938, 1.342)
        assertEquals(0, pos1a.compareTo(pos1b))
        assertEquals(0, pos1b.compareTo(pos1a))
        assertEquals(0, pos1a.compareTo(pos1a))
        assertEquals(0, pos1b.compareTo(pos1b))
        val pos2 = Vec3dImpl(4.662, 5.921, 6.122)
        assertEquals(-1, pos1a.compareTo(pos2))
        assertEquals(-1, pos1b.compareTo(pos2))
        assertEquals(1, pos2.compareTo(pos1a))
        assertEquals(1, pos2.compareTo(pos1b))
    }

    @Test
    fun `test add and subtract with zero returns same`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        assertSame(pos, pos.add(0.0, 0.0, 0.0))
        assertSame(pos, pos.subtract(0.0, 0.0, 0.0))
    }

    @Test
    fun `test add and subtract`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(Vec3dImpl(pos.x + x, pos.y + y, pos.z + z), pos.add(x, y, z))
        assertEquals(Vec3dImpl(pos.x - x, pos.y - y, pos.z - z), pos.subtract(x, y, z))
    }

    @Test
    fun `test multiply and divide with one returns same`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        assertSame(pos, pos.multiply(1.0, 1.0, 1.0))
        assertSame(pos, pos.multiply(1.0))
        assertSame(pos, pos.divide(1.0, 1.0, 1.0))
        assertSame(pos, pos.divide(1.0))
    }

    @Test
    fun `test multiply and divide`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(Vec3dImpl(pos.x * x, pos.y * y, pos.z * z), pos.multiply(x, y, z))
        assertEquals(Vec3dImpl(pos.x * 5.342, pos.y * 5.342, pos.z * 5.342), pos.multiply(5.342))
        assertEquals(Vec3dImpl(pos.x / x, pos.y / y, pos.z / z), pos.divide(x, y, z))
        assertEquals(Vec3dImpl(pos.x / 5.342, pos.y / 5.342, pos.z / 5.342), pos.divide(5.342))
    }

    @Test
    fun `test dot product`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(pos.x * x + pos.y * y + pos.z * z, pos.dot(x, y, z))
    }

    @Test
    fun `test cross product`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val x = 4.662
        val y = 5.921
        val z = 6.122
        assertEquals(Vec3dImpl(pos.y * z - pos.z * y, pos.z * x - pos.x * z, pos.x * y - pos.y * x), pos.cross(x, y, z))
    }

    @Test
    fun `test negation`() {
        val x = 1.342
        val y = 2.938
        val z = 3.1543
        assertEquals(Vec3dImpl(-x, -y, -z), Vec3dImpl(x, y, z).negate())
    }

    @Test
    fun `test distance and distance squared`() {
        val pos1 = Vec3dImpl(1.342, 2.938, 3.1543)
        val pos2 = Vec3dImpl(4.662, 5.921, 6.122)
        val dx = pos1.x - pos2.x
        val dy = pos1.y - pos2.y
        val dz = pos1.z - pos2.z
        val sqDistance = dx * dx + dy * dy + dz * dz
        assertEquals(sqDistance, pos1.distanceSquared(pos2.x, pos2.y, pos2.z))
        assertEquals(sqrt(sqDistance), pos1.distance(pos2.x, pos2.y, pos2.z))
    }

    @Test
    fun `test length and length squared`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val sqLength = pos.x * pos.x + pos.y * pos.y + pos.z * pos.z
        assertEquals(sqLength, pos.lengthSquared())
        assertEquals(sqrt(sqLength), pos.length())
    }

    @Test
    fun `test normalization`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
        val length = pos.length()
        assertEquals(Vec3dImpl(pos.x / length, pos.y / length, pos.z / length), pos.normalize())
    }

    @Test
    fun `test floor values`() {
        val pos = Vec3dImpl(1.342, 2.938, 3.1543)
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
