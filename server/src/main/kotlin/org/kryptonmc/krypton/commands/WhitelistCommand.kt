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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literal
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.locale.DisconnectMessages
import java.io.IOException

object WhitelistCommand {

    private val LOGGER = LogManager.getLogger()
    private val ERROR_ALREADY_ENABLED = CommandExceptions.simple("commands.whitelist.alreadyOn")
    private val ERROR_ALREADY_DISABLED = CommandExceptions.simple("commands.whitelist.alreadyOff")
    private val ERROR_ALREADY_WHITELISTED = CommandExceptions.simple("commands.whitelist.add.failed")
    private val ERROR_NOT_WHITELISTED = CommandExceptions.simple("commands.whitelist.remove.failed")

    private const val TARGETS = "targets"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("whitelist") {
            requiresPermission(KryptonPermission.WHITELIST)
            literal("on") {
                runs { enableWhitelist(it.source) }
            }
            literal("off") {
                runs { disableWhitelist(it.source) }
            }
            literal("list") {
                executes { showList(it.source) }
            }
            literal("add") {
                argument(TARGETS, GameProfileArgument) {
                    suggests { context, builder ->
                        val playerManager = context.source.server.playerManager
                        val values = playerManager.players().stream()
                            .filter { !playerManager.whitelistManager.isWhitelisted(it.profile) }
                            .map { it.profile.name }
                        CommandSuggestionProvider.suggest(values, builder)
                    }
                    executes { addPlayers(it.source, GameProfileArgument.get(it, TARGETS)) }
                }
            }
            literal("remove") {
                argument(TARGETS, GameProfileArgument) {
                    suggests { context, builder -> CommandSuggestionProvider.suggest(getWhitelistNames(context.source.server), builder) }
                    executes { removePlayers(it.source, GameProfileArgument.get(it, TARGETS)) }
                }
            }
            literal("reload") {
                runs { reload(it.source) }
            }
        })
    }

    @JvmStatic
    private fun reload(source: CommandSourceStack) {
        reloadWhitelist(source.server)
        source.sendSuccess(CommandMessages.WHITELIST_RELOADED, true)
        kickUnlistedPlayers(source.server)
    }

    @JvmStatic
    private fun addPlayers(source: CommandSourceStack, profiles: Collection<GameProfile>): Int {
        val whitelistManager = source.server.playerManager.whitelistManager

        var count = 0
        profiles.forEach {
            if (whitelistManager.isWhitelisted(it)) return@forEach
            whitelistManager.whitelist(it)
            CommandMessages.WHITELIST_ADD.sendSuccess(source, it, true)
            ++count
        }
        if (count == 0) throw ERROR_ALREADY_WHITELISTED.create()
        return count
    }

    @JvmStatic
    private fun removePlayers(source: CommandSourceStack, profiles: Collection<GameProfile>): Int {
        val whitelistManager = source.server.playerManager.whitelistManager

        var count = 0
        profiles.forEach {
            if (!whitelistManager.isWhitelisted(it)) return@forEach
            whitelistManager.removeWhitelisted(it)
            CommandMessages.WHITELIST_REMOVE.sendSuccess(source, it, true)
            ++count
        }

        if (count == 0) throw ERROR_NOT_WHITELISTED.create()
        kickUnlistedPlayers(source.server)
        return count
    }

    @JvmStatic
    private fun enableWhitelist(source: CommandSourceStack) {
        val whitelistManager = source.server.playerManager.whitelistManager
        if (whitelistManager.isEnabled()) throw ERROR_ALREADY_ENABLED.create()
        whitelistManager.enable()
        source.sendSuccess(CommandMessages.WHITELIST_ENABLED, true)
        kickUnlistedPlayers(source.server)
    }

    @JvmStatic
    private fun disableWhitelist(source: CommandSourceStack) {
        val whitelistManager = source.server.playerManager.whitelistManager
        if (!whitelistManager.isEnabled()) throw ERROR_ALREADY_DISABLED.create()
        whitelistManager.disable()
        source.sendSuccess(CommandMessages.WHITELIST_DISABLED, true)
    }

    @JvmStatic
    private fun showList(source: CommandSourceStack): Int {
        val names = getWhitelistNames(source.server)
        if (names.isEmpty()) {
            source.sendSuccess(CommandMessages.WHITELIST_NONE, false)
        } else {
            CommandMessages.WHITELIST_LIST.sendSuccess(source, names, false)
        }
        return names.size
    }

    @JvmStatic
    private fun getWhitelistNames(server: KryptonServer): Array<String> =
        server.playerManager.whitelistManager.profiles().map { it.name }.toArray { arrayOfNulls<String>(it) }

    @JvmStatic
    private fun kickUnlistedPlayers(server: KryptonServer) {
        server.playerManager.players().forEach {
            if (!server.playerManager.whitelistManager.isWhitelisted(it.profile)) it.disconnect(DisconnectMessages.NOT_WHITELISTED)
        }
    }

    @JvmStatic
    private fun reloadWhitelist(server: KryptonServer) {
        try {
            server.playerManager.whitelistManager.load()
        } catch (exception: IOException) {
            LOGGER.error("Failed to reload whitelist!", exception)
        }
    }
}
