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
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.world.World

/**
 * Called when the given [entity] spawns in to the given [world].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface SpawnEntityEvent : ResultedEvent<GenericResult> {

    /**
     * The entity that is being spawned in to the world.
     */
    @get:JvmName("entity")
    public val entity: Entity

    /**
     * The world that the entity is being spawned in to.
     */
    @get:JvmName("world")
    public val world: World
}
