package org.kryptonmc.krypton.api

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandTest {

    @Test
    fun `test command calls`() {
        assertEquals("test", command.name)
        assertEquals("test.test", command.permission)
        assertEquals(listOf("hello", "world"), command.aliases)
        assertEquals(emptyList(), command.suggest(sender, emptyList()))
    }
}
