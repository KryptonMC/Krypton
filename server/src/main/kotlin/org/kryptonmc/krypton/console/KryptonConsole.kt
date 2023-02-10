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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.util.TriState
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.apache.logging.log4j.LogManager
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.event.command.KryptonCommandExecuteEvent
import org.kryptonmc.krypton.event.server.KryptonSetupPermissionsEvent
import org.kryptonmc.krypton.locale.MinecraftTranslationManager
import org.kryptonmc.krypton.network.chat.RichChatType

class KryptonConsole(override val server: KryptonServer) : SimpleTerminalConsole(), KryptonSender, ConsoleSender {

    // The permission function defaults to ALWAYS_TRUE because we are god and have all permissions by default
    private var permissionFunction = DEFAULT_PERMISSION_FUNCTION
    private var cachedPointers: Pointers? = null

    override val name: Component
        get() = DISPLAY_NAME

    fun setupPermissions() {
        val event = server.eventNode.fire(KryptonSetupPermissionsEvent(this, DEFAULT_PERMISSION_FUNCTION))
        permissionFunction = event.result?.function ?: event.defaultFunction
    }

    fun run() {
        val thread = Thread({ start() }, "Console Handler").apply {
            setUncaughtExceptionHandler { _, exception -> LOGGER.error("Caught previously unhandled exception!", exception) }
            isDaemon = true
        }
        thread.start()
    }

    override fun sendSystemMessage(message: Component) {
        LOGGER.info(LegacyComponentSerializer.legacySection().serialize(MinecraftTranslationManager.render(message)))
    }

    fun logChatMessage(message: Component, type: RichChatType.Bound, prefix: String?) {
        if (!server.config.advanced.logPlayerChatMessages) return

        val messageText = LegacyComponentSerializer.legacySection().serialize(MinecraftTranslationManager.render(type.decorate(message)))
        if (prefix != null) LOGGER.info("[$prefix] $messageText") else LOGGER.info(messageText)
    }

    override fun getPermissionValue(permission: String): TriState = permissionFunction.getPermissionValue(permission)

    override fun isRunning(): Boolean = !server.isStopped() && server.isRunning()

    override fun runCommand(command: String) {
        val event = server.eventNode.fire(KryptonCommandExecuteEvent(this, command))
        if (event.isAllowed()) server.commandManager.dispatch(createCommandSourceStack(), event.result?.command ?: command)
    }

    override fun shutdown() {
        server.stop(false)
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader = super.buildReader(
        builder.appName("Krypton")
            .completer(BrigadierCompleter(server.commandManager) { createCommandSourceStack() })
            .highlighter(BrigadierHighlighter(server.commandManager) { createCommandSourceStack() })
            .option(LineReader.Option.COMPLETE_IN_WORD, true)
    )

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withStatic(Identity.NAME, NAME)
                .withStatic(Identity.DISPLAY_NAME, DISPLAY_NAME)
                .withStatic(PermissionChecker.POINTER, PermissionChecker { getPermissionValue(it) })
                .build()
        }
        return cachedPointers!!
    }

    override fun acceptsSuccess(): Boolean = true

    override fun acceptsFailure(): Boolean = true

    override fun shouldInformAdmins(): Boolean = server.config.server.broadcastConsoleToAdmins

    override fun createCommandSourceStack(): CommandSourceStack =
        CommandSourceStack(this, Position.ZERO, server.worldManager.default, NAME, DISPLAY_NAME, server, null)

    companion object {

        private const val NAME = "CONSOLE"
        private val DISPLAY_NAME = Component.text(NAME)

        private val DEFAULT_PERMISSION_FUNCTION = PermissionFunction.ALWAYS_TRUE
        private val LOGGER = LogManager.getLogger("CONSOLE")
    }
}
