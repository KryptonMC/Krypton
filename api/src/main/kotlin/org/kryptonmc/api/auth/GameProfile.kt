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
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.UUID

/**
 * The profile of an authenticated player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface GameProfile {

    /**
     * The UUID of the profile.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * The name of the profile.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * All of the properties for this profile.
     */
    @get:JvmName("properties")
    public val properties: @Unmodifiable List<ProfileProperty>

    /**
     * Creates a new game profile with the given [properties].
     *
     * @param properties the new properties
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withProperties(properties: Iterable<ProfileProperty>): GameProfile

    /**
     * Creates a new game profile with the given [property] added to the list
     * of properties.
     *
     * @param property the property to add
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withProperty(property: ProfileProperty): GameProfile

    /**
     * Creates a new game profile with the property at the given [index]
     * removed from the list of properties.
     *
     * @param index the index of the property to remove
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProperty(index: Int): GameProfile

    /**
     * Creates a new game profile with the given [property] removed from the
     * list of properties.
     *
     * @param property the property to remove
     * @return a new game profile
     */
    @Contract("_ -> new", pure = true)
    public fun withoutProperty(property: ProfileProperty): GameProfile

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile
    }

    public companion object {

        /**
         * Creates a new game profile with the given [name], [uuid], and list
         * of profile [properties].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @param properties the list of profile properties
         * @return a new profile
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile =
            Krypton.factory<Factory>().of(name, uuid, properties)

        /**
         * Creates a new game profile with the given [name] and [uuid].
         *
         * @param name the name of the profile
         * @param uuid the UUID of the profile
         * @return a new profile
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(name: String, uuid: UUID): GameProfile = of(name, uuid, emptyList())
    }
}
