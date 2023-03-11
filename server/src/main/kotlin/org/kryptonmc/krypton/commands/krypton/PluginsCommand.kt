/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.commands.krypton

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.commands.runs
import org.kryptonmc.krypton.plugin.KryptonPluginContainer
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

    override fun register(): LiteralArgumentBuilder<CommandSourceStack> = literalCommand("plugins") {
        runs { context ->
            val plugins = context.source.server.pluginManager.plugins
            if (plugins.isEmpty()) {
                context.source.sendSystemMessage(Component.text("No plugins are currently running on this server.", NamedTextColor.RED))
                return@runs
            }

            val message = foldToMessage(plugins) { pluginIndex, builder, plugin ->
                // Don't include modules in the plugin list.
                if (plugin is KryptonPluginContainer && plugin.isModule) return@foldToMessage

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
