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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.command.suggest
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.logger
import java.io.IOException

object WhitelistCommand : InternalCommand {

    private val LOGGER = logger<WhitelistCommand>()
    private val ERROR_ALREADY_ENABLED = CommandExceptions.simple("commands.whitelist.alreadyOn")
    private val ERROR_ALREADY_DISABLED = CommandExceptions.simple("commands.whitelist.alreadyOff")
    private val ERROR_ALREADY_WHITELISTED = CommandExceptions.simple("commands.whitelist.add.failed")
    private val ERROR_NOT_WHITELISTED = CommandExceptions.simple("commands.whitelist.remove.failed")

    private const val TARGETS_ARGUMENT = "targets"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
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
                argument(TARGETS_ARGUMENT, GameProfileArgument) {
                    suggests { context, builder ->
                        val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                        builder.suggest(server.playerManager.players.stream()
                            .filter { !server.playerManager.whitelistManager.isWhitelisted(it.profile) }
                            .map { it.profile.name })
                    }
                    executes { addPlayers(it.source, it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source)) }
                }
            }
            literal("remove") {
                argument(TARGETS_ARGUMENT, GameProfileArgument) {
                    suggests { context, builder ->
                        val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                        builder.suggest(getWhitelistNames(server))
                    }
                    executes { removePlayers(it.source, it.gameProfileArgument(TARGETS_ARGUMENT).profiles(it.source)) }
                }
            }
            literal("reload") {
                runs { reload(it.source) }
            }
        })
    }

    @JvmStatic
    private fun reload(sender: Sender) {
        val server = sender.server as? KryptonServer ?: return
        reloadWhitelist(server)
        Messages.Commands.WHITELIST_RELOADED.send(sender)
        kickUnlistedPlayers(server)
    }

    @JvmStatic
    private fun addPlayers(sender: Sender, profiles: Collection<GameProfile>): Int {
        val server = sender.server as? KryptonServer ?: return 0
        val whitelistManager = server.playerManager.whitelistManager

        var count = 0
        profiles.forEach {
            if (whitelistManager.isWhitelisted(it)) return@forEach
            whitelistManager.add(it)
            Messages.Commands.WHITELIST_ADD_SUCCESS.send(sender, it)
            ++count
        }
        if (count == 0) throw ERROR_ALREADY_WHITELISTED.create()
        return count
    }

    @JvmStatic
    private fun removePlayers(sender: Sender, profiles: Collection<GameProfile>): Int {
        val server = sender.server as? KryptonServer ?: return 0
        val whitelistManager = server.playerManager.whitelistManager

        var count = 0
        profiles.forEach {
            if (!whitelistManager.isWhitelisted(it)) return@forEach
            whitelistManager.remove(it)
            Messages.Commands.WHITELIST_REMOVE_SUCCESS.send(sender, it)
            ++count
        }

        if (count == 0) throw ERROR_NOT_WHITELISTED.create()
        kickUnlistedPlayers(server)
        return count
    }

    @JvmStatic
    private fun enableWhitelist(sender: Sender) {
        val server = sender.server as? KryptonServer ?: return
        val whitelistManager = server.playerManager.whitelistManager
        if (whitelistManager.isEnabled) throw ERROR_ALREADY_ENABLED.create()
        whitelistManager.isEnabled = true
        Messages.Commands.WHITELIST_ENABLED.send(sender)
        kickUnlistedPlayers(server)
    }

    @JvmStatic
    private fun disableWhitelist(sender: Sender) {
        val server = sender.server as? KryptonServer ?: return
        val whitelistManager = server.playerManager.whitelistManager
        if (!whitelistManager.isEnabled) throw ERROR_ALREADY_DISABLED.create()
        whitelistManager.isEnabled = false
        Messages.Commands.WHITELIST_DISABLED.send(sender)
    }

    @JvmStatic
    private fun showList(sender: Sender): Int {
        val server = sender.server as? KryptonServer ?: return 0
        val names = getWhitelistNames(server)
        if (names.isEmpty()) Messages.Commands.WHITELIST_NONE.send(sender) else Messages.Commands.WHITELIST_LIST.send(sender, names)
        return names.size
    }

    @JvmStatic
    private fun getWhitelistNames(server: KryptonServer): Array<String> =
        server.playerManager.whitelistManager.profiles().stream().map { it.name }.toArray { arrayOfNulls<String>(it) }

    @JvmStatic
    private fun kickUnlistedPlayers(server: KryptonServer) {
        val whitelistManager = server.playerManager.whitelistManager
        server.playerManager.players.forEach {
            if (!whitelistManager.isWhitelisted(it.profile)) it.disconnect(Messages.Disconnect.NOT_WHITELISTED.build())
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
