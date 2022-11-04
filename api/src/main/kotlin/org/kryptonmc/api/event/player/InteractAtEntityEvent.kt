/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Hand
import org.spongepowered.math.vector.Vector3d

/**
 * Called when a player interacts at an entity.
 */
public interface InteractAtEntityEvent : EntityInteractEvent {

    /**
     * The hand the player used to interact with the target.
     */
    public val hand: Hand

    /**
     * The position that the player clicked on the entity.
     */
    public val clickedPosition: Vector3d

    override val type: InteractEvent.Type
        get() = InteractEvent.Type.INTERACT_AT_ENTITY
}
