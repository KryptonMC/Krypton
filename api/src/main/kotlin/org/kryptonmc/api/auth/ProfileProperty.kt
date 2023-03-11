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
