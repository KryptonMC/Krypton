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
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.runs

object InfoCommand : KryptonSubCommand {

    private val MESSAGE = Component.text()
        .append(Component.text("This server is running ", KryptonColors.LIGHTER_PURPLE))
        .append(Component.text()
            .content("Krypton ")
            .color(KryptonColors.STANDARD_PURPLE)
            .decorate(TextDecoration.BOLD)
            .clickEvent(ClickEvent.openUrl("https://kryptonmc.org")))
        .append(Component.text(KryptonPlatform.version, NamedTextColor.GREEN))
        .append(Component.text(" for Minecraft ", KryptonColors.LIGHTER_PURPLE))
        .append(Component.text(KryptonPlatform.minecraftVersion, NamedTextColor.GREEN))
        .build()

    override val aliases: Sequence<String> = sequenceOf("about", "version")

    override fun register(): LiteralArgumentBuilder<Sender> = literal("info") {
        runs { it.source.sendMessage(MESSAGE) }
    }
}
