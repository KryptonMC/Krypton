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
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.apache.logging.log4j.Logger
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.event.command.CommandExecuteEvent
import org.kryptonmc.api.event.server.SetupPermissionsEvent
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.TranslationBootstrap
import org.kryptonmc.krypton.util.logger
import java.util.Locale
import java.util.UUID

class KryptonConsole(override val server: KryptonServer) : SimpleTerminalConsole(), ConsoleSender {

    // The permission function defaults to ALWAYS_TRUE because we are god and have all permissions by default
    private var permissionFunction = DEFAULT_PERMISSION_FUNCTION

    override val name: Component = Component.text("CONSOLE")
    override val uuid: UUID = Identity.nil().uuid()

    fun setupPermissions() {
        val event = SetupPermissionsEvent(this) { DEFAULT_PERMISSION_FUNCTION }
        permissionFunction = server.eventManager.fireSync(event).createFunction(this)
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        LOGGER.info(TranslationBootstrap.RENDERER.render(message, Locale.ENGLISH).toLegacySectionText())
    }

    override fun getPermissionValue(permission: String): TriState = permissionFunction[permission]

    override fun identity(): Identity = Identity.nil()

    override fun isRunning(): Boolean = server.isRunning

    override fun runCommand(command: String) {
        val result = server.eventManager.fireSync(CommandExecuteEvent(this, command)).result
        if (!result.isAllowed) return
        server.commandManager.dispatch(this, result.command ?: command)
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

    companion object {

        private val DEFAULT_PERMISSION_FUNCTION = PermissionFunction.ALWAYS_TRUE

        @JvmField
        val LOGGER: Logger = logger("CONSOLE")
    }
}
