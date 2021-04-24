package org.kryptonmc.krypton.api

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.dummy.DummyCancellableEvent
import org.kryptonmc.krypton.api.event.events.login.JoinEvent
import org.kryptonmc.krypton.api.event.events.login.LoginEvent
import org.kryptonmc.krypton.api.event.events.play.ChatEvent
import org.kryptonmc.krypton.api.event.events.play.QuitEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventTests {

    @Test
    fun `test cancellable event set cancelled true`() {
        val event = DummyCancellableEvent()
        event.isCancelled = true
        assertTrue(event.isCancelled)
    }

    @Test
    fun `test cancellable event set cancelled false`() {
        val event = DummyCancellableEvent()
        event.isCancelled = false
        assertFalse(event.isCancelled)
    }

    @Test
    fun `test cancellable event cancel`() {
        val event = DummyCancellableEvent()
        event.cancel()
        assertTrue(event.isCancelled)
    }

    @Test
    fun `test join event cancels properly`() {
        val event = JoinEvent(player)
        val cancelledReason = Component.text("Cancelled")
        event.cancel(cancelledReason)

        assertTrue(event.isCancelled)
        assertEquals(cancelledReason, event.cancelledReason)
    }

    @Test
    fun `test login event cancels properly`() {
        val event = LoginEvent(player.name, player.uuid, player.address)
        val cancelledReason = Component.text("Cancelled")
        event.cancel(cancelledReason)

        assertTrue(event.isCancelled)
        assertEquals(cancelledReason, event.cancelledReason)
    }

    @Test
    fun `test login event data is retained`() {
        val event = LoginEvent(player.name, player.uuid, player.address)

        assertEquals(player.name, event.username)
        assertEquals(player.uuid, event.uuid)
        assertEquals(player.address, event.address)
    }

    @Test
    fun `test join event message updates`() {
        val event = JoinEvent(player)
        val message = Component.text("Welcome!")
        event.message = message

        assertEquals(message, event.message)
        assertEquals(player, event.player)
    }

    @Test
    fun `test quit event message updates`() {
        val event = QuitEvent(player)
        val message = Component.text("Goodbye!")
        event.message = message

        assertEquals(message, event.message)
        assertEquals(player, event.player)
    }

    @Test
    fun `test chat event data is retained`() {
        val message = "hello world"
        val event = ChatEvent(player, message)

        assertEquals(message, event.message)
        assertEquals(player, event.player)
    }
}
