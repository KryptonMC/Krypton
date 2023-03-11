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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.locale.DisconnectMessages

object KickCommand {

    private const val TARGETS = "targets"
    private const val REASON = "reason"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literalCommand("kick") {
            requiresPermission(KryptonPermission.KICK)
            argument(TARGETS, EntityArgumentType.players()) {
                runs { context -> EntityArgumentType.getPlayers(context, TARGETS).forEach { it.disconnect(DisconnectMessages.KICKED) } }
                argument(REASON, StringArgumentType.string()) {
                    runs { context ->
                        val reason = context.getArgument(REASON, String::class.java)
                        EntityArgumentType.getPlayers(context, TARGETS).forEach {
                            it.disconnect(DisconnectMessages.KICKED.append(Component.text(" Reason: $reason")))
                        }
                    }
                }
            }
        })
    }
}
