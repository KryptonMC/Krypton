/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when an entity goes out of view of a player.
 */
public interface EntityExitViewEvent : PlayerEvent {

    /**
     * The entity that went out of view.
     */
    public val entity: Entity
}
