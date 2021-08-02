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
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.arguments.entities.EntityArgument
import org.kryptonmc.krypton.command.arguments.entities.EntityQuery
import org.kryptonmc.krypton.command.arguments.entities.entityArgument
import org.kryptonmc.krypton.command.permission
import org.kryptonmc.krypton.util.argument

object KickCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal<Sender>("kick")
            .permission("krypton.command.kick", PermissionLevel.LEVEL_3)
            .then(argument<Sender, EntityQuery>("targets", EntityArgument.players())
                .executes { context ->
                    context.entityArgument("targets").getPlayers(context.source).forEach { it.disconnect(translatable("multiplayer.disconnect.kicked")) }
                    1
                }.then(argument<Sender, String>("reason", string())
                    .executes { context ->
                        val reason = context.argument<String>("reason")
                        context.entityArgument("targets").getPlayers(context.source).forEach { it.disconnect(translatable("multiplayer.disconnect.kicked").append(text(" Reason: $reason"))) }
                        1
                    })
            )
        )
    }
}
