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
package org.kryptonmc.krypton.registry

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Iterators
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.tags.KryptonTagSet
import org.kryptonmc.krypton.util.IdentityHashStrategy
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.serialization.DataResult
import java.util.Collections
import java.util.IdentityHashMap
import java.util.Objects
import java.util.OptionalInt
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.math.max

/**
 * A simple registry implementation that provides all of the required functionality of KryptonRegistry.
 */
open class KryptonSimpleRegistry<T>(
    key: ResourceKey<out Registry<T>>,
    /**
     * A function that provides a custom holder reference for a value. Used for registries where all the holders
     * are intrusive.
     */
    private val customHolderProvider: Function<T, Holder.Reference<T>>?
) : WritableRegistry<T>(key) {

    // A map of IDs to holder references. This is an array list because it saves space and is generally faster, as arrays are O(1) to access
    // and they are indexed by integers, so the list doesn't have to store the keys.
    private val byId = ObjectArrayList<Holder.Reference<T>>(256)
    // A map of holder references to IDs. This is an identity hash map as only one holder reference will exist for each value, so we should use
    // reference equality to allow for holders with the same key/value but different identities to be stored in the map.
    private val toId = Object2IntOpenCustomHashMap(IdentityHashStrategy.get<T>()).apply { defaultReturnValue(-1) }
    // A map of namespaced keys to holder references. Allows lookups by namespaced key.
    private val byLocation = HashMap<Key, Holder.Reference<T>>()
    // A map of resource keys to holder references. Allows lookups by resource key.
    private val byKey = HashMap<ResourceKey<T>, Holder.Reference<T>>()
    // A map of values to holder references. Allows finding the namespaced or resource key for a value.
    private val byValue = IdentityHashMap<T, Holder.Reference<T>>()

    // The backing map holding the tags in this registry, where a tag is a set of values associated with a tag key.
    @Volatile
    private var tagMap: MutableMap<TagKey<T>, KryptonTagSet<T>> = IdentityHashMap()
    // A cache of all the intrusive reference holders, to allow reusing them for identical values.
    // This is lazily initialized as not all registries have intrusive references.
    private var intrusiveHolderCache: MutableMap<T, Holder.Reference<T>>? = null
    // A cache of the holders in this registry in order of registration. This is lazily initialized as it may not be necessary.
    private var holdersInOrder: List<Holder.Reference<T>>? = null
    // Indicates whether this registry can be written to. This allows us to lock the registry to avoid any changes being made to it, which
    // may break the general contract of the registry.
    private var frozen = false
    // The ID to be used for registering the next value.
    private var nextId = 0

    /*
     * Base API implementation properties. These are all delegates to internals above, as all collections/maps must be
     * unmodifiable to avoid the client doing a sneaky and downcasting the types to mutable types then mutating them,
     * which would completely break the API, as it is designed to control when registration can occur.
     */

    override val keys: Set<Key>
        get() = Collections.unmodifiableSet(byLocation.keys)
    override val registryKeys: Set<ResourceKey<T>>
        get() = Collections.unmodifiableSet(byKey.keys)
    override val entries: Set<Map.Entry<ResourceKey<T>, T>>
        get() = Collections.unmodifiableSet(Maps.transformValues(byKey, Holder<T>::value).entries)
    override val size: Int
        get() = byKey.size
    override val tagKeys: Set<TagKey<T>>
        get() = Collections.unmodifiableSet(tags.keys)
    override val tags: Map<TagKey<T>, TagSet<T>>
        get() = Collections.unmodifiableMap(tagMap)

    /**
     * Gets a list of the reference holders in this registry in the order they were registered.
     *
     * This list is cached to avoid having to recreate it every time it is requested. It is not permanent as it
     * may need to be reinitialized if values are registered to the registry after it is initially constructed.
     */
    private fun holdersInOrder(): List<Holder.Reference<T>> {
        if (holdersInOrder == null) holdersInOrder = byId.stream().filter(Objects::nonNull).toList()
        return holdersInOrder!!
    }

    private fun validateWrite(key: ResourceKey<T>) {
        if (frozen) error("Registry is already frozen! Cannot write to it! Requested key: $key")
    }

    /*
     * Writable registry API implementation methods.
     */

    override fun register(id: Int, key: ResourceKey<T>, value: T): Holder<T> = register(id, key, value, true)

    override fun register(key: ResourceKey<T>, value: T): Holder<T> = register(nextId, key, value)

    override fun registerOrOverride(id: OptionalInt, key: ResourceKey<T>, value: T): Holder<T> {
        validateWrite(key)
        val existing = byKey.get(key)
        val existingValue = if (existing != null && existing.isBound) existing.value() else null

        val actualId: Int
        if (existingValue == null) {
            actualId = id.orElse(nextId)
        } else {
            actualId = toId.getInt(existingValue)
            if (id.isPresent && id.asInt != actualId) error("ID mismatch! Provided: ${id.asInt}, Expected: $actualId (Registry: ${this.key})")
            toId.removeInt(existingValue)
            byValue.remove(existingValue)
        }
        return register(actualId, key, value, false)
    }

    private fun register(id: Int, key: ResourceKey<T>, value: T, logDuplicateKeys: Boolean): Holder<T> {
        validateWrite(key)
        byId.size(max(byId.size, id + 1))
        toId.put(value, id)
        holdersInOrder = null

        if (logDuplicateKeys && byKey.containsKey(key)) LOGGER.error("Adding duplicate key $key to registry ${this.key}!")
        if (byValue.containsKey(value)) LOGGER.error("Adding duplicate value $value to registry ${this.key}!")
        if (nextId <= id) nextId = id + 1

        val holder: Holder.Reference<T>
        if (customHolderProvider != null) {
            holder = customHolderProvider.apply(value)
            val existing = byKey.put(key, holder)
            if (existing != null && existing != holder) error("Invalid holder present for key $key in registry ${this.key}!")
        } else {
            holder = byKey.computeIfAbsent(key) { Holder.Reference.standalone(this, it) }
        }
        byLocation.put(key.location, holder)
        byValue.put(value, holder)
        holder.bind(key, value)
        byId.set(id, holder)
        return holder
    }

    /*
     * Base API implementation methods.
     */

    override fun containsKey(key: Key): Boolean = byLocation.containsKey(key)

    override fun containsKey(key: ResourceKey<T>): Boolean = byKey.containsKey(key)

    override fun getKey(value: T): Key? = byValue.get(value)?.run { key().location }

    override fun getResourceKey(value: T): ResourceKey<T>? = byValue.get(value)?.key()

    override fun getId(value: T): Int = toId.getInt(value)

    override fun get(key: Key): T? = byLocation.get(key)?.value()

    override fun get(key: ResourceKey<T>): T? = byKey.get(key)?.value()

    override fun get(id: Int): T? {
        if (id < 0 || id >= byId.size) return null
        return byId.get(id)?.value()
    }

    /*
     * Holder API implementation methods.
     */

    override fun holders(): Stream<Holder.Reference<T>> = holdersInOrder().stream()

    override fun getHolder(id: Int): Holder<T>? {
        if (id < 0 || id >= byId.size) return null
        return byId.get(id)
    }

    override fun getHolder(key: ResourceKey<T>): Holder<T>? = byKey.get(key)

    override fun getOrCreateHolder(key: ResourceKey<T>): DataResult<Holder<T>> {
        var holder = byKey.get(key)
        if (holder == null) {
            if (customHolderProvider != null) return DataResult.error("This registry cannot create new holders without a value! Requested key: $key")
            if (frozen) return DataResult.error("Registry is already frozen! Requested key: $key")
            holder = Holder.Reference.standalone(this, key)
            byKey.put(key, holder)
        }
        return DataResult.success(holder)
    }

    override fun getOrCreateHolderOrThrow(key: ResourceKey<T>): Holder<T> = byKey.computeIfAbsent(key) {
        check(customHolderProvider == null) { "This registry cannot create new holders without a value! Requested key: $key" }
        validateWrite(it)
        Holder.Reference.standalone(this, it)
    }

    override fun createIntrusiveHolder(value: T): Holder.Reference<T> {
        require(customHolderProvider != null) { "This registry cannot create intrusive holders!" }
        if (!frozen && intrusiveHolderCache != null) return intrusiveHolderCache!!.computeIfAbsent(value) { Holder.Reference.intrusive(this, it) }
        error("Registry is already frozen!")
    }

    /*
     * Tag API implementation methods.
     */

    override fun getTag(key: TagKey<T>): TagSet<T>? = tagMap.get(key)

    override fun getOrCreateTag(key: TagKey<T>): TagSet<T> {
        var tag = tagMap.get(key)
        if (tag == null) {
            tag = createTagSet(key)
            tagMap = IdentityHashMap(tagMap).apply { put(key, tag) }
        }
        return tag
    }

    override fun isKnownTagKey(key: TagKey<T>): Boolean = tagMap.containsKey(key)

    override fun resetTags() {
        tagMap.values.forEach { it.bind(ImmutableList.of()) }
        byKey.values.forEach { it.bindTags(ImmutableSet.of()) }
    }

    override fun bindTags(tags: Map<TagKey<T>, List<Holder<T>>>) {
        val tagsMap = IdentityHashMap<Holder.Reference<T>, MutableList<TagKey<T>>>()
        byKey.values.forEach { tagsMap.put(it, ArrayList()) }
        tags.forEach { (key, holders) ->
            holders.forEach { holder ->
                if (!holder.isValidIn(this)) error("Cannot create tag set $key containing value $holder from outside registry $this!")
                if (holder !is Holder.Reference<*>) error("Found direct holder $holder value in tag $key!")
                tagsMap.get(holder as Holder.Reference<T>)!!.add(key)
            }
        }
        val difference = Sets.difference(tagMap.keys, tagsMap.keys)
        if (!difference.isEmpty()) {
            val missing = difference.stream().map { it.location.asString() }.sorted().collect(Collectors.joining(", "))
            LOGGER.warn("Not all defined tags for registry $key are present in data pack: $missing")
        }
        val tagsCopy = IdentityHashMap(tagMap)
        tags.forEach { (key, holders) -> tagsCopy.computeIfAbsent(key, ::createTagSet).bindHolders(holders) }
        tagsMap.forEach(Holder.Reference<T>::bindTags)
        tagMap = tagsCopy
    }

    override fun tagNames(): Stream<TagKey<T>> = tagMap.keys.stream()

    private fun createTagSet(key: TagKey<T>): KryptonTagSet<T> = KryptonTagSet(this, key)

    override fun iterator(): Iterator<T> = Iterators.transform(holdersInOrder().iterator(), Holder<T>::value)

    companion object {

        private val LOGGER = logger<KryptonSimpleRegistry<*>>()
    }
}
