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
package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.world.Location
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.toComponent

internal object TeleportCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal<Sender>("teleport")
            .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                .executes {
                    teleport(it.source, it.getArgument("location", Coordinates::class.java)); 1
                })
            .then(argument<Sender, EntityQuery>("players", EntityArgument.players())
                .executes {
                    val sender = it.source as? KryptonPlayer ?: return@executes 1
                    val players = it.entityArgument("players").getPlayers(sender)
                    if (players.size == 1) {
                        val player = players[0]
                        teleport(sender, player.location)
                        sender.sendMessage(
                            translatable(
                                "commands.teleport.success.entity.single",
                                listOf(sender.name.toComponent(), player.name.toComponent())
                            )
                        )
                    }
                    1
                }
                .then(argument<Sender, EntityQuery>("target", EntityArgument.player())
                    .executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                        val players = it.entityArgument("players").getPlayers(sender)
                        val target = it.entityArgument("target").getPlayer(sender.server)
                        for (player in players) {
                            teleport(player, target.location)
                        }
                        sender.sendMessage(
                            translatable(
                                "commands.teleport.success.entity.multiple",
                                listOf(players.size.toString().toComponent(), target.name.toComponent())
                            )
                        )
                        1
                    })
                .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                    .executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                        val players = it.entityArgument("players").getPlayers(sender)
                        val location = it.argument<Coordinates>("location")
                        for (player in players) {
                            teleport(player, location)
                        }
                        sender.sendMessage(
                            translatable(
                                "commands.teleport.success.location.multiple",
                                listOf(
                                    players.size.toString().toComponent(),
                                    location.position(sender).blockX.toString().toComponent(),
                                    location.position(sender).blockY.toString().toComponent(),
                                    location.position(sender).blockZ.toString().toComponent()
                                )
                            )
                        )
                        1
                    })
            )
        )
        dispatcher.register(literal<Sender>("tp").redirect(node))
    }

    private fun teleport(player: Sender, location: Coordinates) {
        if (player !is Player) return
        player.teleport(location.position(player))
    }

    private fun teleport(player: Sender, location: Location) {
        if (player !is Player) return
        player.teleport(location)
    }
}
