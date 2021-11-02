/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command.meta

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.util.provide

/**
 * Holds metadata for a [org.kryptonmc.api.command.Command].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface CommandMeta : Buildable<CommandMeta, CommandMeta.Builder> {

    /**
     * The name of the command.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The set of aliases of the command.
     */
    @get:JvmName("aliases")
    public val aliases: Set<String>

    /**
     * A builder for [CommandMeta].
     */
    @CommandMetaDsl
    public interface Builder : Buildable.Builder<CommandMeta> {

        /**
         * Sets the name of the command to the given [name].
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Adds the given [alias] to the list of aliases.
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun alias(alias: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun aliases(vararg aliases: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         */
        @CommandMetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun aliases(aliases: Iterable<String>): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(name: String): Builder

        public fun simpleBuilder(name: String): SimpleCommandMeta.Builder
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for constructing command metadata.
         *
         * @param name the name of the command
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(name: String): Builder = FACTORY.builder(name)

        /**
         * Creates a new builder for constructing command metadata from the given
         * Brigadier command.
         *
         * @param command the Brigadier command
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(command: BrigadierCommand): Builder = builder(command.node.name)
    }
}
