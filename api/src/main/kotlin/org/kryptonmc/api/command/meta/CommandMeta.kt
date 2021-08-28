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
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * Holds metadata for a [org.kryptonmc.api.command.Command].
 */
interface CommandMeta : Buildable<CommandMeta, CommandMeta.Builder> {

    /**
     * The name of the command.
     */
    val name: String

    /**
     * The set of aliases of the command.
     */
    val aliases: Set<String>

    /**
     * A builder for [CommandMeta].
     */
    interface Builder : Buildable.Builder<CommandMeta> {

        /**
         * Sets the name of the command to the given [name].
         */
        fun name(name: String): Builder

        /**
         * Adds the given [alias] to the list of aliases.
         */
        fun alias(alias: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         */
        fun aliases(vararg aliases: String): Builder

        /**
         * Adds the given [aliases] to the list of aliases.
         */
        fun aliases(aliases: Iterable<String>): Builder
    }

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    interface Factory {

        fun builder(name: String): Builder

        fun simpleBuilder(name: String): SimpleCommandMeta.Builder
    }

    companion object {

        @JvmSynthetic
        internal val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new builder for constructing command metadata.
         *
         * @param name the name of the command
         * @return a new builder
         */
        @JvmStatic
        fun builder(name: String) = FACTORY.builder(name)

        /**
         * Creates a new builder for constructing command metadata from the given
         * Brigadier command.
         *
         * @param command the Brigadier command
         * @return a new builder
         */
        @JvmStatic
        fun builder(command: BrigadierCommand) = builder(command.node.name)
    }
}
