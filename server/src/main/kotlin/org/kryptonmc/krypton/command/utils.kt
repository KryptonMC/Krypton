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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.LiteralCommandNode
import org.apache.commons.lang3.StringUtils
import org.kryptonmc.api.command.Sender

fun LiteralCommandNode<Sender>.buildCopy(newName: String): LiteralArgumentBuilder<Sender> = literal<Sender>(newName)
    .requires(requirement)
    .requiresWithContext(contextRequirement)
    .forward(redirect, redirectModifier, isFork)
    .executes(command)
    .apply { children.forEach { then(it) } }

fun LiteralCommandNode<Sender>.copy(newName: String): LiteralCommandNode<Sender> = buildCopy(newName).build()

fun buildRawArgumentsLiteral(
    alias: String,
    command: Command<Sender>,
    suggestionProvider: SuggestionProvider<Sender>
): LiteralCommandNode<Sender> = literal<Sender>(alias.lowercase())
    .then(argument<Sender, String>("arguments", greedyString())
        .suggests(suggestionProvider)
        .executes(command))
    .executes(command)
    .build()

val CommandContext<Sender>.rawArguments: String
    get() {
        val firstSpace = input.indexOf(' ')
        return if (firstSpace == -1) "" else input.substring(firstSpace + 1)
    }

val CommandContext<Sender>.splitArguments: Array<String>
    get() {
        val raw = rawArguments
        return if (raw.isEmpty()) emptyArray() else StringUtils.split(raw)
    }

fun LiteralArgumentBuilder<Sender>.permission(permission: String, level: Int) = apply {
    requires { it.hasPermission(permission) || it.permissionLevel >= level }
}

fun String.normalize(trim: Boolean): String {
    val command = if (trim) trim() else this
    val firstSeparator = command.indexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR)
    if (firstSeparator != -1) {
        return command.substring(0, firstSeparator).lowercase() + command.substring(firstSeparator)
    }
    return command.lowercase()
}
