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
import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.locale.DisconnectMessages

object KickCommand {

    private const val TARGETS = "targets"
    private const val REASON = "reason"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("kick") {
            requiresPermission(KryptonPermission.KICK)
            argument(TARGETS, EntityArgumentType.players()) {
                runs { context -> EntityArgumentType.getPlayers(context, TARGETS).forEach { it.disconnect(DisconnectMessages.KICKED) } }
                argument(REASON, StringArgumentType.string()) {
                    runs { context ->
                        val reason = context.getArgument(REASON, String::class.java)
                        EntityArgumentType.getPlayers(context, TARGETS).forEach {
                            it.disconnect(DisconnectMessages.KICKED.append(Component.text(" Reason: $reason")))
                        }
                    }
                }
            }
        })
    }
}
