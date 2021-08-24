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
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedPlayerEntry
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.toComponent

object BanCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("ban")
            .permission("krypton.command.ban", 3)
            .then(argument<Sender, EntityQuery>("targets", GameProfileArgument())
                .executes {
                    val server = it.source.server as? KryptonServer ?: return@executes 0
                    ban(it.gameProfileArgument("targets").getProfiles(it.source), server, it.source)
                    1
                }.then(argument<Sender, String>("reason", string())
                    .executes {
                        val server = it.source.server as? KryptonServer ?: return@executes 0
                        val reason = it.argument<String>("reason")
                        ban(it.gameProfileArgument("targets").getProfiles(it.source), server, it.source, reason)
                        1
                    })
            )
        )
    }

    private fun ban(
        profiles: List<KryptonGameProfile>,
        server: KryptonServer,
        sender: Sender,
        reason: String = "Banned by operator.",
    ) = profiles.forEach { profile ->
        if (server.playerManager.bannedPlayers.contains(profile)) return@forEach
        val entry = BannedPlayerEntry(profile, reason = reason)
        server.playerManager.bannedPlayers += entry
        server.player(profile.uuid)?.let { kick(entry, it) }
        sender.sendMessage(translatable("commands.ban.success", text(profile.name), text(reason)))
        logBan(profile.name, sender.name, reason, server)
    }

    private fun kick(entry: BannedPlayerEntry, player: KryptonPlayer) {
        val text = translatable("multiplayer.disconnect.banned.reason", entry.reason.toComponent())
        if (entry.expiryDate != null) text.append(translatable("multiplayer.disconnect.banned.expiration", BanEntry.DATE_FORMATTER.format(entry.expiryDate).toComponent()))
        player.disconnect(text)
    }

    private fun logBan(
        target: String,
        source: String,
        reason: String,
        server: KryptonServer
    ) = server.console.sendMessage(translatable("commands.banlist.entry", target.toComponent(), source.toComponent(), reason.toComponent()))
}
