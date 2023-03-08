/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player

/**
 * Something that contains entities.
 */
public interface EntityContainer {

    /**
     * All entities contained within this container.
     */
    public val entities: Collection<Entity>

    /**
     * All players contained within this container.
     */
    public val players: Collection<Player>
}
