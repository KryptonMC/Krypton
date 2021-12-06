/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.world.KryptonGameMode

object GameModeCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal<Sender>("gamemode") {
            permission(KryptonPermission.GAME_MODE)
        }
        for (gameMode in Registries.GAME_MODES.values) {
            command.then(literal(gameMode.key().value()) {
                executes { gameModeArgument(it, gameMode) }
                argument("targets", EntityArgument.players()) {
                    executes { targetArgument(it, gameMode) }
                }
            })
        }

        command.then(argument("gameMode", StringArgumentType.string()) {
            executes {
                var gameMode = KryptonGameMode.fromAbbreviation(it.argument("gameMode"))
                if (gameMode == null) {
                    val id = it.argument<String>("gameMode").toIntOrNull() ?: return@executes 0
                    gameMode = Registries.GAME_MODES[id]
                }
                if (gameMode == null) return@executes 0
                gameModeArgument(it, gameMode)
            }
            argument("targets", EntityArgument.players()) {
                executes {
                    var gameMode = KryptonGameMode.fromAbbreviation(it.argument("gameMode"))
                    if (gameMode == null) {
                        val id = it.argument<String>("gameMode").toIntOrNull() ?: return@executes 0
                        gameMode = Registries.GAME_MODES[id]
                    }
                    if (gameMode == null) return@executes 0
                    gameModeArgument(it, gameMode!!)
                }
            }
        })
        dispatcher.register(command)
    }

    @JvmStatic
    private fun gameModeArgument(context: CommandContext<Sender>, gameMode: GameMode): Int {
        val sender = context.source as? KryptonPlayer ?: return 0
        updateGameMode(listOf(sender), gameMode, sender)
        return Command.SINGLE_SUCCESS
    }

    @Suppress("UNCHECKED_CAST")
    private fun targetArgument(context: CommandContext<Sender>, gameMode: GameMode): Int {
        val sender = context.source as? KryptonPlayer ?: return 0
        val entities = context.entityArgument("targets").players(sender)
        updateGameMode(entities, gameMode, sender)
        return Command.SINGLE_SUCCESS
    }

    @JvmStatic
    private fun updateGameMode(entities: List<KryptonPlayer>, mode: GameMode, sender: KryptonPlayer) {
        entities.forEach {
            it.gameMode = mode
            if (sender === it) {
                sender.sendMessage(Component.translatable(
                    "gameMode.changed",
                    Component.translatable("gameMode.${mode.key().value().lowercase()}")
                ))
                return@forEach
            }
            sender.sendMessage(Component.translatable(
                "commands.gamemode.success.other",
                it.displayName,
                Component.translatable("gameMode.${mode.key().value().lowercase()}")
            ))
        }
    }
}
