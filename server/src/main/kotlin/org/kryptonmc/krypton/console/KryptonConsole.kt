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
package org.kryptonmc.krypton.console

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.apache.logging.log4j.LogManager
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.event.command.CommandExecuteEvent
import org.kryptonmc.api.event.server.SetupPermissionsEvent
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.TranslationBootstrap
import java.util.Locale
import java.util.UUID

class KryptonConsole(override val server: KryptonServer) : SimpleTerminalConsole(), ConsoleSender {

    // The permission function defaults to ALWAYS_TRUE because we are god and have all permissions by default
    private var permissionFunction = DEFAULT_PERMISSION_FUNCTION
    private var cachedPointers: Pointers? = null

    override val name: Component = DISPLAY_NAME
    override val uuid: UUID = ID

    fun setupPermissions() {
        val event = SetupPermissionsEvent(this) { DEFAULT_PERMISSION_FUNCTION }
        permissionFunction = server.eventManager.fire(event).get().createFunction(this)
    }

    fun run() {
        val thread = Thread(::start, "Console Handler").apply {
            uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, exception ->
                LOGGER.error("Caught previously unhandled exception ", exception)
            }
            isDaemon = true
        }
        thread.start()
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        LOGGER.info(TranslationBootstrap.render(message).toLegacySectionText())
    }

    override fun getPermissionValue(permission: String): TriState = permissionFunction[permission]

    override fun identity(): Identity = Identity.nil()

    override fun isRunning(): Boolean = server.isRunning

    override fun runCommand(command: String) {
        server.eventManager.fire(CommandExecuteEvent(this, command)).thenAcceptAsync {
            if (!it.result.isAllowed) return@thenAcceptAsync
            server.commandManager.dispatch(this, it.result.command ?: command)
        }
    }

    override fun shutdown() {
        server.stop()
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader = super.buildReader(
        builder.appName("Krypton")
            .completer(BrigadierCompleter(this))
            .highlighter(BrigadierHighlighter(this))
            .option(LineReader.Option.COMPLETE_IN_WORD, true)
    )

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withStatic(Identity.NAME, NAME)
                .withStatic(Identity.DISPLAY_NAME, DISPLAY_NAME)
                .withStatic(Identity.UUID, ID)
                .withStatic(PermissionChecker.POINTER, PermissionChecker(::getPermissionValue))
                .build()
        }
        return cachedPointers!!
    }

    companion object {

        private const val NAME = "CONSOLE"
        private val DISPLAY_NAME = Component.text(NAME)
        private val ID = UUID(0, 0)

        private val DEFAULT_PERMISSION_FUNCTION = PermissionFunction.ALWAYS_TRUE
        private val LOGGER = LogManager.getLogger("CONSOLE")
    }
}
