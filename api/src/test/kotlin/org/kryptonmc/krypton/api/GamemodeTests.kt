package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.world.Gamemode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GamemodeTests {

    @Test
    fun `test id conversions`() {
        assertEquals("survival", Gamemode.SURVIVAL.toString())
        assertNotNull(Gamemode.fromId(0))
        assertNotNull(Gamemode.fromId(3))
        assertNull(Gamemode.fromId(4))
        assertNull(Gamemode.fromId(-1))
        assertNull(Gamemode.fromId(100))
    }
}
