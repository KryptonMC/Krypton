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

import org.kryptonmc.api.event.annotation.PerformanceSensitive
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.api.util.Position
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a player moves.
 *
 * This event is called incredibly frequently, and so any processing should be
 * either incredibly fast or handled asynchronously. Even for a server with
 * one or two players, this event could be called up to one hundred times per
 * second, or even more.
 */
@PerformanceSensitive
public interface PlayerMoveEvent : PlayerEvent, DeniableEventWithResult<PlayerMoveEvent.Result> {

    /**
     * The position of the player before they moved.
     */
    public val oldPosition: Position

    /**
     * The position of the player after they moved.
     */
    public val newPosition: Position

    /**
     * The result of a move event.
     *
     * This allows plugins to modify the position that players will move to.
     *
     * @property newPosition The new position to move the player to.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val newPosition: Position)
}
