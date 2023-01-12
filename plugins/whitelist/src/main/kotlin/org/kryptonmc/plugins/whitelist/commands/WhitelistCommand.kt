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
package org.kryptonmc.plugins.whitelist.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literal
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.plugins.whitelist.config.WhitelistConfig
import org.kryptonmc.plugins.whitelist.storage.WhitelistStorage

class WhitelistCommand(private val config: WhitelistConfig, private val storage: WhitelistStorage) {

    fun create(): LiteralArgumentBuilder<CommandExecutionContext> = literalCommand("whitelist") {
        requires { it.sender.hasPermission("krypton.command.whitelist") }
        literal("on") {
            executes { enableWhitelist(it.source) }
        }
        literal("off") {
            executes { disableWhitelist(it.source) }
        }
        literal("list") {
            executes { showList(it.source as CommandSourceStack) }
        }
        literal("add") {
            argument(TARGETS, GameProfileArgument) {
                suggests { context, builder ->
                    val players = context.source.server.players.stream()
                        .filter { !storage.isWhitelisted(it.profile) }
                        .map { it.profile.name }
                    CommandSuggestionProvider.suggest(players, builder)
                }
                executes { addPlayers(it.source, GameProfileArgument.get(it, TARGETS)) }
            }
        }
        literal("remove") {
            argument(TARGETS, GameProfileArgument) {
                suggests { _, builder -> CommandSuggestionProvider.suggest(storage.whitelistedProfileNames(), builder) }
                executes { removePlayers(it.source, GameProfileArgument.get(it, TARGETS)) }
            }
        }
        literal("reload") {
            executes { reload(it.source) }
        }
    }

    private fun reload(context: CommandExecutionContext): Int {
        storage.reload()
        context.sendSuccessMessage(Component.translatable("commands.whitelist.reloaded"))
        kickUnlistedPlayers(context)
        return 1
    }

    private fun kickUnlistedPlayers(context: CommandExecutionContext) {
        if (!config.enforce) return
        context.server.players.forEach { player ->
            if (!storage.isWhitelisted(player.profile)) player.disconnect(Component.translatable("multiplayer.disconnect.not_whitelisted"))
        }
    }

    private fun addPlayers(context: CommandExecutionContext, profiles: Collection<GameProfile>): Int {
        var count = 0
        profiles.forEach {
            if (storage.isWhitelisted(it)) return@forEach
            storage.whitelist(it)
            context.sendSuccessMessage(Component.translatable("commands.whitelist.add.success", Component.text(it.name)))
            ++count
        }
        if (count == 0) throw ERROR_ALREADY_WHITELISTED.create()
        return count
    }

    private fun removePlayers(context: CommandExecutionContext, profiles: Collection<GameProfile>): Int {
        var count = 0
        profiles.forEach {
            if (!storage.isWhitelisted(it)) return@forEach
            storage.removeWhitelisted(it)
            context.sendSuccessMessage(Component.translatable("commands.whitelist.remove.success", Component.text(it.name)))
            ++count
        }
        if (count == 0) throw ERROR_NOT_WHITELISTED.create()
        kickUnlistedPlayers(context)
        return count
    }

    private fun enableWhitelist(context: CommandExecutionContext): Int {
        if (storage.isEnabled()) throw ERROR_ALREADY_ENABLED.create()
        storage.enable()
        context.sendSuccessMessage(Component.translatable("commands.whitelist.enabled"))
        kickUnlistedPlayers(context)
        return 1
    }

    private fun disableWhitelist(context: CommandExecutionContext): Int {
        if (!storage.isEnabled()) throw ERROR_ALREADY_DISABLED.create()
        storage.disable()
        context.sendSuccessMessage(Component.translatable("commands.whitelist.disabled"))
        return 1
    }

    private fun showList(context: CommandSourceStack): Int {
        val names = storage.whitelistedProfileNames()
        if (names.isEmpty()) {
            context.sendSuccess(Component.translatable("commands.whitelist.none"), false)
        } else {
            val nameString = Component.text(names.joinToString(", "))
            context.sendSuccess(Component.translatable("commands.whitelist.list", Component.text(names.size), nameString), false)
        }
        return names.size
    }

    companion object {

        private const val TARGETS = "targets"

        private val ERROR_ALREADY_ENABLED = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.whitelist.enabled")))
        private val ERROR_ALREADY_DISABLED = SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.whitelist.disabled")))
        private val ERROR_ALREADY_WHITELISTED =
            SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.whitelist.add.failed")))
        private val ERROR_NOT_WHITELISTED =
            SimpleCommandExceptionType(AdventureMessage.of(Component.translatable("commands.whitelist.remove.failed")))
    }
}
