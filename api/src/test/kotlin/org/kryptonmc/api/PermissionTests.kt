/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.player.PermissionCheckEvent
import org.kryptonmc.api.event.server.TickEndEvent
import org.kryptonmc.api.event.server.TickStartEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class PermissionTests {

    @Test
    fun `test permission check event permission is retained`() {
        val event = PermissionCheckEvent(sender, "test.test", true)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
    }

    @Test
    fun `test null permission check evaluates to true`() {
        val event = PermissionCheckEvent(sender, null, false)
        assertEquals(sender, event.sender)
        assertEquals(null, event.permission)
        assertEquals(TriState.TRUE, event.result)
    }

    @Test
    fun `test unset not has permission evaluates to unset`() {
        val event = PermissionCheckEvent(sender, "test.test", false)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(TriState.NOT_SET, event.result)
    }

    @Test
    fun `test set not has permission evaluates to false`() {
        val sender = mockk<Sender> {
            every { permissions } returns mapOf("test.test" to false)
        }
        val event = PermissionCheckEvent(sender, "test.test", false)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(TriState.FALSE, event.result)
    }

    @Test
    fun `test set has permission evaluates to true`() {
        val sender = mockk<Sender> {
            every { permissions } returns mapOf("test.test" to false)
        }
        val event = PermissionCheckEvent(sender, "test.test", true)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(TriState.TRUE, event.result)
    }

    @Test
    fun `test tick start and end event data is retained`() {
        val start = TickStartEvent(10)
        val end = TickEndEvent(10, 20L, 30L)
        assertEquals(10, start.tickNumber)
        assertEquals(10, end.tickNumber)
        assertEquals(20L, end.tickDuration)
        assertEquals(30L, end.timeEnd)
    }
}
