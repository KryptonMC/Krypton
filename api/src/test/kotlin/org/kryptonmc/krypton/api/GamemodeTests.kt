/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
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
