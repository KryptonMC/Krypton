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
package org.kryptonmc.krypton.command.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.gameprofile.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.gameprofile.gameProfileArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.util.toComponent

internal class OpCommand : InternalCommand {

    private val ALREADY_OPPED_EXCEPTION = SimpleCommandExceptionType(translatable("commands.op.failed").toMessage())


    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(
            literal<Sender>("op")
                .permission("krypton.command.op", PermissionLevel.LEVEL_3)
                .then(argument<Sender, EntityQuery>("targets", GameProfileArgument.gameProfile())
                    .executes {
                        val targets = it.gameProfileArgument("targets").getProfiles(it.source)
                        val server = it.source.server as KryptonServer
                        for (target in targets) {
                            val ops = server.playerManager.ops
                            if (!ops.contains(target)) {
                                server.playerManager.addToOperators(target)
                                it.source.sendMessage(
                                    translatable(
                                        "commands.op.success",
                                        listOf(target.name.toComponent())
                                    )
                                )
                            } else {
                                throw ALREADY_OPPED_EXCEPTION.create()
                            }
                        }
                        1
                    })
        )
    }
}
