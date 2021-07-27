/*
 * This file is part of the Krypton project, and originates from the Paper project.
 *
 * The patch parts of this originate from is licensed under the terms of the MIT license,
 * and this file is licensed under the terms of the GNU General Public License v3.0.
 *
 * Copyright (C) PaperMC
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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import kotlin.math.max

/** @author SpottedLeaf */
abstract class AreaMap<E>(
    private val pooledHashSets: PooledLinkedHashSets<E> = PooledLinkedHashSets(),
    private val addCallback: ChangeCallback<E>? = null,
    private val removeCallback: ChangeCallback<E>? = null,
    private val changeSourceCallback: ChangeSourceCallback<E>? = null
) {

    /* Tested via https://gist.github.com/Spottedleaf/520419c6f41ef348fe9926ce674b7217 */

    // we use linked for better iteration.
    // map of: coordinate to set of objects in coordinate
    private val areaMap = Long2ObjectOpenHashMap<PooledObjectLinkedOpenHashSet<E>>(1024, 0.7F)
    private val objectToLastCoordinate = Object2LongOpenHashMap<E>().apply { defaultReturnValue(Long.MIN_VALUE) }
    private val objectToViewDistance = Object2IntOpenHashMap<E>().apply { defaultReturnValue(-1) }

    // returns the total number of mapped chunks
    val size: Int
        get() = areaMap.size

    protected abstract fun emptySetFor(element: E): PooledObjectLinkedOpenHashSet<E>

    fun inRange(key: Long): PooledObjectLinkedOpenHashSet<E>? = areaMap[key]

    fun inRange(position: ChunkPosition): PooledObjectLinkedOpenHashSet<E>? = areaMap[(position.z.toLong() shl 32) or (position.x.toLong() and 0xFFFFFFFFL)]

    fun inRange(chunkX: Int, chunkZ: Int): PooledObjectLinkedOpenHashSet<E>? = areaMap[(chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)]

    // Long.MIN_VALUE indicates the object is not mapped
    fun lastCoordinate(element: E) = objectToLastCoordinate.getLong(element)

    // -1 indicates the object is not mapped
    fun lastViewDistance(element: E) = objectToViewDistance.getInt(element)

    fun addOrUpdate(element: E, chunkX: Int, chunkZ: Int, viewDistance: Int) {
        val oldViewDistance = objectToViewDistance.put(element, viewDistance)
        val newPos = (chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)
        val oldPos = objectToLastCoordinate.put(element, newPos)
        if (oldViewDistance == -1) {
            add(element, chunkX, chunkZ, Int.MIN_VALUE, Int.MIN_VALUE, viewDistance)
        } else {
            update(element, oldPos, newPos, oldViewDistance, viewDistance)
            updateCallback(element, oldPos, newPos)
        }
    }

    fun add(element: E, chunkX: Int, chunkZ: Int, viewDistance: Int): Boolean {
        val oldViewDistance = objectToViewDistance.putIfAbsent(element, viewDistance)
        if (oldViewDistance != -1) return false
        val newPos = (chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)
        objectToLastCoordinate[element] = newPos
        add(element, chunkX, chunkZ, Int.MIN_VALUE, Int.MIN_VALUE, viewDistance)
        return true
    }

    fun remove(element: E): Boolean {
        val position = objectToLastCoordinate.removeLong(element)
        val viewDistance = objectToViewDistance.removeInt(element)
        if (viewDistance == -1) return false
        val currentX = position.toInt()
        val currentZ = (position ushr 32).toInt()
        remove(element, currentX, currentZ, currentX, currentZ, viewDistance)
        return true
    }

    fun update(element: E, chunkX: Int, chunkZ: Int, viewDistance: Int): Boolean {
        val oldViewDistance = objectToViewDistance.replace(element, viewDistance)
        if (oldViewDistance == -1) return false
        val newPos = (chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)
        val oldPos = objectToLastCoordinate.put(element, newPos)
        update(element, oldPos, newPos, oldViewDistance, viewDistance)
        updateCallback(element, oldPos, newPos)
        return true
    }

    private fun add(element: E, chunkX: Int, chunkZ: Int, previousChunkX: Int, previousChunkZ: Int, viewDistance: Int) {
        val maxX = chunkX + viewDistance
        val maxZ = chunkZ + viewDistance
        val minX = chunkX - viewDistance
        val minZ = chunkZ - viewDistance
        for (x in minX..maxX) {
            for (z in minZ..maxZ) addTo(element, x, z, chunkX, chunkZ, previousChunkX, previousChunkZ)
        }
    }

    private fun remove(element: E, chunkX: Int, chunkZ: Int, currentChunkX: Int, currentChunkZ: Int, viewDistance: Int) {
        val maxX = chunkX + viewDistance
        val maxZ = chunkZ + viewDistance
        val minX = chunkX - viewDistance
        val minZ = chunkZ - viewDistance
        for (x in minX..maxX) {
            for (z in minZ..maxZ) removeFrom(element, x, z, currentChunkX, currentChunkZ, chunkX, chunkZ)
        }
    }

    private fun update(element: E, oldPosition: Long, newPosition: Long, oldViewDistance: Int, newViewDistance: Int) {
        val toX = newPosition.toInt()
        val toZ = (newPosition ushr 32).toInt()
        val fromX = oldPosition.toInt()
        val fromZ = (oldPosition ushr 32).toInt()
        val dx = toX - fromX
        val dz = toZ - fromZ
        val totalX = (fromX - toX).branchlessAbs()
        val totalZ = (fromZ - toZ).branchlessAbs()
        if (max(totalX, totalZ) > (2 * max(newViewDistance, oldViewDistance))) {
            // teleported?
            remove(element, fromX, fromZ, fromX, fromZ, oldViewDistance)
            add(element, toX, toZ, fromX, fromZ, newViewDistance)
            return
        }
        if (oldViewDistance != newViewDistance) {
            // remove loop
            val oldMinX = fromX - oldViewDistance
            val oldMinZ = fromZ - oldViewDistance
            val oldMaxX = fromX + oldViewDistance
            val oldMaxZ = fromZ + oldViewDistance
            for (currX in oldMinX..oldMaxX) {
                // only remove if we're outside the new view distance...
                for (currZ in oldMinZ..oldMaxZ) if (max((currX - toX).branchlessAbs(), (currZ - toZ).branchlessAbs()) > newViewDistance) removeFrom(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
            // add loop
            val newMinX = toX - newViewDistance
            val newMinZ = toZ - newViewDistance
            val newMaxX = toX + newViewDistance
            val newMaxZ = toZ + newViewDistance
            for (currX in newMinX..newMaxX) {
                // only add if we're outside the old view distance...
                for (currZ in newMinZ..newMaxZ) if (max((currX - fromX).branchlessAbs(), (currZ - fromZ)) > oldViewDistance) addTo(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
            return
        }
        // x axis is width
        // z axis is height
        // right refers to the x axis of where we moved
        // top refers to the z axis of where we moved

        // same view distance

        // used for relative positioning
        val up = dz.sign() // 1 if dz >= 0, -1 otherwise
        val right = dx.sign() // 1 if dx >= 0, -1 otherwise

        // The area excluded by overlapping the two view distance squares creates four rectangles:
        // Two on the left, and two on the right. The ones on the left we consider the "removed" section
        // and on the right the "added" section.
        // https://i.imgur.com/MrnOBgI.png is a reference image. Note that the outside border is not actually
        // exclusive to the regions they surround.

        // 4 points of the rectangle
        var maxX: Int // exclusive
        var minX: Int // inclusive
        var maxZ: Int // exclusive
        var minZ: Int // inclusive

        if (dx != 0) {
            // handle right addition
            maxX = toX + (oldViewDistance * right) + right // exclusive
            minX = fromX + (oldViewDistance * right) + right // inclusive
            maxZ = fromZ + (oldViewDistance * up) + up // exclusive
            minZ = toZ - (oldViewDistance * up) // inclusive
            for (currX in minX..maxX step right) {
                for (currZ in minZ..maxZ step up) addTo(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
        }

        if (dz != 0) {
            // handle up addition
            maxX = toX + (oldViewDistance * right) + right // exclusive
            minX = toX - (oldViewDistance * right) // inclusive
            maxZ = toZ + (oldViewDistance * up) + up // exclusive
            minZ = fromZ + (oldViewDistance * up) + up // inclusive
            for (currX in minX..maxX step right) {
                for (currZ in minZ..maxZ step up) addTo(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
        }

        if (dx != 0) {
            // handle left removal
            maxX = toX - oldViewDistance * right // exclusive
            minX = fromX - oldViewDistance * right // inclusive
            maxZ = fromZ + oldViewDistance * up + up // exclusive
            minZ = toZ - oldViewDistance * up // inclusive
            for (currX in minX..maxX step right) {
                for (currZ in minZ..maxZ step up) removeFrom(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
        }

        if (dz != 0) {
            // handle down removal
            maxX = fromX + oldViewDistance * right + right // exclusive
            minX = fromX - oldViewDistance * right // inclusive
            maxZ = toZ - oldViewDistance * up // exclusive
            minZ = fromZ - oldViewDistance * up // inclusive
            for (currX in minX..maxX step right) {
                for (currZ in minZ..maxZ step up) removeFrom(element, currX, currZ, toX, toZ, fromX, fromZ)
            }
        }
    }

    private fun addTo(element: E, chunkX: Int, chunkZ: Int, currentChunkX: Int, currentChunkZ: Int, previousChunkX: Int, previousChunkZ: Int) {
        val key = (chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)
        val empty = emptySetFor(element)
        var current = areaMap.putIfAbsent(key, empty)
        if (current != null) {
            val next = pooledHashSets.findMapWith(current, element)
            if (next === current) error("Expected different map, got $next!")
            areaMap[key] = next
            current = next
        } else current = empty
        addCallback?.let {
            try {
                it(element, chunkX, chunkZ, currentChunkX, currentChunkZ, previousChunkX, previousChunkZ, current)
            } catch (exception: Throwable) {
                if (exception is ThreadDeath) throw exception
                LOGGER.error("Add callback for area map threw exception!", exception)
            }
        }
    }

    private fun removeFrom(element: E, chunkX: Int, chunkZ: Int, currentChunkX: Int, currentChunkZ: Int, previousChunkX: Int, previousChunkZ: Int) {
        val key = (chunkZ.toLong() shl 32) or (chunkX.toLong() and 0xFFFFFFFFL)
        val current = checkNotNull(areaMap[key]) { "Current map may not be null for $element, ($chunkX, $chunkZ)" }
        val next = pooledHashSets.findMapWithout(current, element)
        if (next === current) error("Current map [$next] should have contained $element, ($chunkX, $chunkZ)")
        if (next != null) areaMap[key] = next else areaMap.remove(key)
        removeCallback?.let {
            try {
                it(element, chunkX, chunkZ, currentChunkX, currentChunkZ, previousChunkX, previousChunkZ, next)
            } catch (exception: Throwable) {
                if (exception is ThreadDeath) throw exception
                LOGGER.error("Add callback for area map threw exception!", exception)
            }
        }
    }

    // called after the distance map updates
    private fun updateCallback(element: E, oldPosition: Long, newPosition: Long) {
        if (newPosition != oldPosition) changeSourceCallback?.let { it(element, oldPosition, newPosition) }
    }

    fun interface ChangeCallback<E> {

        operator fun invoke(element: E, rangeX: Int, rangeZ: Int, currentX: Int, currentZ: Int, previousX: Int, previousZ: Int, newState: PooledObjectLinkedOpenHashSet<E>?)
    }

    fun interface ChangeSourceCallback<E> {

        operator fun invoke(element: E, previous: Long, new: Long)
    }

    companion object {

        private val LOGGER = logger<AreaMap<*>>()
    }
}
