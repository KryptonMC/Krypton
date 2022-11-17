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
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType
import org.kryptonmc.krypton.command.arguments.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.locale.Messages

object MessageCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val messageCommand = dispatcher.register(literal("msg") {
            permission(KryptonPermission.MESSAGE)
            argument("player", EntityArgumentType.players()) {
                argument("message", StringArgumentType.string()) {
                    runs {
                        val source = it.source as? KryptonPlayer ?: return@runs
                        val player = it.entityArgument("player").players(source).get(0)
                        val message = Component.text(it.argument<String>("message"))
                        Messages.Commands.OUTGOING_MESSAGE.send(source, player.displayName, message)
                        Messages.Commands.INCOMING_MESSAGE.send(player, source.displayName, message)
                    }
                }
            }
        })

        dispatcher.register(LiteralArgumentBuilder.literal<Sender>("tell").redirect(messageCommand))
        dispatcher.register(LiteralArgumentBuilder.literal<Sender>("w").redirect(messageCommand))
    }
}
