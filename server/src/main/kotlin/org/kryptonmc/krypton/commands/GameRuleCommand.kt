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

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal

object GameRuleCommand : InternalCommand {

    @Suppress("UNCHECKED_CAST")
    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal<Sender>("gamerule") {
            permission(KryptonPermission.GAME_RULE)
        }
        Registries.GAME_RULES.values.forEach { rule ->
            val gameRule = LiteralArgumentBuilder.literal<Sender>(rule.name).executes {
                val sender = it.source as? KryptonPlayer ?: return@executes 0
                sender.sendMessage(Component.translatable(
                    "commands.gamerule.query",
                    Component.text(rule.name),
                    Component.text(sender.world.gameRules[rule].toString())
                ))
                Command.SINGLE_SUCCESS
            }
            if (rule.default is Boolean) {
                gameRule.then(argument("value", BoolArgumentType.bool()) {
                    executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 0
                        val value = it.argument<Boolean>("value")
                        sender.world.gameRules[rule] = value
                        sender.sendMessage(Component.translatable(
                            "commands.gamerule.set",
                            Component.text(rule.name),
                            Component.text(value.toString())
                        ))
                        Command.SINGLE_SUCCESS
                    }
                })
            } else if (rule.default is Int) {
                gameRule.then(argument<Sender, Int>("value", IntegerArgumentType.integer()) {
                    executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 0
                        val value = it.argument<Int>("value")
                        sender.world.gameRules[rule] = value
                        sender.sendMessage(Component.translatable(
                            "commands.gamerule.set",
                            Component.text(rule.name),
                            Component.text(value.toString())
                        ))
                        Command.SINGLE_SUCCESS
                    }
                })
            }
            command.then(gameRule)
        }
        dispatcher.register(command)
    }
}
