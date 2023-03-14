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
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.locale.CommandMessages
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.krypton.world.rule.WorldGameRules

object GameRuleCommand {

    private const val VALUE = "value"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literalCommand("gamerule") { requiresPermission(KryptonPermission.GAME_RULE) }
        GameRuleKeys.visitTypes(object : WorldGameRules.TypeVisitor {
            override fun <T : WorldGameRules.Value<T>> visit(key: WorldGameRules.Key<T>, type: WorldGameRules.Type<T>) {
                command.then(literalCommand(key.id) {
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
