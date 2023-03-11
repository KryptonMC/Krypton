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

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.ChunkUtil
import space.vectrix.flare.fastutil.Int2ObjectSyncMap
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * The default implementation of the entity tracker, used by the server.
 *
 * Separating out the specification from the implementation lets us easily swap out the implementation
 * if we want to, without having to change all the call sites.
 */
class DefaultEntityTracker(private val entityViewDistance: Int) : EntityTracker {

    private val entries = EntityTypeTarget.VALUES.map { TargetEntry(it) }.toTypedArray()
    private val entityPositions = Int2ObjectSyncMap.hashmap<Position>()

    override fun <E : KryptonEntity> add(entity: KryptonEntity, position: Position, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>?) {
        val previousPosition = entityPositions.putIfAbsent(entity.id, position)
        if (previousPosition != null) return

        val index = ChunkPos.pack(position.chunkX(), position.chunkZ())
        for (entry in entries) {
            if (!entry.target.type.isInstance(entity)) continue
            entry.entities.add(entity)
            entry.addToChunk(index, entity)
        }

        if (callback == null) return
        callback.referenceUpdate(position, this)
        nearbyChunkEntities(position.chunkX(), position.chunkZ(), entityViewDistance, target) { newEntity ->
            if (newEntity === entity) return@nearbyChunkEntities
            callback.add(newEntity)
        }
    }

