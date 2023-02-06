/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.entity

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.event.type.DeniableEvent
import org.kryptonmc.api.event.type.EntityEvent
import org.kryptonmc.api.world.World

/**
 * Called when the given [entity] spawns in to the given [world].
 */
public interface SpawnEntityEvent : EntityEvent, DeniableEvent {

    /**
     * The entity that is being spawned in to the world.
     */
    override val entity: Entity

    /**
     * The world that the entity is being spawned in to.
     */
    public val world: World
}
