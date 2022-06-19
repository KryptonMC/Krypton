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

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext

inline fun <S> literal(name: String, builder: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> =
    LiteralArgumentBuilder.literal<S>(name).apply(builder)

inline fun <S, T> argument(name: String, type: ArgumentType<T>, builder: RequiredArgumentBuilder<S, T>.() -> Unit): RequiredArgumentBuilder<S, T> =
    RequiredArgumentBuilder.argument<S, T>(name, type).apply(builder)

fun <S> LiteralArgumentBuilder<S>.literal(name: String, builder: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> =
    then(LiteralArgumentBuilder.literal<S>(name).apply(builder))

fun <S, T> LiteralArgumentBuilder<S>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
): LiteralArgumentBuilder<S> = then(RequiredArgumentBuilder.argument<S, T>(name, type).apply(builder))

fun <S, T, T1> RequiredArgumentBuilder<S, T>.argument(
    name: String,
    type: ArgumentType<T1>,
    builder: RequiredArgumentBuilder<S, T1>.() -> Unit
): RequiredArgumentBuilder<S, T> = then(RequiredArgumentBuilder.argument<S, T1>(name, type).apply(builder))

fun <S, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.runs(action: (CommandContext<S>) -> Unit): ArgumentBuilder<S, T> = executes {
    action(it)
    com.mojang.brigadier.Command.SINGLE_SUCCESS
}
