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
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object PardonCommand {

    private const val TARGETS = "targets"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("pardon") {
            permission(KryptonPermission.PARDON)
            argument(TARGETS, GameProfileArgument) {
                suggests { context, builder ->
                    context.source.server.playerManager.banManager.profiles().forEach {
                        if (builder.remainingLowerCase.matchesSubString(it.profile.name.lowercase())) builder.suggest(it.profile.name)
                    }
                    builder.buildFuture()
                }
                runs { context ->
                    val banManager = context.source.server.playerManager.banManager
                    context.gameProfileArgument(TARGETS).profiles(context.source).forEach {
                        if (!banManager.isBanned(it)) return@forEach
                        banManager.remove(it)
                        Messages.Commands.PARDON_SUCCESS.send(context.source, it.name)
                    }
                }
            }
        })
    }
}
