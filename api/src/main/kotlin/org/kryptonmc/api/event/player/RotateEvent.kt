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
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a player rotates their head.
 */
@PerformanceSensitive
public interface RotateEvent : PlayerEvent, DeniableEventWithResult<RotateEvent.Result> {

    /**
     * The yaw of the player before the change in rotation.
     */
    public val oldYaw: Float

    /**
     * The pitch of the player before the change in rotation.
     */
    public val oldPitch: Float

    /**
     * The yaw of the player after the change in rotation.
     */
    public val newYaw: Float

    /**
     * The pitch of the player after the change in rotation.
     */
    public val newPitch: Float

    /**
     * The result of a rotate event.
     *
     * This allows plugins to modify the rotation that players will rotate to.
     *
     * @property newYaw The new yaw to rotate the player to.
     * @param newPitch The new pitch to rotate the player to.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val newYaw: Float, public val newPitch: Float)
}
