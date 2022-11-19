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
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.registry.KryptonRegistries

object GameRuleCommand {

    private const val VALUE = "value"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literal("gamerule") { permission(KryptonPermission.GAME_RULE) }
        KryptonRegistries.GAME_RULES.forEach { rule ->
            val gameRule = literal(rule.name) {
                runs { it.source.sendSuccess(Messages.Commands.GAMERULE_QUERY.build(rule.name, it.source.world.gameRules.get(rule)!!), false) }
            }
            @Suppress("UNCHECKED_CAST")
            if (rule.defaultValue is Boolean) {
                gameRule.then(gameRuleArgument(BoolArgumentType.bool(), rule as GameRule<Boolean>))
            } else if (rule.defaultValue is Int) {
                gameRule.then(gameRuleArgument(IntegerArgumentType.integer(), rule as GameRule<Int>))
            }
            command.then(gameRule)
        }
        dispatcher.register(command)
    }

    @JvmStatic
    private inline fun <reified V : Any> gameRuleArgument(
        argument: ArgumentType<V>,
        rule: GameRule<V>
    ): ArgumentBuilder<CommandSourceStack, *> = argument(VALUE, argument) {
        runs {
            val value = it.argument<V>(VALUE)
            it.source.world.gameRules.set(rule, value)
            it.source.sendSuccess(Messages.Commands.GAMERULE_SET.build(rule.name, value), true)
        }
    }
}
