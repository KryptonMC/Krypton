package org.kryptonmc.krypton.api

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandTests {

    @Test
    fun `test command calls`() {
        assertEquals(command.name, "test")
        assertEquals(command.permission, "test.test")
        assertEquals(command.aliases, listOf("hello", "world"))
        assertEquals(command.suggest(sender, emptyList()), listOf("suggest", "me"))
    }
}
