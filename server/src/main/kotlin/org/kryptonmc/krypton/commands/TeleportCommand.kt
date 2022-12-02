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
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object TeleportCommand {

    private const val LOCATION = "location"
    private const val PLAYERS = "players"
    private const val TARGET = "target"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val node = dispatcher.register(literal("teleport") {
            permission(KryptonPermission.TELEPORT)
            argument(LOCATION, VectorArgument.normal()) {
                runs { it.source.getPlayerOrError().teleport(it.argument<Coordinates>(LOCATION).position(it.source)) }
            }
            argument(PLAYERS, EntityArgumentType.players()) {
                runs {
                    val players = it.entityArgument(PLAYERS).players(it.source)
                    if (players.size == 1) {
                        val player = players.get(0)
                        player.teleport(player.position)
                        it.source.sendSuccess(Messages.Commands.TELEPORT_SINGLE_ENTITY.build(it.source.displayName, player.displayName), true)
                    }
                }
                argument(TARGET, EntityArgumentType.players()) {
                    runs { context ->
                        val players = context.entityArgument(PLAYERS).players(context.source)
                        val target = context.entityArgument(TARGET).players(context.source).get(0)
                        players.forEach { it.teleport(target.position) }
                        context.source.sendSuccess(Messages.Commands.TELEPORT_MULTIPLE_ENTITIES.build(players.size, target.displayName), true)
                    }
                }
                argument(LOCATION, VectorArgument.normal()) {
                    runs { context ->
                        val players = context.entityArgument(PLAYERS).players(context.source)
                        val location = context.argument<Coordinates>(LOCATION).position(context.source)
                        players.forEach { it.teleport(location) }
                        context.source.sendSuccess(Messages.Commands.TELEPORT_MULTIPLE_LOCATIONS.build(players.size, location), true)
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<CommandSourceStack>("tp").redirect(node))
    }
}
