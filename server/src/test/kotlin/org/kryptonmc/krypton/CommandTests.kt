/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.command.Command
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.command.KryptonCommandManager
import org.kryptonmc.krypton.command.commands.RestartCommand
import org.kryptonmc.krypton.command.commands.StopCommand
import org.kryptonmc.krypton.event.KryptonEventBus
import java.security.Permission
import kotlin.system.exitProcess
import kotlin.test.Test

class CommandTests {

    @Test
    fun `stop command actually stops the server`() {
        val stop = StopCommand(server)
        val sender = mockk<Sender> {
            every { sendMessage(any()) } just runs
        }

        stop.execute(sender, emptyList())
        verify { sender.sendMessage(any()) }
        try {
            verify { server.stop() }
        } catch (ignored: SecurityException) {}
    }

    @Test
    fun `restart command restarts`() {
        val restart = RestartCommand(server)
        val sender = mockk<Sender> {
            every { sendMessage(any()) } just runs
        }

        restart.execute(sender, emptyList())
        verify { sender.sendMessage(any()) }
        try {
            verify { server.restart() }
        } catch (ignored: SecurityException) {}
    }

    @Test
    fun `dispatch actually dispatches`() {
        manager.dispatch(emptySender, "test-def-exec")
        verify { emptySender.sendMessage(text("You do not have permission to execute this command!", NamedTextColor.RED)) }
    }

    @Test
    fun `dispatch sends correct message for unknown command`() {
        manager.dispatch(emptySender, "invalid-command")
        verify { emptySender.sendMessage(text("Unknown command at position 0: <--[HERE]")) }
    }

    @Test
    fun `dispatch sends exception as message for unhandled exception type`() {
        manager.dispatch(emptySender, "test-strict 30")
        verify { emptySender.sendMessage(text("Long must not be more than 20, found 30 at position 12: ...st-strict <--[HERE]")) }
    }

    companion object {

        private val eventBusMock = mockk<KryptonEventBus> {
            every { call(any()) } just runs
        }

        private val server = mockk<KryptonServer> {
            every { eventBus } returns eventBusMock
            every { stop(any()) } returns Unit
            every { restart() } returns Unit
        }

        private val manager = KryptonCommandManager(server)

        private val emptySender = mockk<Sender> {
            every { sendMessage(any()) } just runs
            every { hasPermission(any()) } returns false
            every { permissions } returns emptyMap()
        }

        @BeforeAll
        @JvmStatic
        fun `register shady security system and commands`() {
            System.setSecurityManager(ShadySecuritySystem())
            manager.register(DefaultedExecuteCommand("test-null-perm"))
            manager.register(DefaultedExecuteCommand("test-def-exec", "test.def.exec"))
            manager.register(LiteralArgumentBuilder.literal<Sender>("test-strict")
                .then(RequiredArgumentBuilder.argument("bounds", LongArgumentType.longArg(10, 20)))
                .build()
            )
        }
    }
}

private class ShadySecuritySystem : SecurityManager() {

    override fun checkPermission(perm: Permission?) = Unit

    override fun checkPermission(perm: Permission?, context: Any?) = Unit

    override fun checkExit(status: Int) {
        super.checkExit(status)
        throw SecurityException("The shady security system has denied your request to shut down the JVM.")
    }
}

private open class DefaultedExecuteCommand(
    name: String,
    permission: String? = null,
    aliases: List<String> = emptyList()
) : Command(name, permission, aliases) {

    override fun execute(sender: Sender, args: List<String>) = Unit
}
