/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.text.Component
import org.kryptonmc.api.event.login.LoginEvent
import org.kryptonmc.api.event.play.ChatEvent
import org.kryptonmc.api.event.play.QuitEvent
import kotlin.test.Test
import kotlin.test.assertEquals

// TODO: Possibly add some tests with resulted events
class EventTests {

    @Test
    fun `test quit event message updates`() {
        val event = QuitEvent(player)
        val message = Component.text("Goodbye!")
        event.message = message

        assertEquals(message, event.message)
        assertEquals(player, event.player)
    }
}
