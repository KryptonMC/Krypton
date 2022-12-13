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
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.krypton.command.CommandSourceStack

inline fun literal(name: String, builder: LiteralBuilder.() -> Unit): LiteralBuilder = LiteralBuilder.literal<SrcStack>(name).apply(builder)

inline fun <T> argument(name: String, type: ArgumentType<T>, builder: ArgBuilder<T>.() -> Unit): ArgBuilder<T> =
    ArgBuilder.argument<SrcStack, T>(name, type).apply(builder)

fun LiteralBuilder.literal(name: String, builder: LiteralBuilder.() -> Unit): LiteralBuilder =
    then(LiteralBuilder.literal<SrcStack>(name).apply(builder))

fun <T> LiteralBuilder.argument(name: String, type: ArgumentType<T>, builder: ArgBuilder<T>.() -> Unit): LiteralBuilder =
    then(ArgBuilder.argument<SrcStack, T>(name, type).apply(builder))

fun <T, T1> ArgBuilder<T>.argument(name: String, type: ArgumentType<T1>, builder: ArgBuilder<T1>.() -> Unit): ArgBuilder<T> =
    then(ArgBuilder.argument<SrcStack, T1>(name, type).apply(builder))

inline fun <T : Builder<T>> Builder<T>.runs(crossinline action: (CommandContext<SrcStack>) -> Unit): Builder<T> = executes {
    action(it)
    Command.SINGLE_SUCCESS
}

fun LiteralBuilder.permission(permission: KryptonPermission): LiteralBuilder = requires { it.hasPermission(permission) }

/**
 * Equivalent to [CommandContext.getArgument], except this uses a reified type
 * to improve QOL in Kotlin. This also doesn't return a platform type.
 */
inline fun <reified T> CommandContext<*>.getArgument(name: String): T = getArgument(name, T::class.java)

private typealias SrcStack = CommandSourceStack
private typealias Builder<T> = ArgumentBuilder<SrcStack, T>
private typealias LiteralBuilder = LiteralArgumentBuilder<SrcStack>
private typealias ArgBuilder<T> = RequiredArgumentBuilder<SrcStack, T>
