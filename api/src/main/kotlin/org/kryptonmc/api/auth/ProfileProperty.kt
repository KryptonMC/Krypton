/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A property of a [GameProfile].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ProfileProperty {

    /**
     * The name of the property.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The value of the property.
     */
    @get:JvmName("value")
    public val value: String

    /**
     * The Yggdrasil signature for this property. May be null if this property
     * isn't signed.
     */
    @get:JvmName("signature")
    public val signature: String?

    /**
     * Creates a new profile property that is a copy of this one with the
     * given [signature].
     *
     * @param signature the new signature
     * @return the new, modified profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withSignature(signature: String?): ProfileProperty

    /**
     * Creates a new profile property that is a copy of this one without a
     * signature.
     *
     * @return the new, modified profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withoutSignature(): ProfileProperty

    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, value: String, signature: String?): ProfileProperty
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new profile property with the given [name], [value], and
         * optionally, [signature].
         *
         * @param name the name
         * @param value the value
         * @param signature the signature, or null for no signature
         * @return a new profile property with the given name and value
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_, _, _ -> new", pure = true)
        public fun of(
            name: String,
            value: String,
            signature: String? = null
        ): ProfileProperty = FACTORY.of(name, value, signature)
    }
}
