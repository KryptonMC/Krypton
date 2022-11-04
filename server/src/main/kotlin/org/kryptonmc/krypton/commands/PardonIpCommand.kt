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
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.toMessage
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.command.runs

object PardonIpCommand : InternalCommand {

    private val INVALID_IP_EXCEPTION = SimpleCommandExceptionType(Component.translatable("commands.pardonip.invalid").toMessage())
    private val ALREADY_UNBANNED_EXCEPTION = SimpleCommandExceptionType(Component.translatable("commands.pardonip.failed").toMessage())

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("pardon-ip") {
            permission(KryptonPermission.PARDON_IP)
            argument("target", StringArgumentType.string()) {
                suggests { context, builder ->
                    val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                    server.playerManager.bannedIps.forEach {
                        if (!builder.remainingLowerCase.matchesSubString(it.key.lowercase())) return@forEach
                        builder.suggest(it.key)
                    }
                    builder.buildFuture()
                }
                runs {
                    val server = it.source.server as? KryptonServer ?: return@runs
                    val target = it.argument<String>("target")
                    if (target.matches(BanIpCommand.IP_ADDRESS_PATTERN)) {
                        if (server.playerManager.bannedIps.contains(target)) {
                            server.playerManager.bannedIps.remove(target)
                            return@runs
                        }
                        throw ALREADY_UNBANNED_EXCEPTION.create()
                    }
                    throw INVALID_IP_EXCEPTION.create()
                }
            }
        })
    }
}
