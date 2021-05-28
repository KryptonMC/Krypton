/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandTest {

    @Test
    fun `test command calls`() {
        assertEquals("test", command.name)
        assertEquals("test.test", command.permission)
        assertEquals(listOf("hello", "world"), command.aliases)
        assertEquals(emptyList(), command.suggest(sender, emptyArray()))
    }
}