    override fun <E : KryptonEntity> remove(entity: KryptonEntity, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>?) {
        val position = entityPositions.remove(entity.id) ?: return

        val index = ChunkPos.pack(position.chunkX(), position.chunkZ())
        for (entry in entries) {
            if (!entry.target.type.isInstance(entity)) continue
            entry.entities.remove(entity)
            entry.removeFromChunk(index, entity)
        }

        if (callback == null) return
        callback.referenceUpdate(position, this)
        nearbyChunkEntities(position.chunkX(), position.chunkZ(), entityViewDistance, target) { newEntity ->
            if (newEntity === entity) return@nearbyChunkEntities
            callback.remove(newEntity)
        }
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // Required to avoid conflicts
    override fun <E : KryptonEntity> onMove(entity: KryptonEntity, newPosition: Position, target: EntityTypeTarget<E>,
                                            callback: EntityViewCallback<E>?) {
        val oldPosition = entityPositions.put(entity.id, newPosition)
        if (oldPosition == null || oldPosition.chunkX() == newPosition.chunkX() && oldPosition.chunkZ() == newPosition.chunkZ()) {
            // If the chunk position hasn't changed, we don't need to do any view updating, and we're done.
            return
        }

        val oldIndex = ChunkPos.pack(oldPosition.chunkX(), oldPosition.chunkZ())
        val newIndex = ChunkPos.pack(newPosition.chunkX(), newPosition.chunkZ())
        for (entry in entries) {
            if (!entry.target.type.isInstance(entity)) continue
            entry.addToChunk(newIndex, entity)
            entry.removeFromChunk(oldIndex, entity)
        }

        if (callback == null) return
        difference(oldPosition, newPosition, target, object : EntityViewCallback<E> {
            override fun add(added: E) {
                if (entity !== added) callback.add(added)
            }

            override fun remove(removed: E) {
                if (entity !== removed) callback.remove(removed)
            }
        })
        callback.referenceUpdate(newPosition, this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : KryptonEntity> entitiesInChunkOfType(position: ChunkPos, target: EntityTypeTarget<E>): Collection<E> {
        val entry = entries[target.ordinal]
        val chunkEntities = entry.getByChunk(position.pack()) as List<E>
        return Collections.unmodifiableCollection(chunkEntities)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : Entity> entitiesInChunkOfType(position: ChunkPos, type: Class<E>, predicate: Predicate<E>?): Collection<E> {
        val entry = entries[EntityTypeTarget.ENTITIES.ordinal]
        val chunkEntities = entry.getByChunk(position.pack())
        val result = ArrayList<E>()
        for (entity in chunkEntities) {
            if (!type.isInstance(entity)) continue
            entity as E
            if (predicate == null || predicate.test(entity)) result.add(entity)
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : KryptonEntity> nearbyChunkEntities(chunkX: Int, chunkZ: Int, chunkRange: Int, target: EntityTypeTarget<E>, query: Consumer<E>) {
        val entities = entries[target.ordinal].chunkEntities
        if (chunkRange == 0) {
            val chunkEntities = entities.get(ChunkPos.pack(chunkX, chunkZ)) as? List<E>
            if (!chunkEntities.isNullOrEmpty()) chunkEntities.forEach(query)
            return
        }
        ChunkUtil.forChunksInRange(chunkX, chunkZ, chunkRange) { x, z ->
            val chunkEntities = entities.get(ChunkPos.pack(x, z)) as? List<E>
            if (chunkEntities.isNullOrEmpty()) return@forChunksInRange
            chunkEntities.forEach(query)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : KryptonEntity> nearbyEntitiesOfType(position: Position, range: Double, target: EntityTypeTarget<E>, query: Consumer<E>) {
        val entities = entries[target.ordinal].chunkEntities
        val minChunkX = SectionPos.blockToSection(position.x - range)
        val minChunkZ = SectionPos.blockToSection(position.z - range)
        val maxChunkX = SectionPos.blockToSection(position.x + range)
        val maxChunkZ = SectionPos.blockToSection(position.z + range)
        val squaredRange = range * range

        if (minChunkX == maxChunkX && minChunkZ == maxChunkZ) {
            val chunkEntities = entities.get(ChunkPos.pack(position.chunkX(), position.chunkZ())) as? List<E>
            if (chunkEntities.isNullOrEmpty()) return
            chunkEntities.forEach { entity ->
                val otherPosition = entityPositions.get(entity.id)
                if (position.distanceSquared(otherPosition) <= squaredRange) query.accept(entity)
            }
            return
        }

        val chunkRange = SectionPos.blockToSection(range) + 1
        ChunkUtil.forChunksInRange(position.chunkX(), position.chunkZ(), chunkRange) { x, z ->
            val chunkEntities = entities.get(ChunkPos.pack(x, z)) as? List<E>
            if (chunkEntities.isNullOrEmpty()) return@forChunksInRange
            chunkEntities.forEach { entity ->
                val otherPosition = entityPositions.get(entity.id)
                if (position.distanceSquared(otherPosition) <= squaredRange) query.accept(entity)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : KryptonEntity> entitiesOfType(target: EntityTypeTarget<E>): Set<E> = entries[target.ordinal].entitiesView as Set<E>

    @Suppress("UNCHECKED_CAST")
    override fun <E : Entity> entitiesOfType(type: Class<E>, predicate: Predicate<E>?): Collection<E> {
        val result = ArrayList<E>()
        val entry = entries[EntityTypeTarget.ENTITIES.ordinal]
        for (entity in entry.entitiesView) {
            if (!type.isInstance(entity)) continue
            entity as E
            if (predicate == null || predicate.test(entity)) result.add(entity)
        }
        return result
    }

    /**
     * Calculates the view difference between the two positions, adds any entities that came in to view, and removes
     * any entities that went out of view.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <E : KryptonEntity> difference(old: Position, new: Position, target: EntityTypeTarget<E>, callback: EntityViewCallback<E>) {
        val entry = entries[target.ordinal]
        ChunkUtil.forDifferingChunksInRange(new.chunkX(), new.chunkZ(), old.chunkX(), old.chunkZ(), entityViewDistance, { x, z ->
            // Entities come in to view
            val entities = entry.chunkEntities.get(ChunkPos.pack(x, z))
            if (entities.isNullOrEmpty()) return@forDifferingChunksInRange
            for (entity in entities) {
                callback.add(entity as E)
            }
        }, { x, z ->
            // Entities go out of view
            val entities = entry.chunkEntities.get(ChunkPos.pack(x, z))
            if (entities.isNullOrEmpty()) return@forDifferingChunksInRange
            for (entity in entities) {
                callback.remove(entity as E)
            }
        })
    }

    /**
     * Holder for the entities for a specific target.
     */
    private class TargetEntry<E : KryptonEntity>(val target: EntityTypeTarget<E>) {

        val entities: MutableSet<E> = ConcurrentHashMap.newKeySet()
        val entitiesView: Collection<E> = Collections.unmodifiableSet(entities)
        val chunkEntities: Long2ObjectSyncMap<MutableList<E>> = Long2ObjectSyncMap.hashmap()

        fun getByChunk(index: Long): MutableList<E> = chunkEntities.computeIfAbsent(index, Long2ObjectFunction { CopyOnWriteArrayList() })

        fun addToChunk(index: Long, entity: E) {
            getByChunk(index).add(entity)
        }

        fun removeFromChunk(index: Long, entity: E) {
            chunkEntities.get(index)?.remove(entity)
        }
    }

    companion object {

        // Used by EntityTypeTarget for ordinal values.
        @JvmField
        val TARGET_COUNTER: AtomicInteger = AtomicInteger()
    }
}
