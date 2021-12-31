/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
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
 *
 * @param entity the entity that is spawning
 * @param world the world that the entity is spawning in to
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class EntitySpawnEvent(
    @get:JvmName("entity") public val entity: Entity,
    @get:JvmName("world") public val world: World
) : ResultedEvent<GenericResult> {

    @get:JvmName("result")
    override var result: GenericResult = GenericResult.allowed()
}
