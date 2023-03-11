/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
