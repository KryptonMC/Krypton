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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object PardonIpCommand : InternalCommand {

    private val ERROR_INVALID_IP = CommandExceptions.simple("commands.pardonip.invalid")
    private val ERROR_ALREADY_UNBANNED = CommandExceptions.simple("commands.pardonip.failed")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("pardon-ip") {
            permission(KryptonPermission.PARDON_IP)
            argument("target", StringArgumentType.string()) {
                suggests { context, builder ->
                    val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                    server.playerManager.banManager.ips().forEach {
                        if (builder.remainingLowerCase.matchesSubString(it.ip.lowercase())) builder.suggest(it.ip)
                    }
                    builder.buildFuture()
                }
                runs {
                    val server = it.source.server as? KryptonServer ?: return@runs
                    val banManager = server.playerManager.banManager
                    val target = it.argument<String>("target")
                    if (!BanIpCommand.IP_ADDRESS_PATTERN.matcher(target).matches()) throw ERROR_INVALID_IP.create()
                    if (!banManager.isBanned(target)) throw ERROR_ALREADY_UNBANNED.create()
                    banManager.remove(target)
                    Messages.Commands.PARDON_IP_SUCCESS.send(it.source, target)
                }
            }
        })
    }
}
