/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.internal.annotations.ImmutableType
import java.net.InetSocketAddress

/**
 * Called when a player has been authenticated, but they have not yet had
 * a player object constructed for them.
 */
public interface LoginEvent : DeniableEventWithResult<LoginEvent.Result> {

    /**
     * The game profile of the player logging in.
     */
    public val profile: GameProfile

    /**
     * The address that the player is logging in from.
     */
    public val address: InetSocketAddress

    /**
     * The result of a login event.
     *
     * This allows plugins to specify a reason for a player to be kicked.
     *
     * @property reason The reason for the player to be kicked.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val reason: Component)
}
