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
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.locale.CommandMessages

object TeleportCommand {

    private const val LOCATION = "location"
    private const val PLAYERS = "players"
    private const val TARGET = "target"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val node = dispatcher.register(literalCommand("teleport") {
            requiresPermission(KryptonPermission.TELEPORT)
            argument(LOCATION, VectorArgument.normal()) {
                runs { it.source.getPlayerOrError().teleport(VectorArgument.get(it, LOCATION)) }
            }
            argument(PLAYERS, EntityArgumentType.players()) {
                runs {
                    val player = it.source.getPlayerOrError()
                    val targets = EntityArgumentType.getPlayers(it, PLAYERS)
                    if (targets.size == 1) {
                        val target = targets.get(0)
                        player.teleport(target.position)
                        CommandMessages.TELEPORT_SINGLE.sendSuccess(it.source, it.source.displayName, target.displayName, true)
                    }
                }
                argument(TARGET, EntityArgumentType.players()) {
                    runs { context ->
                        val players = EntityArgumentType.getPlayers(context, PLAYERS)
                        val target = EntityArgumentType.getPlayers(context, TARGET).get(0)
                        players.forEach { it.teleport(target.position) }
                        CommandMessages.TELEPORT_ENTITY_MULTIPLE.sendSuccess(context.source, players.size, target.displayName, true)
                    }
                }
                argument(LOCATION, VectorArgument.normal()) {
                    runs { context ->
                        val players = EntityArgumentType.getPlayers(context, PLAYERS)
                        val location = VectorArgument.get(context, LOCATION)
                        players.forEach { it.teleport(location) }
                        CommandMessages.TELEPORT_LOCATION_MULTIPLE.sendSuccess(context.source, players.size, location, true)
                    }
                }
            }
        })
        dispatcher.register(LiteralArgumentBuilder.literal<CommandSourceStack>("tp").redirect(node))
    }
}
