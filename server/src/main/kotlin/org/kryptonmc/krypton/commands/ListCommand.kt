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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission

object ListCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("list") {
            permission(KryptonPermission.LIST)
            executes {
                val server = it.source.server as? KryptonServer ?: return@executes 0
                sendNames(it.source, server)
                Command.SINGLE_SUCCESS
            }
            literal("uuids") {
                executes {
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    sendNamesWithUUID(it.source, server)
                    Command.SINGLE_SUCCESS
                }
            }
        })
    }

    @JvmStatic
    private fun sendNames(sender: Sender, server: KryptonServer) {
        val names = server.players.map { it.displayName.toPlainText() }
        sender.sendMessage(Component.translatable(
            "commands.list.players",
            Component.text(names.size),
            Component.text(server.maxPlayers),
            Component.text(names.joinToString("\n"))
        ))
    }

    @JvmStatic
    private fun sendNamesWithUUID(sender: Sender, server: KryptonServer) {
        val names = server.players.map { "${it.displayName.toPlainText()} (${it.uuid})" }
        sender.sendMessage(Component.translatable(
            "commands.list.players",
            Component.text(names.size),
            Component.text(server.maxPlayers),
            Component.text(names.joinToString("\n"))
        ))
    }
}
