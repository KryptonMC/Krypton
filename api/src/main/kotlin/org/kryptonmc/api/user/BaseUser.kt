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
package org.kryptonmc.api.user

import org.kryptonmc.api.auth.GameProfile
import java.time.Instant

/**
 * Common data associated with both users and players.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BaseUser {

    /**
     * The cached game profile associated with this user.
     */
    public val profile: GameProfile

    /**
     * If this user has joined this server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The time that this user first joined the server.
     */
    public val firstJoined: Instant

    /**
     * The latest time when this user last joined the server.
     */
    public val lastJoined: Instant

    /**
     * If this user is online or not.
     *
     * @return true if this user is online
     */
    public fun isOnline(): Boolean
}
