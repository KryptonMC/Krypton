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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.world.KryptonGameMode

object GameModeCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal<Sender>("gamemode").permission("krypton.command.gamemode", 2)
        for (gameMode in Registries.GAME_MODES.values) {
            command.then(literal(gameMode.key().value()))
                .executes { gameModeArgument(it, gameMode) }
                .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                    .executes { targetArgument(it, gameMode) })
        }

        command.then(argument<Sender, String>("gameMode", string())
            .executes {
                val gameMode = KryptonGameMode.fromAbbreviation(it.argument("gameMode"))
                    ?: Registries.GAME_MODES[it.argument<String>("gameMode").toIntOrNull() ?: return@executes 1]
                    ?: return@executes 1
                gameModeArgument(it, gameMode)
            })
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .executes {
                    val gameMode = KryptonGameMode.fromAbbreviation(it.argument("gameMode"))
                        ?: Registries.GAME_MODES[it.argument<String>("gameMode").toIntOrNull() ?: return@executes 1]
                        ?: return@executes 1
                    gameModeArgument(it, gameMode)
                })
        dispatcher.register(command)
    }

    private fun gameModeArgument(context: CommandContext<Sender>, gameMode: GameMode): Int {
        val sender = context.source as? KryptonPlayer ?: return 0
        updateGameMode(listOf(sender), gameMode, sender)
        return 1
    }

    @Suppress("UNCHECKED_CAST")
    private fun targetArgument(context: CommandContext<Sender>, gameMode: GameMode): Int {
        val sender = context.source as? KryptonPlayer ?: return 0
        val entities = context.entityArgument("targets").players(sender)
        updateGameMode(entities, gameMode, sender)
        return 1
    }

    private fun updateGameMode(entities: List<KryptonPlayer>, mode: GameMode, sender: KryptonPlayer) = entities.forEach {
        it.gameMode = mode
        if (sender == it) {
            sender.sendMessage(translatable("gameMode.changed", translatable("gameMode.${mode.key().value().lowercase()}")))
            return@forEach
        }
        sender.sendMessage(translatable(
            "commands.gamemode.success.other",
            it.displayName,
            translatable("gameMode.${mode.key().value().lowercase()}")
        ))
    }
}
