/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.tracking

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.KryptonEntity
import java.util.function.Consumer
import java.util.function.Predicate

interface EntityTracker {

    /**
     * Called when an entity is added to the world.
     */
    fun <E : KryptonEntity> add(entity: KryptonEntity, position: Position, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>?)

    /**
     * Called when an entity is removed from the world.
     */
    fun <E : KryptonEntity> remove(entity: KryptonEntity, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>?)

    /**
     * Called when an entity moves. This will both update the position and update the chunk the entity is in,
     * if the entity has moved chunks.
     */
    fun <E : KryptonEntity> onMove(entity: KryptonEntity, newPosition: Position, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>?)

    /**
     * Gets all entities matching the target in the chunk at the given position.
     */
    fun <E : KryptonEntity> entitiesInChunkOfType(position: ChunkPos, target: EntityTypeTarget<E>): Collection<E>

    /**
     * Gets all entities in the chunk at the given position of the given type matching the predicate, if given.
     */
    fun <E : Entity> entitiesInChunkOfType(position: ChunkPos, type: Class<E>, predicate: Predicate<E>?): Collection<E>

    /**
     * Gets all entities in the chunk at the given position.
     */
    fun entitiesInChunk(position: ChunkPos): Collection<KryptonEntity> = entitiesInChunkOfType(position, EntityTypeTarget.ENTITIES)

    /**
     * Gets all entities within range of the position matching the target, and calls the query consumer on them.
     */
    fun <E : KryptonEntity> nearbyEntitiesOfType(position: Position, range: Double, target: EntityTypeTarget<E>, query: Consumer<E>)

    /**
     * Gets all entities within range of the position, and calls the query consumer on them.
     */
    fun nearbyEntities(position: Position, range: Double, query: Consumer<KryptonEntity>) {
        nearbyEntitiesOfType(position, range, EntityTypeTarget.ENTITIES, query)
    }

    /**
     * Gets all entities within range of the position, returning all found entities.
     */
    fun nearbyEntities(position: Position, range: Double): Collection<KryptonEntity> {
        val entities = ArrayList<KryptonEntity>()
        nearbyEntities(position, range) { entities.add(it) }
        return entities
    }

    /**
     * Gets all entities matching the target.
     */
    fun <E : KryptonEntity> entitiesOfType(target: EntityTypeTarget<E>): Set<E>

    /**
     * Gets all entities of the given type matching the predicate, if given.
     */
    fun <E : Entity> entitiesOfType(type: Class<E>, predicate: Predicate<E>?): Collection<E>

    /**
     * Gets all entities tracked by the tracker.
     *
     * This will NOT include players!
     */
    fun entities(): Set<KryptonEntity> = entitiesOfType(EntityTypeTarget.ENTITIES)
}
