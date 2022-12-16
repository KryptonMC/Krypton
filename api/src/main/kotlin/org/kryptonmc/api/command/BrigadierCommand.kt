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
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A command that is backed by a Brigadier [LiteralCommandNode].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BrigadierCommand : Command {

    /**
     * The built command node representing the command tree for this specific
     * command.
     */
    @get:JvmName("node")
    public val node: LiteralCommandNode<CommandExecutionContext>

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(node: LiteralCommandNode<CommandExecutionContext>): BrigadierCommand
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
        public fun of(node: LiteralCommandNode<CommandExecutionContext>): BrigadierCommand = Krypton.factory<Factory>().of(node)

        /**
         * Creates a new command backed by the Brigadier command node built
         * from the given [builder].
         *
         * @param builder the builder to build the backing command node from
         * @return a new Brigadier command
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(builder: LiteralArgumentBuilder<CommandExecutionContext>): BrigadierCommand = of(builder.build())
    }
}
