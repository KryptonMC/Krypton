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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.apache.commons.lang3.StringUtils
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.commands.KryptonPermission

fun CommandContext<Sender>.rawArguments(): String {
    val firstSpace = input.indexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR)
    if (firstSpace == -1) return ""
    return input.substring(firstSpace + 1)
}

fun CommandContext<Sender>.splitArguments(): Array<String> {
    val raw = rawArguments()
    if (raw.isEmpty()) return emptyArray()
    return StringUtils.split(raw)
}

fun LiteralArgumentBuilder<Sender>.permission(permission: KryptonPermission): LiteralArgumentBuilder<Sender> =
    requires { it.hasPermission(permission.node) }

fun String.normalize(trim: Boolean): String {
    val command = if (trim) trim() else this
    val firstSeparator = command.indexOf(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR)
    if (firstSeparator != -1) return command.substring(0, firstSeparator).lowercase() + command.substring(firstSeparator)
    return command.lowercase()
}
