/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * A property of a [GameProfile].
 */
public interface ProfileProperty {

    /**
     * The name of the property.
     */
    public val name: String

    /**
     * The value of the property.
     */
    public val value: String

    /**
     * The Yggdrasil signature for this property. May be null if this property
     * isn't signed.
     */
    public val signature: String?

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, value: String, signature: String? = null): ProfileProperty
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new profile property with the given [name] and [value], with
         * no signature.
         *
         * @param name the name
         * @param value the value
         * @return a new profile property with the given name and value
         */
        public fun of(name: String, value: String): ProfileProperty = FACTORY.of(name, value)

        /**
         * Creates a new profile property with the given [name] and [value], and
         * [signature].
         *
         * @param name the name
         * @param value the value
         * @param signature the signature, or null for no signature
         * @return a new profile property with the given name and value
         */
        public fun of(
            name: String,
            value: String,
            signature: String?
        ): ProfileProperty = FACTORY.of(name, value, signature)
    }
}
