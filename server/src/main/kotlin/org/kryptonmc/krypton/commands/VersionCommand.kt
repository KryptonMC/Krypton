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
import net.kyori.adventure.text.Component.newline
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.CommandExceptions
import org.kryptonmc.krypton.command.InternalCommand
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object VersionCommand : InternalCommand {

    private val KRYPTON_COLOR = TextColor.color(128, 0, 255)
    private val OPTION_COLOR = TextColor.color(255, 251, 33)
    private val VALUE_COLOR = TextColor.color(0, 196, 244)

    override fun register(dispatcher: CommandDispatcher<Sender>) {
        val node = dispatcher.register(literal<Sender>("version")
            .executes {
                val sender = it.source as? KryptonPlayer ?: throw CommandExceptions.MUST_BE_PLAYER.create()
                val version = sender.server.platform.version
                val minecraftVersion = sender.server.platform.minecraftVersion
                val pluginsLoaded = sender.server.pluginManager.plugins.size
                val text = text("Krypton", KRYPTON_COLOR)
                    .clickEvent(ClickEvent.openUrl("https://github.com/KryptonMC/Krypton"))
                    .append(newline())
                    .append(column("Version: ", version))
                    .append(column("Minecraft Version: ", minecraftVersion))
                    .append(text("Plugins Loaded: ", OPTION_COLOR))
                    .append(text(pluginsLoaded, VALUE_COLOR))
                sender.sendMessage(text)
                1
            })
        dispatcher.register(literal<Sender>("about").redirect(node))
    }

    private fun column(option: String, value: String) = text(option, OPTION_COLOR)
        .append(text(value, VALUE_COLOR))
        .append(newline())
}
