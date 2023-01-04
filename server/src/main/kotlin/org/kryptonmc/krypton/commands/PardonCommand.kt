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
import org.kryptonmc.krypton.command.CommandSuggestionProvider
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.locale.CommandMessages

object PardonCommand {

    private const val TARGETS = "targets"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("pardon") {
            requiresPermission(KryptonPermission.PARDON)
            argument(TARGETS, GameProfileArgument) {
                suggests { context, builder ->
                    CommandSuggestionProvider.suggest(context.source.server.playerManager.banManager.profileNames(), builder)
                }
                runs { context ->
                    val banManager = context.source.server.playerManager.banManager
                    GameProfileArgument.get(context, TARGETS).forEach {
                        if (!banManager.isBanned(it)) return@forEach
                        banManager.removeBan(it)
                        CommandMessages.PARDON.sendSuccess(context.source, it.name, true)
                    }
                }
            }
        })
    }
}
