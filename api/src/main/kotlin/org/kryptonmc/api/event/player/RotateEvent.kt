/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.spongepowered.math.vector.Vector2f

/**
 * Called when a player rotates their head.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface RotateEvent : PlayerEvent {

    /**
     * The rotation of the player before the change in rotation.
     */
    @get:JvmName("oldRotation")
    public val oldRotation: Vector2f

    /**
     * The rotation of the player after the change in rotation.
     */
    @get:JvmName("newRotation")
    public val newRotation: Vector2f
}
