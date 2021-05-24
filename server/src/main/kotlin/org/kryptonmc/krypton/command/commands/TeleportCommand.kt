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
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.krypton.command.BrigadierCommand
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates

object TeleportCommand : BrigadierCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal<Sender>("teleport")
            .then(argument<Sender, Coordinates>("location", VectorArgument(true))
                .executes { teleport(it.source, it.getArgument("location", Coordinates::class.java)); 1 }))
        dispatcher.register(literal<Sender>("tp").redirect(node))
    }

    private fun teleport(sender: Sender, location: Coordinates) {
        if (sender !is Player) return
        sender.teleport(location.position(sender))
    }
}
