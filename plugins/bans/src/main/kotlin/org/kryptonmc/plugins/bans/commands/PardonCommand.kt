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

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.plugins.bans.storage.KryptonBanManager

class PardonCommand(private val banManager: KryptonBanManager) {

    fun create(): LiteralArgumentBuilder<CommandExecutionContext> = literalCommand("pardon") {
        requires { it.sender.hasPermission("krypton.command.pardon") }
        argument(TARGETS, GameProfileArgument) {
            suggests { _, builder -> CommandSuggestionProvider.suggest(banManager.profileNames(), builder) }
            executes { unban(it.source, GameProfileArgument.get(it, TARGETS)) }
        }
    }

    private fun unban(context: CommandExecutionContext, profiles: Collection<GameProfile>): Int {
        var count = 0
        profiles.forEach {
            if (!banManager.isBanned(it)) return@forEach
            banManager.removeBan(it)
            ++count
            context.sendSuccessMessage(Component.translatable("commands.pardon.success", Component.text(it.name)))
        }
        if (count == 0) throw ERROR_NOT_BANNED.create()
        return count
    }

    companion object {

        private const val TARGETS = "targets"
        private val ERROR_NOT_BANNED = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.pardon.failed")))
    }
}
