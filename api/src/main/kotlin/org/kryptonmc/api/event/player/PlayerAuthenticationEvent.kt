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
