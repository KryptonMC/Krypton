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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
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
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedPlayerEntry
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.toComponent

class BanCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            literal<Sender>("ban")
                .then(argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                    .executes {
                        if ((it.source as? KryptonPlayer) == null) {
                            val sender = it.source as KryptonConsoleSender
                            val targets = it.gameProfileArgument("targets").getProfile(sender.server)
                            ban(targets, server = sender.server, sender = sender)
                        } else {
                            val sender = it.source as KryptonPlayer
                            val targets = it.gameProfileArgument("targets").getProfiles(sender)
                            ban(targets, server = sender.server, sender = sender)
                        }
                        1
                    }
                    .then(argument<Sender, String>("reason", string())
                        .executes {
                            val reason = it.argument<String>("reason")
                            if ((it.source as? KryptonPlayer) == null) {
                                val sender = it.source as KryptonConsoleSender
                                val targets = it.gameProfileArgument("targets").getProfile(sender.server)
                                ban(targets, server = sender.server, reason = reason, sender = sender)
                            } else {
                                val sender = it.source as KryptonPlayer
                                val targets = it.gameProfileArgument("targets").getProfiles(sender)
                                ban(targets, server = sender.server, reason = reason, sender = sender)
                            }
                            1
                        })
                )
        )
    }

    private fun ban(
        profiles: List<GameProfile>,
        reason: String = "Banned by operator.",
        server: KryptonServer,
        sender: Sender
    ) {
        for (target in profiles) {
            if (!server.playerManager.bannedPlayers.contains(target)) {
                val entry = BannedPlayerEntry(target, reason = reason)
                server.playerManager.bannedPlayers += entry
                if (server.player(target.uuid) != null) kick(entry, server.player(target.uuid)!!)
                sender.sendMessage(translatable("commands.ban.success", listOf(text(target.name), text(reason))))
            }
        }
    }

    private fun kick(entry: BannedPlayerEntry, player: KryptonPlayer) {
        val text = Component.translatable("multiplayer.disconnect.banned.reason", listOf(entry.reason.toComponent()))
        if (entry.expiryDate != null) text.append(
            Component.translatable(
                "multiplayer.disconnect.banned.expiration", listOf(
                    BanEntry.DATE_FORMAT.format(entry.expiryDate).toComponent()
                )
            )
        )
        player.disconnect(text)
    }
}
