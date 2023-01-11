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
package org.kryptonmc.plugins.bans.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.plugins.bans.api.ProfileBan
import org.kryptonmc.plugins.bans.storage.KryptonBanManager

class BanCommand(private val banManager: KryptonBanManager) {

    fun create(): LiteralArgumentBuilder<CommandExecutionContext> = literalCommand("ban") {
        requires { it.sender.hasPermission("krypton.command.ban") }
        argument(TARGETS, GameProfileArgument) {
            executes { ban(it.source, GameProfileArgument.get(it, TARGETS), null) }
            // TODO: Use a component-based argument type when we have one
            argument(REASON, StringArgumentType.string()) {
                executes { ban(it.source, GameProfileArgument.get(it, TARGETS), StringArgumentType.getString(it, REASON)) }
            }
        }
    }

    private fun ban(source: CommandExecutionContext, profiles: List<GameProfile>, reason: String?): Int {
        var count = 0
        profiles.forEach {
            if (banManager.isBanned(it)) return@forEach
            val ban = ProfileBan(it, source.textName, reason, null, null)
            banManager.addBan(ban)
            ++count
            source.sendSuccessMessage(Component.translatable("commands.ban.success", Component.text(it.name), Component.text(ban.reason)))
            source.server.getPlayer(it.uuid)?.disconnect(Component.translatable("multiplayer.disconnect.banned"))
        }
        if (count == 0) throw ERROR_ALREADY_BANNED.create()
        return count
    }

    companion object {

        private const val TARGETS = "targets"
        private const val REASON = "reason"
        private val ERROR_ALREADY_BANNED = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.ban.failed")))
    }
}
