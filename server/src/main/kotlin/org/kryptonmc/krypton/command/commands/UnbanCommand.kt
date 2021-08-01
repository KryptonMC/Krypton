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
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameprofile.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameprofile.gameProfileArgument
import org.kryptonmc.krypton.console.KryptonConsoleSender
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.toComponent

internal class UnbanCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Sender>("pardon")
                .then(
                    argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                        .executes {
                            if (it.source !is KryptonPlayer) {
                                val sender = it.source as KryptonConsoleSender
                                val targets = it.gameProfileArgument("targets").getProfile(sender.server)
                                unban(listOf(targets), sender, sender.server)
                            } else {
                                val sender = it.source as KryptonPlayer
                                val targets = it.gameProfileArgument("targets").getProfiles(sender)
                                unban(targets, server = sender.server, sender = sender)
                            }
                            1
                        })
        )
    }

    private fun unban(targets: List<GameProfile>, sender: Sender, server: KryptonServer) {
        for (target in targets) {
            if (server.playerManager.bannedPlayers.contains(target)) {
                server.playerManager.bannedPlayers -= target
                sender.sendMessage(translatable("commands.pardon.success", listOf(target.name.toComponent())))
            }
        }
    }
}
