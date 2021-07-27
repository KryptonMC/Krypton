/*
 * This file is part of the Krypton project, and originates from the Paper project.
 *
 * The patch this originates from is licensed under the terms of the MIT license,
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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.lang.ref.WeakReference

/** @author Spottedleaf */
class PooledLinkedHashSets<E> {

    /* Tested via https://gist.github.com/Spottedleaf/a93bb7a8993d6ce142d3efc5932bf573 */

    // we really want to avoid that equals() check as much as possible...
    private val mapPool = Object2ObjectOpenHashMap<PooledObjectLinkedOpenHashSet<E>, PooledObjectLinkedOpenHashSet<E>>(128, 0.25f)

    private fun decrementReferenceCount(current: PooledObjectLinkedOpenHashSet<E>) {
        check(current.referenceCount != 0) { "Cannot decrement reference count for $current" }
        if (current.referenceCount == -1 || --current.referenceCount > 0) return
        mapPool.remove(current)
    }

    fun findMapWith(current: PooledObjectLinkedOpenHashSet<E>, element: E): PooledObjectLinkedOpenHashSet<E> {
        val cached = current.getAddCache(element)
        if (cached != null) {
            decrementReferenceCount(current)
            if (cached.referenceCount == 0) {
                // bring the map back from the dead
                val contending = mapPool.putIfAbsent(cached, cached)
                if (contending != null) {
                    // a map already exists with the elements we want
                    if (contending.referenceCount != -1) {
                        ++contending.referenceCount
                    }
                    current.updateAddCache(element, contending)
                    return contending
                }
                cached.referenceCount = 1
            } else if (cached.referenceCount != -1) {
                ++cached.referenceCount
            }
            return cached
        }
        if (!current.add(element)) {
            return current
        }
        // we use get/put since we use a different key on put
        var ret = mapPool[current]
        if (ret == null) {
            ret = PooledObjectLinkedOpenHashSet(current)
            current.remove(element)
            mapPool[ret] = ret
            ret.referenceCount = 1
        } else {
            if (ret.referenceCount != -1) {
                ++ret.referenceCount
            }
            current.remove(element)
        }
        current.updateAddCache(element, ret)
        decrementReferenceCount(current)
        return ret
    }

    // rets null if current.size() == 1
    fun findMapWithout(current: PooledObjectLinkedOpenHashSet<E>, element: E): PooledObjectLinkedOpenHashSet<E>? {
        if (current.set.size == 1) {
            decrementReferenceCount(current)
            return null
        }
        val cached = current.getRemoveCache(element)
        if (cached != null) {
            decrementReferenceCount(current)
            if (cached.referenceCount == 0) {
                // bring the map back from the dead
                val contending = mapPool.putIfAbsent(cached, cached)
                if (contending != null) {
                    // a map already exists with the elements we want
                    if (contending.referenceCount != -1) {
                        ++contending.referenceCount
                    }
                    current.updateRemoveCache(element, contending)
                    return contending
                }
                cached.referenceCount = 1
            } else if (cached.referenceCount != -1) {
                ++cached.referenceCount
            }
            return cached
        }
        if (!current.remove(element)) {
            return current
        }
        // we use get/put since we use a different key on put
        var ret = mapPool[current]
        if (ret == null) {
            ret = PooledObjectLinkedOpenHashSet(current)
            current.add(element)
            mapPool[ret] = ret
            ret.referenceCount = 1
        } else {
            if (ret.referenceCount != -1) {
                ++ret.referenceCount
            }
            current.add(element)
        }
        current.updateRemoveCache(element, ret)
        decrementReferenceCount(current)
        return ret
    }
}

class RawSetObjectLinkedOpenHashSet<E>(capacity: Int, loadFactor: Float) : ObjectOpenHashSet<E>(capacity, loadFactor) {

    override fun clone() = super.clone() as RawSetObjectLinkedOpenHashSet<E>

    val rawSet: Array<E?>
        get() = key
}

@Suppress("UNCHECKED_CAST")
class PooledObjectLinkedOpenHashSet<E> {

    val set: RawSetObjectLinkedOpenHashSet<E>
    var referenceCount = 0 // -1 if special
    var hash = 0 // optimize hashcode

    // add cache
    private var lastAddObject: WeakReference<E> = NULL_REFERENCE as WeakReference<E>
    private var lastAddMap: WeakReference<PooledObjectLinkedOpenHashSet<E>> = NULL_REFERENCE as WeakReference<PooledObjectLinkedOpenHashSet<E>>

    // remove cache
    private var lastRemoveObject: WeakReference<E> = NULL_REFERENCE as WeakReference<E>
    private var lastRemoveMap: WeakReference<PooledObjectLinkedOpenHashSet<E>> = NULL_REFERENCE as WeakReference<PooledObjectLinkedOpenHashSet<E>>

    constructor(pooledSets: PooledLinkedHashSets<E>?) {
        set = RawSetObjectLinkedOpenHashSet(2, 0.8f)
    }

    constructor(single: E) : this(null) {
        referenceCount = -1
        add(single)
    }

    constructor(other: PooledObjectLinkedOpenHashSet<E>) {
        set = other.set.clone()
        hash = other.hash
    }

    @Suppress("SuspiciousEqualsCombination")
    fun getAddCache(element: E): PooledObjectLinkedOpenHashSet<E>? {
        val currentAdd = lastAddObject.get()
        return if (currentAdd == null || !(currentAdd === element || currentAdd == element)) null else lastAddMap.get()
    }

    @Suppress("SuspiciousEqualsCombination")
    fun getRemoveCache(element: E): PooledObjectLinkedOpenHashSet<E>? {
        val currentRemove = lastRemoveObject.get()
        return if (currentRemove == null || !(currentRemove === element || currentRemove == element)) null else lastRemoveMap.get()
    }

    fun updateAddCache(element: E, map: PooledObjectLinkedOpenHashSet<E>) {
        lastAddObject = WeakReference(element)
        lastAddMap = WeakReference(map)
    }

    fun updateRemoveCache(element: E, map: PooledObjectLinkedOpenHashSet<E>) {
        lastRemoveObject = WeakReference(element)
        lastRemoveMap = WeakReference(map)
    }

    fun add(element: E): Boolean {
        val added = set.add(element)
        if (added) {
            hash += hash0(element.hashCode())
        }
        return added
    }

    fun remove(element: E): Boolean {
        val removed = set.remove(element)
        if (removed) hash -= hash0(element.hashCode())
        return removed
    }

    operator fun contains(element: Any?) = set.contains(element)

    val backingSet: Array<E?>
        get() = set.rawSet
    val size: Int
        get() = set.size

    override fun hashCode() = hash

    override fun equals(other: Any?): Boolean {
        if (other !is PooledObjectLinkedOpenHashSet<*>) return false
        if (referenceCount == 0) return other === this
        if (other === this) return false // Unfortunately we are never equal to our own instance while in use!
        return hash == other.hash && set == other.set
    }

    override fun toString() = "PooledHashSet: size: ${set.size}, reference count: $referenceCount, hash: ${hashCode()}, identity: ${System.identityHashCode(this)}, map: $set"

    companion object {

        private val NULL_REFERENCE: WeakReference<*> = WeakReference<Any?>(null)

        // from https://github.com/Spottedleaf/ConcurrentUtil/blob/master/src/main/java/ca/spottedleaf/concurrentutil/util/IntegerUtil.java
        // generated by https://github.com/skeeto/hash-prospector
        private fun hash0(x: Int): Int {
            var x = x
            x *= 0x36935555
            x = x xor (x ushr 16)
            return x
        }
    }
}
