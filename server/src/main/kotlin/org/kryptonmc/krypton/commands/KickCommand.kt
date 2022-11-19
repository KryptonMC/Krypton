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
import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object KickCommand {

    private val KICKED_MESSAGE = Messages.Disconnect.KICKED.build()

    private const val TARGETS = "targets"
    private const val REASON = "reason"

    @JvmStatic
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal("kick") {
            permission(KryptonPermission.KICK)
            argument(TARGETS, EntityArgumentType.players()) {
                runs { context -> context.entityArgument(TARGETS).players(context.source).forEach { it.disconnect(KICKED_MESSAGE) } }
                argument(REASON, StringArgumentType.string()) {
                    runs { context ->
                        val reason = context.argument<String>(REASON)
                        context.entityArgument(TARGETS).players(context.source).forEach {
                            it.disconnect(KICKED_MESSAGE.append(Component.text(" Reason: $reason")))
                        }
                    }
                }
            }
        })
    }
}
