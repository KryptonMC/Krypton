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
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.commands.literal
import org.kryptonmc.krypton.commands.runs
import java.util.stream.Stream

object PluginsCommand : KryptonSubCommand {

    private val OPEN_BRACKET = Component.text(" (", NamedTextColor.DARK_GRAY)
    private val CLOSED_BRACKET = Component.text(")", NamedTextColor.DARK_GRAY)

    private val INDENT = Component.text("  - ", NamedTextColor.DARK_GRAY)
    private val PREFIX = Component.text("Plugins", KryptonColors.STANDARD_PURPLE, TextDecoration.BOLD)
    private val COLON = Component.text(": ", NamedTextColor.DARK_GRAY)
    private val SEPARATOR = Component.text(", ", KryptonColors.DARK_ORCHID)

    private val ID = Component.text("ID: ", KryptonColors.DARK_ORCHID)
    private val NAME = Component.text("Name: ", KryptonColors.DARK_ORCHID)
    private val VERSION = Component.text("Version: ", KryptonColors.DARK_ORCHID)
    private val DESCRIPTION = Component.text("Description: ", KryptonColors.DARK_ORCHID)
    private val AUTHORS = Component.text("Authors: ", KryptonColors.DARK_ORCHID)
    private val DEPENDENCIES = Component.text("Dependencies: ", KryptonColors.DARK_ORCHID)
    private val OPTIONAL = Component.text("optional: ", KryptonColors.DARK_ORCHID)

    override fun aliases(): Stream<String> = Stream.of("pl")

    override fun register(): LiteralArgumentBuilder<CommandSourceStack> = literal("plugins") {
        runs { context ->
            val plugins = context.source.server.pluginManager.plugins
            if (plugins.isEmpty()) {
                context.source.sendSystemMessage(Component.text("No plugins are currently running on this server.", NamedTextColor.RED))
                return@runs
            }

            val message = foldToMessage(plugins) { pluginIndex, builder, plugin ->
                val pluginMessage = Component.text()
                    .append(ID)
                    .append(Component.text(plugin.description.id, KryptonColors.VIVID_SKY_BLUE))
                    .appendNewline()
                    .append(NAME)
                    .append(Component.text(plugin.description.name, KryptonColors.TURQUOISE))
                    .appendNewline()
                    .append(VERSION)
                    .append(Component.text(plugin.description.version, NamedTextColor.GREEN))
                    .appendNewline()
                    .append(DESCRIPTION)
                    .append(Component.text(plugin.description.description, NamedTextColor.GOLD))
                    .appendNewline()
                    .append(AUTHORS)
                    .append(Component.text(plugin.description.authors.joinToString(", "), NamedTextColor.YELLOW))
                    .appendNewline()
                    .append(DEPENDENCIES)
                val dependencies = plugin.description.dependencies
                if (dependencies.isNotEmpty()) {
                    pluginMessage.appendNewline().append(foldToMessage(dependencies) { index, dependencyMessage, dependency ->
                        dependencyMessage.append(INDENT)
                            .append(Component.text(dependency.id, NamedTextColor.GREEN))
                            .append(OPEN_BRACKET)
                            .append(OPTIONAL)
                            .append(Component.text(dependency.isOptional, NamedTextColor.GREEN))
                            .append(CLOSED_BRACKET)
                        if (index < dependencies.size - 1) dependencyMessage.appendNewline()
                    })
                } else {
                    pluginMessage.append(Component.text("None", NamedTextColor.RED))
                }
                builder.append(Component.text()
                    .append(Component.text(plugin.description.id, KryptonColors.LIGHTER_PURPLE))
                    .append(OPEN_BRACKET)
                    .append(Component.text(plugin.description.version, NamedTextColor.GREEN))
                    .append(CLOSED_BRACKET)
                    .hoverEvent(HoverEvent.showText(pluginMessage)))
                if (pluginIndex < plugins.size - 1) builder.append(SEPARATOR)
            }
            context.source.sendSystemMessage(Component.text()
                .append(PREFIX)
                .append(OPEN_BRACKET)
                .append(Component.text(plugins.size, NamedTextColor.GREEN))
                .append(CLOSED_BRACKET)
                .append(COLON)
                .append(message)
                .build())
        }
    }

    @JvmStatic
    private inline fun <T> foldToMessage(iterable: Iterable<T>, operation: (Int, ComponentBuilder<*, *>, T) -> Unit): Component =
        iterable.foldIndexed(Component.text()) { index, builder, value ->
            operation(index, builder, value)
            builder
        }.build()
}
