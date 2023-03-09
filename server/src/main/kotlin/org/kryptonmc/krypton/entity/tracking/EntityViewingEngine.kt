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

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.Collections
import java.util.function.Consumer

/**
 * This engine keeps track of what players are viewing an entity, and what entities an entity can see.
 *
 * This is primarily based off of Minestom's `ViewEngine`, with the manual viewing completely stripped
 * out, as it is not something we want to support in the API, and there is no way we would want it in
 * the backend.
 */
class EntityViewingEngine(private val entity: KryptonEntity) {

    // The lock to use when synchronizing operations on the engine.
    // This is a separate field to make it easier for us to change the lock if we want to, and
    // to make it clear we are synchronizing on a lock.
    private val mutex: Any = this
    // A view of all viewers of the entity.
    private val viewers = SetImpl()
    // The currently known location of the entity.
    @Volatile
    private var trackedLocation: TrackedLocation? = null

    // A tracker for all players that are viewing this entity.
    private val viewable: Tracker<KryptonPlayer> = Tracker({ player ->
        val lock1 = if (player.id < entity.id) player else entity
        val lock2 = if (lock1 === entity) player else entity
        synchronized(lock1.viewingEngine.mutex) {
            synchronized(lock2.viewingEngine.mutex) {
                entity.viewingEngine.viewable.register(player)
                player.viewingEngine.viewing.register(entity)
            }
        }
        entity.showToViewer(player)
    }, { player ->
        val lock1 = if (player.id < entity.id) player else entity
        val lock2 = if (lock1 === entity) player else entity
        synchronized(lock1.viewingEngine.mutex) {
            synchronized(lock2.viewingEngine.mutex) {
                entity.viewingEngine.viewable.unregister(player)
                player.viewingEngine.viewing.unregister(entity)
            }
        }
        entity.hideFromViewer(player)
    })
    // A tracker for all entities that this entity is viewing (primarily for players).
    private val viewing: Tracker<KryptonEntity> = Tracker(
        if (entity is KryptonPlayer) Consumer { it.viewingEngine.viewable.addition!!.accept(entity) } else null,
        if (entity is KryptonPlayer) Consumer { it.viewingEngine.viewable.removal!!.accept(entity) } else null
    )

    fun viewers(): Set<KryptonPlayer> = viewers

    /**
     * Call to indicate that the entity has moved between worlds or to a different position.
     */
    fun updateTracker(world: KryptonWorld, position: Position) {
        trackedLocation = TrackedLocation(world, position)
    }

    /**
     * Should be called when the entity enters the view of this entity.
     *
     * As all viewing is automatic, this should not need to be called outside of the
     * tracking view callback within the entity.
     */
    fun handleEnterView(entity: KryptonEntity) {
        handleViewUpdate(entity, viewing.addition, viewable.addition)
    }

    /**
     * Should be called when the entity exits the view of this entity.
     *
     * As all viewing is automatic, this should not need to be called outside of the
     * tracking view callback within the entity.
     */
    fun handleExitView(entity: KryptonEntity) {
        handleViewUpdate(entity, viewing.removal, viewable.removal)
    }

    private fun handleViewUpdate(entity: KryptonEntity, viewer: Consumer<KryptonEntity>?, viewable: Consumer<KryptonPlayer>?) {
        if (this.entity is KryptonPlayer) viewer?.accept(entity)
        if (entity is KryptonPlayer) viewable?.accept(entity)
    }

    @JvmRecord
    private data class TrackedLocation(val world: KryptonWorld, val position: Position)

    private class Tracker<E : KryptonEntity>(val addition: Consumer<E>?, val removal: Consumer<E>?) {

        // Contains all the entity IDs that are viewable by this tracker
        val ids: IntSet = IntOpenHashSet()

        fun isRegistered(entity: E): Boolean = ids.contains(entity.id)

        fun register(entity: E) {
            ids.add(entity.id)
        }

        fun unregister(entity: E) {
            ids.remove(entity.id)
        }
    }

    private inner class SetImpl : AbstractSet<KryptonPlayer>() {

        override val size: Int
            get() = synchronized(mutex) { viewable.ids.size }

        override fun isEmpty(): Boolean = synchronized(mutex) { viewable.ids.isEmpty() }

        override fun contains(element: KryptonPlayer): Boolean = synchronized(mutex) { viewable.isRegistered(element) }

        override fun iterator(): Iterator<KryptonPlayer> {
            val players: MutableList<KryptonPlayer>
            synchronized(mutex) {
                val bitSet = viewable.ids
                if (bitSet.isEmpty()) return Collections.emptyIterator()

                players = ArrayList(bitSet.size)
                val iterator = bitSet.iterator()
                while (iterator.hasNext()) {
                    val id = iterator.nextInt()
                    val player = entity.world.entityManager.getById(id) as? KryptonPlayer
                    if (player != null) players.add(player)
                }
            }
            return players.iterator()
        }
    }
}
