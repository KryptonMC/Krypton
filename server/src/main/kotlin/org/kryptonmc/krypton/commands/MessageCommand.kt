/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.buildCopy
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.command.argument.argument

object MessageCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val messageCommand = dispatcher.register(literal<Sender>("msg")
            .permission("krypton.command.msg", 1)
            .then(argument<Sender, EntityQuery>("player", EntityArgument.player())
                .then(argument<Sender, String>("message", string())
                    .executes {
                        val source = it.source as? KryptonPlayer ?: return@executes 0
                        val player = it.entityArgument("player").players(source)[0]
                        val message = it.argument<String>("message")
                        source.sendMessage(translatable("commands.message.display.outgoing", player.displayName, text(message)))
                        player.sendMessage(translatable("commands.message.display.incoming", source.displayName, text(message)))
                        1
                    })
            )
        )

        dispatcher.register(messageCommand.buildCopy("tell"))
        dispatcher.register(messageCommand.buildCopy("w"))
    }
}
