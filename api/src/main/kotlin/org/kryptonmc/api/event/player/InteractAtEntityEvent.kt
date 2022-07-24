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

/**
 * Called when a player interacts at an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface InteractAtEntityEvent : EntityInteractEvent {

    /**
     * The hand the player used to interact with the target.
     */
    @get:JvmName("hand")
    public val hand: Hand

    /**
     * The X coordinate of the position that the player clicked on the entity.
     */
    @get:JvmName("clickedX")
    public val clickedX: Double

    /**
     * The Y coordinate of the position that the player clicked on the entity.
     */
    @get:JvmName("clickedY")
    public val clickedY: Double

    /**
     * The Z coordinate of the position that the player clicked on the entity.
     */
    @get:JvmName("clickedZ")
    public val clickedZ: Double

    override val type: InteractEvent.Type
        get() = InteractEvent.Type.INTERACT_AT_ENTITY
}
