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
package org.kryptonmc.krypton.commands.krypton

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.literal

object PluginsCommand : KryptonSubCommand {

    private val INDENT = Component.text("  - ", NamedTextColor.DARK_GRAY)
    private val PREFIX = Component.text("Plugins", KryptonColors.STANDARD_PURPLE, TextDecoration.BOLD)
    private val OPEN_BRACKET = Component.text(" (", NamedTextColor.DARK_GRAY)
    private val CLOSED_BRACKET = Component.text(")", NamedTextColor.DARK_GRAY)
    private val COLON = Component.text(": ", NamedTextColor.DARK_GRAY)
    private val SEPARATOR = Component.text(", ", KryptonColors.DARK_ORCHID)

    override val aliases: Sequence<String> = sequenceOf("pl")

    override fun register(): LiteralArgumentBuilder<Sender> = literal("plugins") {
        executes { context ->
            val sender = context.source
            val plugins = sender.server.pluginManager.plugins
            if (plugins.isEmpty()) {
                sender.sendMessage(Component.text("No plugins are currently running on this server.", NamedTextColor.RED))
                return@executes 1
            }

            var message = Component.empty()
            plugins.forEachIndexed { index, plugin ->
                val authors = plugin.description.authors.joinToString(", ")
                var pluginMessage = Component.empty()
                    .append(Component.text("ID: ", KryptonColors.DARK_ORCHID))
                    .append(Component.text(plugin.description.id, KryptonColors.VIVID_SKY_BLUE))
                    .append(Component.newline())
                    .append(Component.text("Name: ", KryptonColors.DARK_ORCHID))
                    .append(Component.text(plugin.description.name, KryptonColors.TURQUOISE))
                    .append(Component.newline())
                    .append(Component.text("Version: ", KryptonColors.DARK_ORCHID))
                    .append(Component.text(plugin.description.version, NamedTextColor.GREEN))
                    .append(Component.newline())
                    .append(Component.text("Description: ", KryptonColors.DARK_ORCHID))
                    .append(Component.text(plugin.description.description, NamedTextColor.GOLD))
                    .append(Component.newline())
                    .append(Component.text("Authors: ", KryptonColors.DARK_ORCHID))
                    .append(Component.text(authors, NamedTextColor.YELLOW))
                    .append(Component.newline())
                    .append(Component.text("Dependencies: ", KryptonColors.DARK_ORCHID))
                if (plugin.description.dependencies.isNotEmpty()) {
                    var dependencies = Component.empty()
                    plugin.description.dependencies.forEachIndexed { index, dependency ->
                        dependencies = dependencies
                            .append(INDENT)
                            .append(Component.text(dependency.id, NamedTextColor.GREEN))
                            .append(OPEN_BRACKET)
                            .append(Component.text("optional: ", KryptonColors.DARK_ORCHID))
                            .append(Component.text(dependency.isOptional, NamedTextColor.GREEN))
                            .append(CLOSED_BRACKET)
                        if (index < plugin.description.dependencies.size - 1) dependencies = dependencies.append(Component.newline())
                    }
                    pluginMessage = pluginMessage.append(Component.newline()).append(dependencies)
                } else {
                    pluginMessage = pluginMessage.append(Component.text("None", NamedTextColor.RED))
                }
                message = message
                    .append(Component.empty()
                        .append(Component.text(plugin.description.id, KryptonColors.LIGHTER_PURPLE))
                        .append(OPEN_BRACKET)
                        .append(Component.text(plugin.description.version, NamedTextColor.GREEN))
                        .append(CLOSED_BRACKET)
                        .hoverEvent(HoverEvent.showText(pluginMessage)))
                if (index < plugins.size - 1) message = message.append(SEPARATOR)
            }
            sender.sendMessage(Component.empty()
                .append(PREFIX)
                .append(OPEN_BRACKET)
                .append(Component.text(sender.server.pluginManager.plugins.size, NamedTextColor.GREEN))
                .append(CLOSED_BRACKET)
                .append(COLON)
                .append(message))
            1
        }
    }
}
