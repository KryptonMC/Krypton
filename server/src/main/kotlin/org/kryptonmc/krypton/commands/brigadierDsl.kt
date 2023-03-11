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
