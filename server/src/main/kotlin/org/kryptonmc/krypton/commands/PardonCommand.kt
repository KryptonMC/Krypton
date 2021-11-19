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
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.command.permission

object PardonCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("pardon")
            .permission(KryptonPermission.PARDON)
            .then(argument<Sender, EntityQuery>("targets", GameProfileArgument)
                .suggests { context, builder ->
                    val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                    server.playerManager.bannedPlayers.forEach {
                        if (!builder.remainingLowerCase.matchesSubString(it.key.name.lowercase())) return@forEach
                        builder.suggest(it.key.name)
                    }
                    builder.buildFuture()
                }
                .executes {
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    unban(it.gameProfileArgument("targets").profiles(it.source), it.source, server)
                    1
                })
        )
    }

    private fun unban(targets: List<KryptonGameProfile>, sender: Sender, server: KryptonServer) = targets.forEach {
        if (!server.playerManager.bannedPlayers.contains(it)) return@forEach
        server.playerManager.bannedPlayers.remove(it)
        sender.sendMessage(translatable("commands.pardon.success", text(it.name)))
    }
}
