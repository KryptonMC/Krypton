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
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.krypton.world.rule.WorldGameRules

object GameRuleCommand {

    private const val VALUE = "value"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literal("gamerule") { requiresPermission(KryptonPermission.GAME_RULE) }
        GameRuleKeys.visitTypes(object : WorldGameRules.TypeVisitor {
            override fun <T : WorldGameRules.Value<T>> visit(key: WorldGameRules.Key<T>, type: WorldGameRules.Type<T>) {
                command.then(literal(key.id) {
                    executes { queryRule(it.source, key) }
                    then(type.createArgument(VALUE).executes { setRule(it, key) })
                })
            }
        })
        dispatcher.register(command)
    }

    @JvmStatic
    private fun <T : WorldGameRules.Value<T>> setRule(context: CommandContext<CommandSourceStack>, key: WorldGameRules.Key<T>): Int {
        val value = context.source.server.worldManager.default.gameRules().getRule(key)
        value.setFromArgument(context, VALUE)
        CommandMessages.GAME_RULE_SET.sendSuccess(context.source, key.id, value.toString(), true)
        return value.commandResult()
    }

    @JvmStatic
    private fun <T : WorldGameRules.Value<T>> queryRule(source: CommandSourceStack, key: WorldGameRules.Key<T>): Int {
        val value = source.server.worldManager.default.gameRules().getRule(key)
        CommandMessages.GAME_RULE_QUERY.sendSuccess(source, key.id, value.toString(), false)
        return value.commandResult()
    }
}
