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
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A property of a game profile.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
     * The signature for this property.
     *
     * May be null if this property isn't signed.
     */
    @get:JvmName("signature")
    public val signature: String?

    /**
     * Creates a new profile property with the given [signature].
     *
     * @param signature the new signature
     * @return the new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withSignature(signature: String?): ProfileProperty

    /**
     * Creates a new profile property without a signature.
     *
     * @return the new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withoutSignature(): ProfileProperty

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(name: String, value: String, signature: String?): ProfileProperty
    }

    public companion object {

        /**
         * Creates a new profile property with the given [name], [value], and
         * [signature].
         *
         * @param name the name
         * @param value the value
         * @param signature the signature, or null for no signature
         * @return a new profile property
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(name: String, value: String, signature: String?): ProfileProperty = Krypton.factory<Factory>().of(name, value, signature)

        /**
         * Creates a new profile property with the given [name] and [value].
         *
         * @param name the name
         * @param value the value
         * @return a new profile property
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(name: String, value: String): ProfileProperty = of(name, value, null)
    }
}
