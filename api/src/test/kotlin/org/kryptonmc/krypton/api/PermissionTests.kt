package org.kryptonmc.krypton.api

import io.mockk.every
import io.mockk.mockk
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckEvent
import org.kryptonmc.krypton.api.event.events.play.PermissionCheckResult
import org.kryptonmc.krypton.api.event.events.ticking.TickEndEvent
import org.kryptonmc.krypton.api.event.events.ticking.TickStartEvent
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
        assertEquals(PermissionCheckResult.TRUE, event.result)
    }

    @Test
    fun `test unset not has permission evaluates to unset`() {
        val event = PermissionCheckEvent(sender, "test.test", false)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(PermissionCheckResult.UNSET, event.result)
    }

    @Test
    fun `test set not has permission evaluates to false`() {
        val sender = mockk<Sender> {
            every { permissions } returns mapOf("test.test" to false)
        }
        val event = PermissionCheckEvent(sender, "test.test", false)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(PermissionCheckResult.FALSE, event.result)
    }

    @Test
    fun `test set has permission evaluates to true`() {
        val sender = mockk<Sender> {
            every { permissions } returns mapOf("test.test" to false)
        }
        val event = PermissionCheckEvent(sender, "test.test", true)
        assertEquals(sender, event.sender)
        assertEquals("test.test", event.permission)
        assertEquals(PermissionCheckResult.TRUE, event.result)
    }

    @Test
    fun `test permission check result boolean values`() {
        assertEquals(true, PermissionCheckResult.TRUE.value)
        assertEquals(false, PermissionCheckResult.FALSE.value)
        assertEquals(false, PermissionCheckResult.UNSET.value)
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
