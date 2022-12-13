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
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.locale.Messages
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
        dispatcher.register(literal("whitelist") {
            permission(KryptonPermission.WHITELIST)
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
                        val values = playerManager.players.stream()
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
        source.sendSuccess(Messages.Commands.WHITELIST_RELOADED.build(), true)
        kickUnlistedPlayers(source.server)
    }

    @JvmStatic
    private fun addPlayers(source: CommandSourceStack, profiles: Collection<GameProfile>): Int {
        val whitelistManager = source.server.playerManager.whitelistManager

        var count = 0
        profiles.forEach {
            if (whitelistManager.isWhitelisted(it)) return@forEach
            whitelistManager.add(it)
            source.sendSuccess(Messages.Commands.WHITELIST_ADD_SUCCESS.build(it), true)
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
            whitelistManager.remove(it)
            source.sendSuccess(Messages.Commands.WHITELIST_REMOVE_SUCCESS.build(it), true)
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
        source.sendSuccess(Messages.Commands.WHITELIST_ENABLED.build(), true)
        kickUnlistedPlayers(source.server)
    }

    @JvmStatic
    private fun disableWhitelist(source: CommandSourceStack) {
        val whitelistManager = source.server.playerManager.whitelistManager
        if (!whitelistManager.isEnabled()) throw ERROR_ALREADY_DISABLED.create()
        whitelistManager.disable()
        source.sendSuccess(Messages.Commands.WHITELIST_DISABLED.build(), true)
    }

    @JvmStatic
    private fun showList(source: CommandSourceStack): Int {
        val names = getWhitelistNames(source.server)
        if (names.isEmpty()) {
            source.sendSuccess(Messages.Commands.WHITELIST_NONE.build(), false)
        } else {
            source.sendSuccess(Messages.Commands.WHITELIST_LIST.build(names), false)
        }
        return names.size
    }

    @JvmStatic
    private fun getWhitelistNames(server: KryptonServer): Array<String> =
        server.playerManager.whitelistManager.profiles().stream().map { it.name }.toArray { arrayOfNulls<String>(it) }

    @JvmStatic
    private fun kickUnlistedPlayers(server: KryptonServer) {
        server.playerManager.players.forEach {
            if (!server.playerManager.whitelistManager.isWhitelisted(it.profile)) it.disconnect(Messages.Disconnect.NOT_WHITELISTED.build())
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
