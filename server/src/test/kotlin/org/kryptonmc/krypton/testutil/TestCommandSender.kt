/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.testutil

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.Server
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonSender
import java.util.ArrayDeque
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestCommandSender(
    override val name: String,
    override val server: Server,
    private val permissionFunction: PermissionFunction,
    private val acceptSuccess: Boolean,
    private val acceptFailure: Boolean,
    private val informAdmins: Boolean
) : KryptonSender {

    private val sentMessages = ArrayDeque<Component>()

    override fun acceptsSuccess(): Boolean = acceptSuccess

    override fun acceptsFailure(): Boolean = acceptFailure

    override fun shouldInformAdmins(): Boolean = informAdmins

    override fun sendSystemMessage(message: Component) {
        sentMessages.add(message)
    }

    fun pollMessage(): Component = assertNotNull(sentMessages.poll(), "No messages were sent!")

    fun assertNoMessages() {
        assertTrue(sentMessages.isEmpty(), "Expected no messages to be sent, but ${sentMessages.size} were!")
    }

    override fun createCommandSourceStack(): CommandSourceStack {
        throw UnsupportedOperationException("Cannot create a command source stack for a test command sender!")
    }

    override fun getPermissionValue(permission: String): TriState = permissionFunction.getPermissionValue(permission)
}
