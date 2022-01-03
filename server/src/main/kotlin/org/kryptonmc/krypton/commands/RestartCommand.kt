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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.permission

object RestartCommand : InternalCommand {

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        dispatcher.register(literal("restart") {
            permission(KryptonPermission.RESTART)
            executes {
                val server = it.source.server as? KryptonServer ?: return@executes 0
                it.source.sendMessage(Component.text("Attempting to restart Krypton..."))
                server.restart()
                Command.SINGLE_SUCCESS
            }
        })
    }
}
