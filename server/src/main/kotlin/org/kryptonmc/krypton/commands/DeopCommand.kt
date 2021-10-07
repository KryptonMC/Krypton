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
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameProfileArgument
import org.kryptonmc.krypton.command.matchesSubString
import org.kryptonmc.krypton.command.permission

object DeopCommand : InternalCommand {

    private val ALREADY_DEOPPED_EXCEPTION = SimpleCommandExceptionType(
        translatable("commands.deop.failed").toMessage()
    )

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("deop")
            .permission("krypton.command.deop", 3)
            .then(argument<Sender, EntityQuery>("targets", GameProfileArgument)
                .suggests { context, builder ->
                    val server = context.source.server as? KryptonServer ?: return@suggests builder.buildFuture()
                    server.playerManager.ops.forEach {
                        if (!builder.remainingLowerCase.matchesSubString(it.key.name.lowercase())) return@forEach
                        builder.suggest(it.key.name)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val targets = context.gameProfileArgument("targets").profiles(context.source)
                    val server = context.source.server as? KryptonServer ?: return@executes 0
                    targets.forEach {
                        val ops = server.playerManager.ops
                        if (!ops.contains(it)) throw ALREADY_DEOPPED_EXCEPTION.create()
                        server.playerManager.removeFromOperators(it)
                        context.source.sendMessage(translatable("commands.deop.success", text(it.name)))
                    }
                    1
                })
        )
    }
}
