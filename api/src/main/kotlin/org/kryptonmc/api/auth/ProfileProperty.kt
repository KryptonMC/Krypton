/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.auth

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import java.util.function.Consumer

/**
 * A property of a [GameProfile].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ProfileProperty : Buildable<ProfileProperty, ProfileProperty.Builder> {

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
     * Creates a new profile property by creating a new builder from this
     * property and applying the given [builder] to it.
     *
     * @param builder the builder to apply
     * @return a new profile property
     */
    @JvmSynthetic
    @Contract("_ -> new", pure = true)
    public fun with(builder: Builder.() -> Unit): ProfileProperty

    /**
     * Creates a new profile property by creating a new builder from this
     * property and applying the given [builder] to it.
     *
     * @param builder the builder to apply
     * @return a new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun with(builder: Consumer<Builder>): ProfileProperty = with { builder.accept(this) }

    /**
     * Creates a new profile property with the given [name].
     *
     * @param name the new name
     * @return a new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withName(name: String): ProfileProperty

    /**
     * Creates a new profile property with the given [value].
     *
     * @param value the new value
     * @return a new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withValue(value: String): ProfileProperty

    /**
     * Creates a new profile property with the given [signature].
     *
     * @param signature the new signature
     * @return a new profile property
     */
    @Contract("_ -> new", pure = true)
    public fun withSignature(signature: String?): ProfileProperty

    /**
     * A builder for building profile properties.
     */
    public interface Builder : Buildable.Builder<ProfileProperty> {

        /**
         * Sets the name of the profile property to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @Contract("_ -> new", pure = true)
        public fun name(name: String): Builder

        /**
         * Sets the value of the profile property to the given [value].
         *
         * @param value the value
         * @return this builder
         */
        @Contract("_ -> new", pure = true)
        public fun value(value: String): Builder

        /**
         * Sets the signature of the profile property to the given [signature].
         *
         * @param signature the signature
         * @return this builder
         */
        @Contract("_ -> new", pure = true)
        public fun signature(signature: String?): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, value: String, signature: String?): ProfileProperty
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new profile property with the given [name] and [value], with
         * no signature.
         *
         * @param name the name
         * @param value the value
         * @return a new profile property with the given name and value
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(name: String, value: String): ProfileProperty = of(name, value, null)

        /**
         * Creates a new profile property with the given [name] and [value], and
         * [signature].
         *
         * @param name the name
         * @param value the value
         * @param signature the signature, or null for no signature
         * @return a new profile property with the given name and value
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            name: String,
            value: String,
            signature: String?
        ): ProfileProperty = FACTORY.of(name, value, signature)
    }
}
