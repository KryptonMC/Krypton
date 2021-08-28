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
interface ProfileProperty {

    /**
     * The name of the property.
     */
    val name: String

    /**
     * The value of the property.
     */
    val value: String

    /**
     * The Yggdrasil signature for this property. May be null if this property isn't
     * signed.
     */
    val signature: String?

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    interface Factory {

        fun of(name: String, value: String, signature: String? = null): ProfileProperty
    }

    companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new profile property with the given [name] and [value], with no
         * signature.
         *
         * @param name the name
         * @param value the value
         * @return a new profile property with the given name and value
         */
        fun of(name: String, value: String) = FACTORY.of(name, value)

        /**
         * Creates a new profile property with the given [name] and [value], and
         * [signature].
         *
         * @param name the name
         * @param value the value
         * @param signature the signature, or null for no signature
         * @return a new profile property with the given name and value
         */
        fun of(name: String, value: String, signature: String?) = FACTORY.of(name, value, signature)
    }
}
