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
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.server.ban.KryptonProfileBan

object BanCommand {

    private const val TARGETS = "targets"
    private const val REASON = "reason"
    private const val DEFAULT_REASON = "Banned by operator."

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("ban") {
            permission(KryptonPermission.BAN)
            argument(TARGETS, GameProfileArgument) {
                runs { ban(it.source, GameProfileArgument.get(it, TARGETS), DEFAULT_REASON) }
                argument(REASON, StringArgumentType.string()) {
                    runs { ban(it.source, GameProfileArgument.get(it, TARGETS), it.getArgument(REASON)) }
                }
            }
        })
    }

    @JvmStatic
    private fun ban(source: CommandSourceStack, profiles: List<GameProfile>, reason: String) {
        val banManager = source.server.playerManager.banManager
        profiles.forEach { profile ->
            if (banManager.isBanned(profile)) return@forEach
            val ban = KryptonProfileBan(profile, reason = LegacyComponentSerializer.legacySection().deserialize(reason))
            banManager.add(ban)
            source.sendSuccess(Messages.Commands.BAN_SUCCESS.build(profile.name, reason), true)
            source.server.getPlayer(profile.uuid)?.disconnect(Messages.Disconnect.BANNED_MESSAGE.build(ban.reason, ban.expirationDate))
        }
    }
}
