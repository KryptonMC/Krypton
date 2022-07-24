/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A command that is backed by a Brigadier [LiteralCommandNode].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BrigadierCommand : Command {

    /**
     * The built command node representing the command tree for this specific
     * command.
     */
    @get:JvmName("node")
    public val node: LiteralCommandNode<Sender>

    @ApiStatus.Internal
    public interface Factory {

        public fun of(node: LiteralCommandNode<Sender>): BrigadierCommand
    }

    public companion object {

        /**
         * Creates a new command backed by the given Brigadier command [node].
         *
         * @param node the backing command node
         * @return a new Brigadier command
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(node: LiteralCommandNode<Sender>): BrigadierCommand = Krypton.factoryProvider.provide<Factory>().of(node)

        /**
         * Creates a new command backed by the Brigadier command node built
         * from the given [builder].
         *
         * @param builder the builder to build the backing command node from
         * @return a new Brigadier command
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(builder: LiteralArgumentBuilder<Sender>): BrigadierCommand = of(builder.build())
    }
}

/**
 * Builds a command backed by a Brigadier [LiteralCommandNode].
 *
 * @param literal the primary command name
 * @param builder the builder to build the command with
 * @return the built Brigadier command
 */
@JvmSynthetic
@Contract("_, _ -> new", pure = true)
public inline fun brigadierCommand(literal: String, builder: LiteralArgumentBuilder<Sender>.() -> Unit): BrigadierCommand =
    BrigadierCommand.of(LiteralArgumentBuilder.literal<Sender>(literal).apply(builder).build())
