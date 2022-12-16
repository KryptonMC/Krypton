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
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.locale.Messages

object PardonIpCommand {

    private val ERROR_INVALID_IP = CommandExceptions.simple("commands.pardonip.invalid")
    private val ERROR_ALREADY_UNBANNED = CommandExceptions.simple("commands.pardonip.failed")

    private const val TARGET = "target"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("pardon-ip") {
            requiresPermission(KryptonPermission.PARDON_IP)
            argument(TARGET, StringArgumentType.string()) {
                suggests { context, builder ->
                    CommandSuggestionProvider.suggest(context.source.server.playerManager.banManager.ipStrings(), builder)
                }
                runs {
                    val banManager = it.source.server.playerManager.banManager
                    val target = it.getArgument(TARGET, String::class.java)
                    if (!BanIpCommand.IP_ADDRESS_PATTERN.matcher(target).matches()) throw ERROR_INVALID_IP.create()
                    if (!banManager.isBanned(target)) throw ERROR_ALREADY_UNBANNED.create()
                    banManager.remove(target)
                    Messages.Commands.PARDON_IP_SUCCESS.send(it.source, target)
                }
            }
        })
    }
}
