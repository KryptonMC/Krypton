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
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.internal.annotations.dsl.BrigadierDsl
import org.kryptonmc.krypton.command.CommandSourceStack

@BrigadierDsl
inline fun <T : Builder<T>> Builder<T>.runs(crossinline action: (CommandContext<CommandSourceStack>) -> Unit): Builder<T> = executes {
    action(it)
    Command.SINGLE_SUCCESS
}

@BrigadierDsl
fun LiteralArgumentBuilder<CommandSourceStack>.requiresPermission(permission: KryptonPermission): LiteralArgumentBuilder<CommandSourceStack> =
    requires { it.hasPermission(permission) }

/**
 * Equivalent to [CommandContext.getArgument], except this uses a reified type
 * to improve QOL in Kotlin. This also doesn't return a platform type.
 */
inline fun <reified T> CommandContext<*>.getArgument(name: String): T = getArgument(name, T::class.java)

private typealias Builder<T> = ArgumentBuilder<CommandSourceStack, T>
