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

import com.mojang.brigadier.Command
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
import org.spongepowered.math.vector.Vector3d

object TeleportCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal("teleport") {
            permission(KryptonPermission.TELEPORT)
            argument("location", VectorArgument.normal()) {
                executes {
                    teleport(it.source, it.argument<Coordinates>("location"))
                    Command.SINGLE_SUCCESS
                }
            }
            argument("players", EntityArgument.players()) {
                executes {
                    val sender = it.source as? KryptonPlayer ?: return@executes 0
                    val players = it.entityArgument("players").players(sender)
                    if (players.size == 1) {
                        val player = players[0]
                        teleport(sender, player.location)
                        sender.sendMessage(Component.translatable("commands.teleport.success.entity.single", sender.displayName, player.displayName))
                    }
                    Command.SINGLE_SUCCESS
                }
                argument("target", EntityArgument.players()) {
                    executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val players = context.entityArgument("players").players(sender)
                        val target = context.entityArgument("target").players(sender)[0]
                        players.forEach { teleport(it, target.location) }
                        val playerCount = Component.text(players.size.toString())
                        sender.sendMessage(Component.translatable("commands.teleport.success.entity.multiple", playerCount, target.displayName))
                        Command.SINGLE_SUCCESS
                    }
                }
                argument("location", VectorArgument.normal()) {
                    executes { context ->
                        val sender = context.source as? KryptonPlayer ?: return@executes 0
                        val players = context.entityArgument("players").players(sender)
                        val location = context.argument<Coordinates>("location")
                        players.forEach { teleport(it, location) }
                        val position = location.position(sender)
                        sender.sendMessage(Component.translatable(
                            "commands.teleport.success.location.multiple",
                            Component.text(players.size.toString()),
                            Component.text(position.floorX().toString()),
                            Component.text(position.floorY().toString()),
                            Component.text(position.floorZ().toString())
                        ))
                        1
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<Sender>("tp").redirect(node))
    }

    @JvmStatic
    private fun teleport(player: Sender, location: Coordinates) {
        if (player !is Player) return
        player.teleport(location.position(player))
    }

    @JvmStatic
    private fun teleport(player: Sender, location: Vector3d) {
        if (player !is Player) return
        player.teleport(location)
    }
}
