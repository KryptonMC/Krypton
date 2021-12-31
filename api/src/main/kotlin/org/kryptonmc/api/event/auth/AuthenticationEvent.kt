/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.auth

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.event.ResultedEvent.Result

/**
 * An event that is called when a request is made to authenticate a player with
 * the given [username].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class AuthenticationEvent(@get:JvmName("username") public val username: String) : ResultedEvent<AuthenticationResult> {

    @get:JvmName("result")
    override var result: AuthenticationResult = AuthenticationResult.allowed()
}

/**
 * The result of a request to authenticate a player.
 *
 * @param profile the optional profile for a successful result
 */
@JvmRecord
public data class AuthenticationResult(
    override val isAllowed: Boolean,
    public val profile: GameProfile?
) : Result {

    public companion object {

        private val ALLOWED = AuthenticationResult(true, null)
        private val DENIED = AuthenticationResult(false, null)

        /**
         * Gets the result that allows the authenticating player to join the
         * server as normal.
         *
         * @return the allowed result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun allowed(): AuthenticationResult = ALLOWED

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
        public fun allowed(profile: GameProfile): AuthenticationResult = AuthenticationResult(true, profile)

        /**
         * Gets the result that denies the authenticating player from joining
         * the server.
         *
         * @return the denied result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun denied(): AuthenticationResult = DENIED
    }
}
