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
package org.kryptonmc.api.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import org.jetbrains.annotations.Contract
import org.kryptonmc.internal.annotations.dsl.BrigadierDsl

/**
 * Creates a new Brigadier literal argument builder with the given [name],
 * applying the given [builder] to it and returning the result.
 *
 * @param S the command source type
 * @param name the name of the literal
 * @param builder the builder to apply
 * @return a new literal argument builder
 */
@Contract("_, _ -> new", pure = true)
@JvmSynthetic
@BrigadierDsl
public inline fun <S> literalCommand(name: String, builder: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> {
    return LiteralArgumentBuilder.literal<S>(name).apply(builder)
}

/**
 * Adds a new literal command node with the given name to this literal,
 * applying the given [builder] to the node and returning this builder as the
 * result.
 *
 * @param S the command source type
 * @param name the name of the literal
 * @param builder the builder to apply
 * @return this builder
 */
@Contract("_, _ -> this", mutates = "this")
@JvmSynthetic
@BrigadierDsl
public inline fun <S> LiteralArgumentBuilder<S>.literal(name: String, builder: LiteralArgumentBuilder<S>.() -> Unit): LiteralArgumentBuilder<S> {
    return then(LiteralArgumentBuilder.literal<S>(name).apply(builder))
}

/**
 * Adds a new argument command node with the given [name] and [type] to this
 * literal, applying the given [builder] to the node and returning this
 * builder as the result.
 *
 * @param S the command source type
 * @param T the argument type
 * @param name the name of the argument
 * @param type the argument type for parsing
 * @param builder the builder to apply
 * @return this builder
 */
@Contract("_, _, _ -> this", mutates = "this")
@JvmSynthetic
@BrigadierDsl
public inline fun <S, T> LiteralArgumentBuilder<S>.argument(
    name: String,
    type: ArgumentType<T>,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
): LiteralArgumentBuilder<S> {
    return then(RequiredArgumentBuilder.argument<S, T>(name, type).apply(builder))
}

/**
 * Adds a new argument command node with the given [name] and [type] to this
 * argument, applying the given [builder] to the node and returning this
 * builder as the result.
 *
 * @param S the command source type
 * @param T the argument type of this argument
 * @param T1 the argument type of the new argument
 * @param name the name of the argument
 * @param type the argument type for parsing
 * @param builder the builder to apply
 * @return this builder
 */
@Contract("_, _, _ -> this", mutates = "this")
@JvmSynthetic
@BrigadierDsl
public inline fun <S, T, T1> RequiredArgumentBuilder<S, T>.argument(
    name: String,
    type: ArgumentType<T1>,
    builder: RequiredArgumentBuilder<S, T1>.() -> Unit
): RequiredArgumentBuilder<S, T> {
    return then(RequiredArgumentBuilder.argument<S, T1>(name, type).apply(builder))
}
