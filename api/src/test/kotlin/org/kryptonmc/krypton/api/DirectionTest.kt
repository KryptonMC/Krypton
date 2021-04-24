package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.space.Direction
import kotlin.test.Test
import kotlin.test.assertEquals

class DirectionTest {

    @Test
    fun `test direction id retention`() {
        assertEquals(1, Direction.UP.id)
        assertEquals(0, Direction.DOWN.id)
    }
}
