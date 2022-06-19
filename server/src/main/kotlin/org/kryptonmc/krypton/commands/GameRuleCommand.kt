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
import org.kryptonmc.krypton.command.runs

object GameRuleCommand : InternalCommand {

    @Suppress("UNCHECKED_CAST")
    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal("gamerule") { permission(KryptonPermission.GAME_RULE) }
        Registries.GAME_RULES.values.forEach { rule ->
            val gameRule = LiteralArgumentBuilder.literal<Sender>(rule.name).runs {
                val sender = it.source as? KryptonPlayer ?: return@runs
                val gameRule = Component.text(sender.world.gameRules[rule].toString())
                sender.sendMessage(Component.translatable("commands.gamerule.query", Component.text(rule.name), gameRule))
            }
            if (rule.default is Boolean) {
                gameRule.then(argument("value", BoolArgumentType.bool()) {
                    runs {
                        val sender = it.source as? KryptonPlayer ?: return@runs
                        val value = it.argument<Boolean>("value")
                        sender.world.gameRules[rule] = value
                        val name = Component.text(rule.name)
                        sender.sendMessage(Component.translatable("commands.gamerule.set", name, Component.text(value.toString())))
                    }
                })
            } else if (rule.default is Int) {
                gameRule.then(argument<Sender, Int>("value", IntegerArgumentType.integer()) {
                    runs {
                        val sender = it.source as? KryptonPlayer ?: return@runs
                        val value = it.argument<Int>("value")
                        sender.world.gameRules[rule] = value
                        val name = Component.text(rule.name)
                        sender.sendMessage(Component.translatable("commands.gamerule.set", name, Component.text(value.toString())))
                    }
                })
            }
            command.then(gameRule)
        }
        dispatcher.register(command)
    }
}
