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
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.argument

class KickCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("kick")
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .executes {
                    if (it.source is KryptonPlayer) {
                        val players = it.entityArgument("targets").getPlayers(it.source as KryptonPlayer)
                        for (player in players) {
                            player.kick()
                        }
                    } else {
                        val player = it.entityArgument("targets").getPlayer(it.source.server as KryptonServer)
                            player.kick()

                    }
                    1
                }
                .then(argument<Sender, String>("reason", string())
                    .executes {
                        val reason = it.argument<String>("reason")
                        if (it.source is KryptonPlayer) {
                            val players = it.entityArgument("targets").getPlayers(it.source as KryptonPlayer)
                            for (player in players) {
                                player.kick(reason)
                            }
                        } else {
                            val player = it.entityArgument("targets").getPlayer(it.source.server as KryptonServer)
                                player.kick(reason)

                        }
                        1
                    })
            )
        )
    }
}
