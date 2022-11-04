/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import javax.annotation.concurrent.Immutable

/**
 * Holds metadata for a [Command].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
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
    @CommandMetaDsl
    public interface Builder : AbstractBuilder<CommandMeta> {

        /**
         * Sets the name of the command to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Adds the given [alias] to the list of aliases.
         *
         * @param alias the alias
         * @return this builder
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun alias(alias: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         *
         * @param aliases the aliases
         * @return this builder
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun aliases(aliases: Collection<String>): Builder
    }

    @ApiStatus.Internal
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
