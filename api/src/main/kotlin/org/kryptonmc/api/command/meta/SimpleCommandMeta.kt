/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command.meta

/**
 * Command metadata for a [org.kryptonmc.api.command.SimpleCommand].
 */
public interface SimpleCommandMeta : CommandMeta {

    /**
     * The permission required to execute the command, or null if no permission
     * is required.
     */
    public val permission: String?

    override fun toBuilder(): Builder

    /**
     * A builder for [SimpleCommandMeta].
     */
    public interface Builder : CommandMeta.Builder {

        /**
         * Sets the permission required to use the command to the given [permission].
         *
         * @param permission the permission
         */
        public fun permission(permission: String?): Builder

        override fun build(): SimpleCommandMeta
    }

    public companion object {

        /**
         * Creates a new builder for constructing simple command metadata.
         *
         * @param name the name of the command
         * @return a new builder
         */
        @JvmStatic
        public fun builder(name: String): Builder = CommandMeta.FACTORY.simpleBuilder(name)
    }
}
