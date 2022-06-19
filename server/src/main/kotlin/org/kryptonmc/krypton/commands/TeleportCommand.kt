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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs

object TeleportCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal("teleport") {
            permission(KryptonPermission.TELEPORT)
            argument("location", VectorArgument.normal()) {
                runs {
                    val player = it.source as? Player ?: return@runs
                    player.teleport(it.argument<Coordinates>("location").position(player))
                }
            }
            argument("players", EntityArgument.players()) {
                runs {
                    val sender = it.source as? KryptonPlayer ?: return@runs
                    val players = it.entityArgument("players").players(sender)
                    if (players.size == 1) {
                        val player = players[0]
                        player.teleport(player.location)
                        sender.sendMessage(Component.translatable("commands.teleport.success.entity.single", sender.displayName, player.displayName))
                    }
                }
                argument("target", EntityArgument.players()) {
                    runs { context ->
                        val sender = context.source as? KryptonPlayer ?: return@runs
                        val players = context.entityArgument("players").players(sender)
                        val target = context.entityArgument("target").players(sender)[0]
                        players.forEach { it.teleport(target.location) }
                        val playerCount = Component.text(players.size.toString())
                        sender.sendMessage(Component.translatable("commands.teleport.success.entity.multiple", playerCount, target.displayName))
                    }
                }
                argument("location", VectorArgument.normal()) {
                    runs { context ->
                        val sender = context.source as? KryptonPlayer ?: return@runs
                        val players = context.entityArgument("players").players(sender)
                        val location = context.argument<Coordinates>("location").position(sender)
                        players.forEach { it.teleport(location) }
                        val playerCount = Component.text(players.size.toString())
                        val x = Component.text(location.floorX().toString())
                        val y = Component.text(location.floorY().toString())
                        val z = Component.text(location.floorZ().toString())
                        sender.sendMessage(Component.translatable("commands.teleport.success.location.multiple", playerCount, x, y, z))
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<Sender>("tp").redirect(node))
    }
}
