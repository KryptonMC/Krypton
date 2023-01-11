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
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.plugins.bans.storage.KryptonBanManager

class PardonIpCommand(private val banManager: KryptonBanManager) {

    fun create(): LiteralArgumentBuilder<CommandExecutionContext> = literalCommand("pardon-ip") {
        requires { it.sender.hasPermission("krypton.command.pardonip") }
        argument(TARGET, StringArgumentType.word()) {
            suggests { _, builder -> CommandSuggestionProvider.suggest(banManager.ipStrings(), builder) }
            executes { unban(it.source, StringArgumentType.getString(it, TARGET)) }
        }
    }

    private fun unban(context: CommandExecutionContext, ip: String): Int {
        if (!BanCommandUtil.IP_ADDRESS_PATTERN.matcher(ip).matches()) throw ERROR_INVALID.create()
        if (!banManager.isBanned(ip)) throw ERROR_NOT_BANNED.create()
        banManager.removeBan(ip)
        context.sendSuccessMessage(Component.translatable("commands.pardonip.success", Component.text(ip)))
        return 1
    }

    companion object {

        private const val TARGET = "target"
        private val ERROR_INVALID = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.pardonip.invalid")))
        private val ERROR_NOT_BANNED = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.pardonip.failed")))
    }
}
