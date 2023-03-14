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
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.locale.CommandMessages

object DifficultyCommand {

    private val ERROR_ALREADY_DIFFICULT = CommandExceptions.dynamic("commands.difficulty.failure")

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val command = literalCommand<CommandSourceStack>("difficulty") {
            requiresPermission(KryptonPermission.DIFFICULTY)
            runs { CommandMessages.DIFFICULTY_QUERY.sendSuccess(it.source, it.source.world.difficulty, false) }
        }
        Difficulty.values().forEach { difficulty ->
            command.then(literalCommand(difficulty.name.lowercase()) {
                runs {
                    if (it.source.world.difficulty == difficulty) throw ERROR_ALREADY_DIFFICULT.create(difficulty.name.lowercase())
                    it.source.world.difficulty = difficulty
                    CommandMessages.DIFFICULTY_SUCCESS.sendSuccess(it.source, difficulty, true)
                }
            })
        }
        dispatcher.register(command)
    }
}
