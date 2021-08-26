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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.space.Location
import org.kryptonmc.krypton.command.CommandExceptions
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.buildCopy
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument

object TeleportCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal<Sender>("teleport")
            .permission("krypton.command.teleport", 2)
            .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                .executes {
                    teleport(it.source, it.argument<Coordinates>("location"))
                    1
                })
            .then(argument<Sender, EntityQuery>("players", EntityArgument.players())
                .executes {
                    val sender = it.source as? KryptonPlayer ?: return@executes 0
                    val players = it.entityArgument("players").getPlayers(sender)
                    if (players.size == 1) {
                        val player = players[0]
                        teleport(sender, player.location)
                        sender.sendMessage(translatable("commands.teleport.success.entity.single", text(sender.name), text(player.name)))
                    }
                    1
                }
                .then(argument<Sender, EntityQuery>("target", EntityArgument.player())
                    .executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val players = context.entityArgument("players").getPlayers(sender)
                        val target = context.entityArgument("target").getPlayers(sender)[0]
                        players.forEach { teleport(it, target.location) }
                        sender.sendMessage(translatable("commands.teleport.success.entity.multiple", text(players.size.toString()), text(target.name)))
                        1
                    })
                .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                    .executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val players = context.entityArgument("players").getPlayers(sender)
                        val location = context.argument<Coordinates>("location")
                        players.forEach { teleport(it, location) }
                        sender.sendMessage(translatable("commands.teleport.success.location.multiple",
                            text(players.size.toString()),
                            text(location.position(sender).blockX.toString()),
                            text(location.position(sender).blockY.toString()),
                            text(location.position(sender).blockZ.toString())
                        ))
                        1
                    })
                )
        )
        dispatcher.register(node.buildCopy("tp"))
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
