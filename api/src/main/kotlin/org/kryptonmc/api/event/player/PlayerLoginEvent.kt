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
public interface PlayerLoginEvent : DeniableEventWithResult<PlayerLoginEvent.Result> {

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
