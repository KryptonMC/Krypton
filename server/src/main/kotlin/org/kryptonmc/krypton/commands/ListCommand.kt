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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.toPlainText
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages

object ListCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("list") {
            permission(KryptonPermission.LIST)
            runs { context -> sendNames(context.source) { it.displayName.toPlainText() } }
            literal("uuids") {
                runs { context -> sendNames(context.source) { "${it.displayName.toPlainText()} (${it.uuid})" } }
            }
        })
    }

    @JvmStatic
    private inline fun sendNames(sender: Sender, nameGetter: (KryptonPlayer) -> String) {
        val server = sender.server as? KryptonServer ?: return
        val names = server.players.map(nameGetter)
        Messages.Commands.LIST_PLAYERS.send(sender, names.size, server.config.status.maxPlayers, names.joinToString("\n"))
    }
}
