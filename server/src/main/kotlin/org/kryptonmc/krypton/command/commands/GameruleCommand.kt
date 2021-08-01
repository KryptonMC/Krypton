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
package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.argument
import org.kryptonmc.krypton.util.toComponent

internal class GameruleCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal<Sender>("gamerule")
        for ((_, rule) in GameRules.rules) {
            val gameRule = command.then(literal<Sender>(rule.name))
                .executes {
                    val sender = it.source as? KryptonPlayer ?: return@executes 1
                    sender.sendMessage(
                        translatable(
                            "commands.gamerule.query",
                            listOf(rule.name.toComponent(), sender.world.gameRules[rule].toString().toComponent())
                        )
                    )
                    1
                }
            if (rule.default is Boolean) {
                gameRule.then(argument<Sender, Boolean>("value", BoolArgumentType.bool()))
                    .executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                        val value = it.argument<Boolean>("value")
                        sender.world.gameRules[rule] = value
                        sender.sendMessage(
                            translatable(
                                "commands.gamerule.set",
                                listOf(rule.name.toComponent(), value.toString().toComponent())
                            )
                        )
                        1
                    }
            } else if (rule.default is Int) {
                gameRule.then(argument<Sender, Int>("value", IntegerArgumentType.integer()))
                    .executes {
                        val sender = it.source as? KryptonPlayer ?: return@executes 1
                        val value = it.argument<Int>("value")
                        sender.world.gameRules[rule] = value
                        sender.sendMessage(
                            translatable(
                                "commands.gamerule.set",
                                listOf(rule.name.toComponent(), value.toString().toComponent())
                            )
                        )
                        1
                    }
            }
        }
    }
}
