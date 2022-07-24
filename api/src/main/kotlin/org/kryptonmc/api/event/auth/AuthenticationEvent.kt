/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.auth

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.ResultedEvent

/**
 * An event that is called when a request is made to authenticate a player with
 * the given [username].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AuthenticationEvent : ResultedEvent<AuthenticationEvent.Result> {

    /**
     * The username of the player that is being authenticated.
     */
    @get:JvmName("username")
    public val username: String

    /**
     * The result of a request to authenticate a player.
     *
     * @param profile the optional profile for a successful result
     */
    @JvmRecord
    public data class Result(override val isAllowed: Boolean, public val profile: GameProfile?) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED = Result(true, null)
            private val DENIED = Result(false, null)

            /**
             * Gets the result that allows the authenticating player to join the
             * server as normal.
             *
             * @return the allowed result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun allowed(): Result = ALLOWED

            /**
             * Creates a new result that allows the authenticating player to join
             * the server as normal, but silently replaces their authenticated
             * profile with the given [profile].
             *
             * @param profile the profile to replace with
             * @return a new allowed result
             */
            @JvmStatic
            @Contract("_ -> new", pure = true)
            public fun allowed(profile: GameProfile): Result = Result(true, profile)

            /**
             * Gets the result that denies the authenticating player from joining
             * the server.
             *
             * @return the denied result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun denied(): Result = DENIED
        }
    }
}
