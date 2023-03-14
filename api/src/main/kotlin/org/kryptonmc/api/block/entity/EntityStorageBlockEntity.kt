/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.entity.Entity

/**
 * A block entity that stores entities within it.
 */
public interface EntityStorageBlockEntity<T : Entity> : BlockEntity {

    /**
     * The amount of entities contained within this block entity.
     */
    public val entityCount: Int

    /**
     * The maximum amount of entities that can be stored within this block
     * entity.
     */
    public var maximumEntities: Int

    /**
     * Gets whether this block entity is full of entities.
     *
     * @return whether this block entity is full
     */
    public fun isFull(): Boolean = entityCount >= maximumEntities

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
