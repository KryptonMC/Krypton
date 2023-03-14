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
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType

object MessageCommand {

    private const val PLAYER = "player"
    private const val MESSAGE = "message"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val messageCommand = dispatcher.register(literalCommand("msg") {
            requiresPermission(KryptonPermission.MESSAGE)
            argument(PLAYER, EntityArgumentType.players()) {
                argument(MESSAGE, StringArgumentType.string()) {
                    runs {
                        // TODO: Update when new chat changes are implemented
                        val player = EntityArgumentType.getPlayers(it, PLAYER).get(0)
                        val message = Component.text(it.getArgument(MESSAGE, String::class.java))
                        it.source.sendSystemMessage(Component.translatable("commands.message.display.outgoing", player.displayName, message))
                        player.sendSystemMessage(Component.translatable("commands.message.display.incoming", it.source.displayName, message))
                    }
                }
            }
        })

        dispatcher.register(LiteralArgumentBuilder.literal<CommandSourceStack>("tell").redirect(messageCommand))
        dispatcher.register(LiteralArgumentBuilder.literal<CommandSourceStack>("w").redirect(messageCommand))
    }
}
