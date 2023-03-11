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
