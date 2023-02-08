/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * An event that is called when a request is made to authenticate a player with
 * the given [username].
 */
public interface PlayerAuthenticationEvent : DeniableEventWithResult<PlayerAuthenticationEvent.Result> {

    /**
     * The username of the player that is being authenticated.
     */
    public val username: String

    /**
     * The result of a request to authenticate a player.
     *
     * This allows for the authenticated profile to be provided to the server,
     * to allow plugins to replace the authentication process with their own.
     *
     * @property profile The replacement authenticated profile to use.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val profile: GameProfile)
}
