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
 * Called when a player sends a chat message (not a command).
 */
public interface PlayerChatEvent : PlayerEvent, DeniableEventWithResult<PlayerChatEvent.Result> {

    /**
     * The message that the player has sent.
     */
    public val message: String

    /**
     * The result of a chat event.
     *
     * This allows you to modify the message that was sent by the player.
     * For example, you could replace bad words with asterisks.
     *
     * @property message The message that will be sent by the player.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val message: Component)
}
