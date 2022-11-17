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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages

object DifficultyCommand : InternalCommand {

    private val ERROR_ALREADY_DIFFICULT = CommandExceptions.dynamic("commands.difficulty.failure")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal("difficulty") {
            permission(KryptonPermission.DIFFICULTY)
            runs {
                val sender = it.source as? KryptonPlayer ?: return@runs
                Messages.Commands.DIFFICULTY_QUERY.send(sender, sender.world.difficulty)
            }
        }
        Difficulty.values().forEach { difficulty ->
            command.then(literal(difficulty.name.lowercase()) {
                runs {
                    val sender = it.source as? KryptonPlayer ?: return@runs
                    if (sender.world.difficulty == difficulty) throw ERROR_ALREADY_DIFFICULT.create(difficulty.name.lowercase())
                    sender.world.difficulty = difficulty
                    Messages.Commands.DIFFICULTY_SUCCESS.send(sender, difficulty)
                }
            })
        }
        dispatcher.register(command)
    }
}
