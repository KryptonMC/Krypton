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
import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.server.ban.BanEntry
import org.kryptonmc.krypton.server.ban.BannedPlayerEntry
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs

object BanCommand : InternalCommand {

    private const val TARGETS_ARGUMENT = "targets"
    private const val REASON_ARGUMENT = "reason"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("ban") {
            permission(KryptonPermission.BAN)
            argument(TARGETS_ARGUMENT, GameProfileArgument) {
                runs {
                    val server = it.source.server as? KryptonServer ?: return@runs
                    ban(it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source), server, it.source)
                    Command.SINGLE_SUCCESS
                }
                argument(REASON_ARGUMENT, StringArgumentType.string()) {
                    runs {
                        val server = it.source.server as? KryptonServer ?: return@runs
                        ban(it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source), server, it.source, it.argument(REASON_ARGUMENT))
                        Command.SINGLE_SUCCESS
                    }
                }
            }
        })
    }

    @JvmStatic
    private fun ban(profiles: List<GameProfile>, server: KryptonServer, sender: Sender, reason: String = "Banned by operator.") {
        val bannedPlayers = server.playerManager.bannedPlayers
        profiles.forEach { profile ->
            if (bannedPlayers.contains(profile)) return@forEach
            val entry = BannedPlayerEntry(profile, reason = LegacyComponentSerializer.legacySection().deserialize(reason))
            bannedPlayers.add(entry)
            server.player(profile.uuid)?.let { kick(entry, it) }
            sender.sendMessage(Component.translatable("commands.ban.success", Component.text(profile.name), Component.text(reason)))
        }
    }

    @JvmStatic
    private fun kick(entry: BannedPlayerEntry, player: KryptonPlayer) {
        val text = Component.translatable("multiplayer.disconnect.banned.reason", entry.reason)
        if (entry.expirationDate != null) {
            val expirationDate = Component.text(BanEntry.DATE_FORMATTER.format(entry.expirationDate))
            text.append(Component.translatable("multiplayer.disconnect.banned.expiration", expirationDate))
        }
        player.disconnect(text)
    }
}
