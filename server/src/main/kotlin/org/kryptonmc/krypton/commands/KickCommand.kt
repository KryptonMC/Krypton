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

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal

object KickCommand : InternalCommand {

    private val KICKED_MESSAGE = Component.translatable("multiplayer.disconnect.kicked")

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("kick") {
            permission(KryptonPermission.KICK)
            argument("targets", EntityArgument.players()) {
                executes { context ->
                    context.entityArgument("targets").players(context.source).forEach {
                        it.disconnect(KICKED_MESSAGE)
                    }
                    Command.SINGLE_SUCCESS
                }
                argument("reason", StringArgumentType.string()) {
                    executes { context ->
                        val reason = context.argument<String>("reason")
                        context.entityArgument("targets").players(context.source).forEach {
                            it.disconnect(KICKED_MESSAGE.append(Component.text(" Reason: $reason")))
                        }
                        Command.SINGLE_SUCCESS
                    }
                }
            }
        })
    }
}
