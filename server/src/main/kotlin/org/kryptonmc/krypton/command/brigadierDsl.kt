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
import org.kryptonmc.api.command.Sender
import java.util.function.Consumer

inline fun literal(name: String, builder: LiteralArgumentBuilder<Sender>.() -> Unit): LiteralArgumentBuilder<Sender> =
    LiteralArgumentBuilder.literal<Sender>(name).apply(builder)

inline fun <T> argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<Sender, T>.() -> Unit
): RequiredArgumentBuilder<Sender, T> = RequiredArgumentBuilder.argument<Sender, T>(name, type).apply(builder)

fun LiteralArgumentBuilder<Sender>.literal(name: String, builder: LiteralArgumentBuilder<Sender>.() -> Unit): LiteralArgumentBuilder<Sender> =
    then(LiteralArgumentBuilder.literal<Sender>(name).apply(builder))

fun <T> LiteralArgumentBuilder<Sender>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<Sender, T>.() -> Unit
): LiteralArgumentBuilder<Sender> = then(RequiredArgumentBuilder.argument<Sender, T>(name, type).apply(builder))

fun <T, T1> RequiredArgumentBuilder<Sender, T>.argument(
    name: String,
    type: ArgumentType<T1>,
    builder: RequiredArgumentBuilder<Sender, T1>.() -> Unit
): RequiredArgumentBuilder<Sender, T> = then(RequiredArgumentBuilder.argument<Sender, T1>(name, type).apply(builder))

fun <T : ArgumentBuilder<Sender, T>> ArgumentBuilder<Sender, T>.runs(action: Consumer<CommandContext<Sender>>): ArgumentBuilder<Sender, T> =
    executes {
        action.accept(it)
        com.mojang.brigadier.Command.SINGLE_SUCCESS
    }
