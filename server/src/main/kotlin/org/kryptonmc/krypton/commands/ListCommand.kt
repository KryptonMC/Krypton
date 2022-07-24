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
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs

object ListCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("list") {
            permission(KryptonPermission.LIST)
            runs { context ->
                val server = context.source.server as? KryptonServer ?: return@runs
                sendNames(context.source, server, server.members.map { it.displayName.toPlainText() })
            }
            literal("uuids") {
                runs { context ->
                    val server = context.source.server as? KryptonServer ?: return@runs
                    sendNames(context.source, server, server.members.map { "${it.displayName.toPlainText()} (${it.uuid})" })
                }
            }
        })
    }

    @JvmStatic
    private fun sendNames(sender: Sender, server: KryptonServer, names: List<String>) {
        val maxPlayers = Component.text(server.maxPlayers)
        val players = Component.text(names.joinToString("\n"))
        sender.sendMessage(Component.translatable("commands.list.players", Component.text(names.size), maxPlayers, players))
    }
}
