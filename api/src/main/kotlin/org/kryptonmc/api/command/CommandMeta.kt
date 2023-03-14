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

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * Holds metadata for a [Command].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface CommandMeta {

    /**
     * The name of the command.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * All of the aliases of the command.
     */
    @get:JvmName("aliases")
    public val aliases: Set<String>

    /**
     * A builder for [CommandMeta].
     */
    public interface Builder : AbstractBuilder<CommandMeta> {

        /**
         * Sets the name of the command to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Adds the given [alias] to the list of aliases.
         *
         * @param alias the alias
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun alias(alias: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         *
         * @param aliases the aliases
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun aliases(aliases: Collection<String>): Builder
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun builder(name: String): Builder
    }

    public companion object {

        /**
         * Creates a new builder for constructing command metadata.
         *
         * @param name the name of the command
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(name: String): Builder = Krypton.factory<Factory>().builder(name)
    }
}
