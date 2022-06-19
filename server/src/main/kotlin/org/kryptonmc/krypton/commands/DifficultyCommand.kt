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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object DifficultyCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val command = literal<Sender>("difficulty") {
            permission(KryptonPermission.DIFFICULTY)
            runs {
                val sender = it.source as? KryptonPlayer ?: return@runs
                sender.sendMessage(Component.translatable("commands.difficulty.query", sender.world.difficulty.translation))
            }
        }
        Difficulty.values().forEach { difficulty ->
            command.then(literal(difficulty.serialized) {
                runs {
                    val sender = it.source as? KryptonPlayer ?: return@runs
                    if (sender.world.difficulty == difficulty) {
                        sender.sendMessage(Component.translatable("commands.difficulty.failure", difficulty.translation))
                        return@runs
                    }
                    sender.world.difficulty = difficulty
                    sender.sendMessage(Component.translatable("commands.difficulty.success", difficulty.translation))
                }
            })
        }
        dispatcher.register(command)
    }
}
