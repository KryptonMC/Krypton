/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.annotation.PerformanceSensitive
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.api.util.Vec3d
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
     * The location of the player before they moved.
     */
    public val oldLocation: Vec3d

    /**
     * The location of the player after they moved.
     */
    public val newLocation: Vec3d

    /**
     * The result of a move event.
     *
     * This allows plugins to modify the location that players will move to.
     *
     * @property newLocation The new location to move the player to.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val newLocation: Vec3d)
}
