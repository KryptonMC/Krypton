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
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.suggest
import org.kryptonmc.krypton.util.toComponent

internal class PardonCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            literal<Sender>("pardon")
                .permission("krypton.command.pardon", PermissionLevel.LEVEL_3)
                .then(
                    argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                        .suggests { context, builder ->
                            builder.suggest((context.source.server as KryptonServer).playerManager.bannedPlayers.map { it.key.name })
                        }
                        .executes {
                            unban(
                                it.gameProfileArgument("targets").getProfiles(it.source),
                                server = it.source.server as KryptonServer,
                                sender = it.source
                            )
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
