package org.kryptonmc.krypton

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.kyori.adventure.identity.Identity
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.event.KryptonEventBus
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFalse

class SenderTests {

    private val eventBusMock = mockk<KryptonEventBus> {
        every { call(any()) } returns Unit
    }

    private val server = mockk<KryptonServer> {
        every { eventBus } returns eventBusMock
    }

    @Test
    fun `has permission calls event`() {
        val sender = DummySender(server)
        assertFalse(sender.hasPermission("test.test"))
        verify { eventBusMock.call(any()) }
    }
}

private class DummySender(server: KryptonServer) : KryptonSender(server) {

    override val name = "dummy"
    override fun identity() = Identity.identity(UUID.randomUUID())
}
