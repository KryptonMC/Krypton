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
