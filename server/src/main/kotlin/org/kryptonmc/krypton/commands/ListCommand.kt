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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.CommandMessages

object ListCommand {

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("list") {
            requiresPermission(KryptonPermission.LIST)
            runs { context -> sendNames(context.source) { PlainTextComponentSerializer.plainText().serialize(it.displayName) } }
            literal("uuids") {
                runs { context ->
                    sendNames(context.source) { "${PlainTextComponentSerializer.plainText().serialize(it.displayName)} (${it.uuid})" }
                }
            }
        })
    }

    @JvmStatic
    private inline fun sendNames(source: CommandSourceStack, nameGetter: (KryptonPlayer) -> String) {
        val names = source.server.players.map(nameGetter)
        CommandMessages.LIST_PLAYERS.sendSuccess(source, source.server.config.status.maxPlayers, names, false)
    }
}
