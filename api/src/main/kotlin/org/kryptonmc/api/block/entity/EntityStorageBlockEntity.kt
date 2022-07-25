/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.entity.Entity

/**
 * A block entity that stores entities within it.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface EntityStorageBlockEntity<T : Entity> : BlockEntity {

    /**
     * Whether this block entity is full of entities.
     */
    public val isFull: Boolean

    /**
     * The amount of entities contained within this block entity.
     */
    @get:JvmName("entityCount")
    public val entityCount: Int

    /**
     * The maximum amount of entities that can be stored within this block
     * entity.
     */
    @get:JvmName("maximumEntities")
    public var maximumEntities: Int

    /**
     * Releases all the entities contained within this block entity.
     *
     * @return the released entities
     */
    public fun releaseEntities(): List<T>

    /**
     * Adds the given [entity] to the entities contained within this block
     * entity.
     *
     * @param entity the entity
     */
    public fun addEntity(entity: T)

    /**
     * Clears all of the entities out of this block entity.
     */
    public fun clearEntities()
}
