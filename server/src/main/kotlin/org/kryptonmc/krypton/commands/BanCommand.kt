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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
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
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.server.ban.KryptonProfileBan

object BanCommand : InternalCommand {

    private const val TARGETS_ARGUMENT = "targets"
    private const val REASON_ARGUMENT = "reason"
    private const val DEFAULT_REASON = "Banned by operator."

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("ban") {
            permission(KryptonPermission.BAN)
            argument(TARGETS_ARGUMENT, GameProfileArgument) {
                runs { ban(it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source), it.source, DEFAULT_REASON) }
                argument(REASON_ARGUMENT, StringArgumentType.string()) {
                    runs { ban(it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source), it.source, it.argument(REASON_ARGUMENT)) }
                }
            }
        })
    }

    @JvmStatic
    private fun ban(profiles: List<GameProfile>, sender: Sender, reason: String) {
        val server = sender.server as? KryptonServer ?: return
        val banManager = server.playerManager.banManager
        profiles.forEach { profile ->
            if (banManager.isBanned(profile)) return@forEach
            val ban = KryptonProfileBan(profile, reason = LegacyComponentSerializer.legacySection().deserialize(reason))
            banManager.add(ban)
            server.getPlayer(profile.uuid)?.let { kick(ban, it) }
            Messages.Commands.BAN_SUCCESS.send(sender, profile.name, reason)
        }
    }

    @JvmStatic
    private fun kick(ban: KryptonProfileBan, player: KryptonPlayer) {
        player.disconnect(Messages.Disconnect.BANNED_MESSAGE.build(ban.reason, ban.expirationDate))
    }
}
