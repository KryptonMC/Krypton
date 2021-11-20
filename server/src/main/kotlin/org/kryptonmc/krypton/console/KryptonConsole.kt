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
package org.kryptonmc.krypton.console

import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import net.minecrell.terminalconsole.SimpleTerminalConsole
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

class KryptonConsole(override val server: KryptonServer) : SimpleTerminalConsole(), ConsoleSender {

    private var permissionFunction = PermissionFunction.ALWAYS_TRUE
    override val name = Component.text("CONSOLE")
    override val permissionLevel = 4

    fun setupPermissions() {
        val event = SetupPermissionsEvent(this) { PermissionFunction.ALWAYS_TRUE }
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
            .completer(BrigadierCompleter(this, server.commandManager.dispatcher))
            .highlighter(BrigadierHighlighter(this, server.commandManager.dispatcher))
            .option(LineReader.Option.COMPLETE_IN_WORD, true)
    )

    companion object {

        val LOGGER = logger("CONSOLE")
    }
}
