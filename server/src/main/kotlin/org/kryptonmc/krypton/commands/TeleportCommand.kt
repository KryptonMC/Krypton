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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object TeleportCommand : InternalCommand {

    private const val LOCATION_ARGUMENT = "location"
    private const val PLAYERS_ARGUMENT = "players"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal("teleport") {
            permission(KryptonPermission.TELEPORT)
            argument(LOCATION_ARGUMENT, VectorArgument.normal()) {
                runs {
                    val player = it.source as? Player ?: return@runs
                    player.teleport(it.argument<Coordinates>(LOCATION_ARGUMENT).position(player))
                }
            }
            argument(PLAYERS_ARGUMENT, EntityArgumentType.players()) {
                runs {
                    val sender = it.source as? KryptonPlayer ?: return@runs
                    val players = it.entityArgument(PLAYERS_ARGUMENT).players(sender)
                    if (players.size == 1) {
                        val player = players.get(0)
                        player.teleport(player.location)
                        Messages.Commands.TELEPORT_SINGLE_ENTITY.send(sender, sender.displayName, player.displayName)
                    }
                }
                argument("target", EntityArgumentType.players()) {
                    runs { context ->
                        val sender = context.source as? KryptonPlayer ?: return@runs
                        val players = context.entityArgument(PLAYERS_ARGUMENT).players(sender)
                        val target = context.entityArgument("target").players(sender).get(0)
                        players.forEach { it.teleport(target.location) }
                        Messages.Commands.TELEPORT_MULTIPLE_ENTITIES.send(sender, players.size, target.displayName)
                    }
                }
                argument(LOCATION_ARGUMENT, VectorArgument.normal()) {
                    runs { context ->
                        val sender = context.source as? KryptonPlayer ?: return@runs
                        val players = context.entityArgument(PLAYERS_ARGUMENT).players(sender)
                        val location = context.argument<Coordinates>(LOCATION_ARGUMENT).position(sender)
                        players.forEach { it.teleport(location) }
                        Messages.Commands.TELEPORT_MULTIPLE_LOCATIONS.send(sender, players.size, location)
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<Sender>("tp").redirect(node))
    }
}
