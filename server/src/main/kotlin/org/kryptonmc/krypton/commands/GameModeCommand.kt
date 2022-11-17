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
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.GameModes

object GameModeCommand : InternalCommand {

    private const val GAME_MODE_ARGUMENT = "gameMode"
    private const val TARGETS_ARGUMENT = "targets"

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal("gamemode") { permission(KryptonPermission.GAME_MODE) }
        GameModes.VALUES.forEach { mode ->
            command.then(literal(mode.name.lowercase()) {
                runs { gameModeArgument(it, mode) }
                argument(TARGETS_ARGUMENT, EntityArgumentType.players()) {
                    runs { targetArgument(it, mode) }
                }
            })
        }

        command.then(argument(GAME_MODE_ARGUMENT, StringArgumentType.string()) {
            runs {
                var gameMode = GameModes.fromAbbreviation(it.argument(GAME_MODE_ARGUMENT))
                if (gameMode == null) {
                    val id = it.argument<String>(GAME_MODE_ARGUMENT).toIntOrNull() ?: return@runs
                    gameMode = GameModes.fromId(id)
                }
                if (gameMode == null) return@runs
                gameModeArgument(it, gameMode)
            }
            argument(TARGETS_ARGUMENT, EntityArgumentType.players()) {
                runs {
                    var gameMode = GameModes.fromAbbreviation(it.argument(GAME_MODE_ARGUMENT))
                    if (gameMode == null) {
                        val id = it.argument<String>(GAME_MODE_ARGUMENT).toIntOrNull() ?: return@runs
                        gameMode = GameModes.fromId(id)
                    }
                    if (gameMode == null) return@runs
                    gameModeArgument(it, gameMode!!)
                }
            }
        })
        dispatcher.register(command)
    }

    @JvmStatic
    private fun gameModeArgument(context: CommandContext<Sender>, gameMode: GameMode) {
        val sender = context.source as? KryptonPlayer ?: return
        updateGameMode(listOf(sender), gameMode, sender)
    }

    @JvmStatic
    private fun targetArgument(context: CommandContext<Sender>, gameMode: GameMode) {
        updateGameMode(context.entityArgument(TARGETS_ARGUMENT).players(context.source), gameMode, context.source)
    }

    @JvmStatic
    private fun updateGameMode(entities: List<KryptonPlayer>, mode: GameMode, sender: Sender) {
        entities.forEach {
            it.updateGameMode(mode, ChangeGameModeEvent.Cause.COMMAND)
            if (sender === it) {
                Messages.GAME_MODE_CHANGED.send(sender, mode)
                return@forEach
            }
            Messages.Commands.GAME_MODE_SUCCESS.send(sender, it.displayName, mode)
        }
    }
}
