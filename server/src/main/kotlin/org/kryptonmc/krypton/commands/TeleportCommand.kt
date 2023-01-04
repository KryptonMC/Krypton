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
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.locale.CommandMessages

object TeleportCommand {

    private const val LOCATION = "location"
    private const val PLAYERS = "players"
    private const val TARGET = "target"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val node = dispatcher.register(literal("teleport") {
            requiresPermission(KryptonPermission.TELEPORT)
            argument(LOCATION, VectorArgument.normal()) {
                runs { it.source.getPlayerOrError().teleport(VectorArgument.get(it, LOCATION)) }
            }
            argument(PLAYERS, EntityArgumentType.players()) {
                runs {
                    val players = EntityArgumentType.getPlayers(it, PLAYERS)
                    if (players.size == 1) {
                        val player = players.get(0)
                        player.teleport(player.position)
                        CommandMessages.TELEPORT_SINGLE.sendSuccess(it.source, it.source.displayName, player.displayName, true)
                    }
                }
                argument(TARGET, EntityArgumentType.players()) {
                    runs { context ->
                        val players = EntityArgumentType.getPlayers(context, PLAYERS)
                        val target = EntityArgumentType.getPlayers(context, TARGET).get(0)
                        players.forEach { it.teleport(target.position) }
                        CommandMessages.TELEPORT_ENTITY_MULTIPLE.sendSuccess(context.source, players.size, target.displayName, true)
                    }
                }
                argument(LOCATION, VectorArgument.normal()) {
                    runs { context ->
                        val players = EntityArgumentType.getPlayers(context, PLAYERS)
                        val location = VectorArgument.get(context, LOCATION)
                        players.forEach { it.teleport(location) }
                        CommandMessages.TELEPORT_LOCATION_MULTIPLE.sendSuccess(context.source, players.size, location, true)
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<CommandSourceStack>("tp").redirect(node))
    }
}
