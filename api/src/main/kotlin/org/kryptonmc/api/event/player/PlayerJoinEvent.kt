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
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a player logs in and a player object has been constructed for
 * them.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PlayerJoinEvent : PlayerEvent, DeniableEventWithResult<PlayerJoinEvent.Result> {

    /**
     * If the player has joined the server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The result of a join event.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(
        /**
         * The custom join message to send, or null, if no custom message is to
         * be sent.
         *
         * Note: some implementations may still send a join message.
         */
        public val message: Component?,
        /**
         * If the joining player has joined before.
         *
         * Implementations may choose to use this information to alter the join
         * message.
         */
        public val hasJoinedBefore: Boolean
    )
}
