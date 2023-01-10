/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
