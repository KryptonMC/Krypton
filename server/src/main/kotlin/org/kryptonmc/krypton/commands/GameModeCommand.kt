/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.kryptonmc.krypton.world.rule.GameRuleKeys

object GameModeCommand {

    private val ERROR_INVALID_GAME_MODE = DynamicCommandExceptionType { KryptonAdventure.asMessage(Component.text("Invalid game mode $it!")) }

    private const val GAME_MODE = "gameMode"
    private const val TARGETS = "targets"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literalCommand("gamemode") { requiresPermission(KryptonPermission.GAME_MODE) }
        GameModes.VALUES.forEach { mode ->
            command.then(literalCommand(mode.name.lowercase()) {
                runs { setMode(it.source, listOf(it.source.getPlayerOrError()), mode) }
                argument(TARGETS, EntityArgumentType.players()) {
                    runs { setMode(it.source, EntityArgumentType.getPlayers(it, TARGETS), mode) }
                }
            })
        }

        command.argument(GAME_MODE, StringArgumentType.string()) {
            runs { setMode(it.source, listOf(it.source.getPlayerOrError()), getGameMode(it)) }
            argument(TARGETS, EntityArgumentType.players()) {
                runs { setMode(it.source, EntityArgumentType.getPlayers(it, TARGETS), getGameMode(it)) }
            }
        }
        dispatcher.register(command)
    }

    @JvmStatic
    private fun getGameMode(context: CommandContext<CommandSourceStack>): GameMode {
        val argument = context.getArgument(GAME_MODE, String::class.java)
        return try {
            GameModes.fromId(argument.toInt()) ?: throw ERROR_INVALID_GAME_MODE.create(argument)
        } catch (_: NumberFormatException) {
            throw ERROR_INVALID_GAME_MODE.create(argument)
        }
    }

    @JvmStatic
    private fun setMode(source: CommandSourceStack, targets: List<KryptonPlayer>, mode: GameMode): Int {
        var count = 0
        targets.forEach {
            it.updateGameMode(mode)
            notifyGameModeChanged(source, it, mode)
            ++count
        }
        return count
    }

    @JvmStatic
    private fun notifyGameModeChanged(source: CommandSourceStack, player: KryptonPlayer, mode: GameMode) {
        if (source.entity === player) {
            CommandMessages.GAME_MODE_SELF.sendSuccess(source, mode, true)
            return
        }
        if (source.world.gameRules().getBoolean(GameRuleKeys.SEND_COMMAND_FEEDBACK)) {
            source.sendSystemMessage(Component.translatable("gameMode.changed", Component.translatable(mode)))
        }
        CommandMessages.GAME_MODE_OTHER.sendSuccess(source, player.displayName, mode, true)
    }
}
