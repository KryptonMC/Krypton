/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.kyori.adventure.identity.Identity
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.event.play.PermissionCheckEvent
import org.kryptonmc.krypton.command.KryptonSender
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.test.Test
import kotlin.test.assertFalse

class SenderTests {

    private val eventBusMock = mockk<EventManager> {
        every { fire(any<Any>()) } answers { CompletableFuture.completedFuture(arg(0)) }
    }

    @Test
    fun `has permission calls event`() {
        val sender = DummySender(eventBusMock)
        assertFalse(sender.hasPermission("test.test"))
        verify { eventBusMock.fire(any<PermissionCheckEvent>()) }
    }
}

private class DummySender(private val eventManager: EventManager) : KryptonSender(mockk()) {

    override val name = "dummy"

    override fun hasPermission(permission: String): Boolean {
        val event = PermissionCheckEvent(this, permission, permission in permissions)
        return eventManager.fire(event).join().result.value
    }

    override fun identity() = Identity.identity(UUID.randomUUID())
}
