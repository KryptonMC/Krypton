/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
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
