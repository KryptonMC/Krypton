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
 * Called when a player interacts on an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface InteractEntityEvent : EntityInteractEvent {

    /**
     * The hand that the player used to interact with the target.
     */
    @get:JvmName("hand")
    public val hand: Hand

    override val type: InteractEvent.Type
        get() = InteractEvent.Type.INTERACT_ON_ENTITY
}
