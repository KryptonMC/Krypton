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

import com.google.common.collect.Iterators
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderGetter
import org.kryptonmc.krypton.registry.holder.HolderLookup
import org.kryptonmc.krypton.registry.holder.HolderOwner
import org.kryptonmc.krypton.registry.holder.HolderSet
import org.kryptonmc.krypton.tags.KryptonTagSet
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.ImmutableSets
import java.util.Collections
import java.util.IdentityHashMap
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.math.max

/**
 * A simple registry implementation that provides all of the required functionality of KryptonRegistry.
 */
open class KryptonSimpleRegistry<T> protected constructor(override val key: ResourceKey<out Registry<T>>, intrusive: Boolean) : WritableRegistry<T> {

    // A map of IDs to holder references. This is an array list because it saves space and is generally faster, as arrays are O(1) to access
    // and they are indexed by integers, so the list doesn't have to store the keys.
    private val byId = ObjectArrayList<Holder.Reference<T>>(256)
    // A map of holder references to IDs. This is an identity hash map as only one holder reference will exist for each value, so we should use
    // reference equality to allow for holders with the same key/value but different identities to be stored in the map.
    private val toId = Reference2IntOpenHashMap<T>().apply { defaultReturnValue(-1) }
    // A map of namespaced keys to holder references. Allows lookups by namespaced key.
    private val byLocation = HashMap<Key, Holder.Reference<T>>()
    // A map of resource keys to holder references. Allows lookups by resource key.
    private val byKey = HashMap<ResourceKey<T>, Holder.Reference<T>>()
    // A map of values to holder references. Allows finding the namespaced or resource key for a value.
    private val byValue = IdentityHashMap<T, Holder.Reference<T>>()

    // The backing map holding the tags in this registry, where a tag is a set of values associated with a tag key.
    @Volatile
    private var tags: MutableMap<TagKey<T>, HolderSet.Named<T>> = IdentityHashMap()
    // A cache of all the intrusive reference holders, to allow reusing them for identical values.
    // This is lazily initialized as not all registries have intrusive references.
    private var unregisteredIntrusiveHolders: MutableMap<T, Holder.Reference<T>>? = null
    // A cache of the holders in this registry in order of registration. This is lazily initialized as it may not be necessary.
    private var holdersInOrder: List<Holder.Reference<T>>? = null
    // Indicates whether this registry can be written to. This allows us to lock the registry to avoid any changes being made to it, which
    // may break the general contract of the registry.
    private var frozen = false
    // The ID to be used for registering the next value.
    private var nextId = 0
    private val lookup = object : HolderLookup.ForRegistry<T> {
        override fun key(): ResourceKey<out Registry<out T>> = key

        override fun get(key: ResourceKey<T>): Holder.Reference<T>? = getHolder(key)

        override fun listElements(): Stream<Holder.Reference<T>> = holders()

        override fun get(key: TagKey<T>): HolderSet.Named<T>? = getTag(key)

        override fun listTags(): Stream<HolderSet.Named<T>> = tagEntries().values.stream()
    }

    init {
        if (intrusive) unregisteredIntrusiveHolders = IdentityHashMap()
    }

    /**
     * Gets a list of the reference holders in this registry in the order they were registered.
     *
     * This list is cached to avoid having to recreate it every time it is requested. It is not permanent as it
     * may need to be reinitialized if values are registered to the registry after it is initially constructed.
     */
    private fun holdersInOrder(): List<Holder.Reference<T>> {
        if (holdersInOrder == null) holdersInOrder = byId.stream().filter { it != null }.toList()
        return holdersInOrder!!
    }

    private fun validateWrite() {
        if (frozen) error("Registry is already frozen! Cannot write to it!")
    }

    private fun validateWrite(key: ResourceKey<T>) {
        if (frozen) error("Registry is already frozen! Cannot write to it! Requested key: $key")
    }

    /*
     * Writable registry API implementation methods.
     */

    override fun register(id: Int, key: ResourceKey<T>, value: T): Holder.Reference<T> {
        validateWrite(key)
        if (byLocation.containsKey(key.location)) LOGGER.warn("Adding duplicate key $key to registry ${this.key}")
        if (byValue.containsKey(value)) LOGGER.warn("Adding duplicate value $value to registry ${this.key}")

        val holder: Holder.Reference<T>
        if (unregisteredIntrusiveHolders != null) {
            holder = unregisteredIntrusiveHolders!!.remove(value) ?:
                throw AssertionError("Missing intrusive holder for $key -> $value in registry ${this.key}!")
            holder.bindKey(key)
        } else {
            holder = byKey.computeIfAbsent(key) { Holder.Reference.standalone(holderOwner(), it) }
        }

        byKey.put(key, holder)
        byLocation.put(key.location, holder)
        byValue.put(value, holder)
        byId.size(max(byId.size, id + 1))
        byId.set(id, holder)
        toId.put(value, id)
        if (nextId <= id) nextId = id + 1
        holdersInOrder = null
        return holder
    }

    override fun register(key: ResourceKey<T>, value: T): Holder.Reference<T> = register(nextId, key, value)

    /*
     * Base API implementation methods.
     */

    override fun keys(): Set<Key> = Collections.unmodifiableSet(byLocation.keys)

    override fun registryKeys(): Set<ResourceKey<T>> = Collections.unmodifiableSet(byKey.keys)

    override fun entries(): Set<Map.Entry<ResourceKey<T>, T>> = Collections.unmodifiableSet(Maps.transformValues(byKey) { it.value() }.entries)

    override fun size(): Int = byKey.size

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
     * Holder API.
     */

    override fun holders(): Stream<Holder.Reference<T>> = holdersInOrder().stream()

    override fun getHolder(id: Int): Holder.Reference<T>? {
        if (id < 0 || id >= byId.size) return null
        return byId.get(id)
    }

    override fun getHolder(key: ResourceKey<T>): Holder.Reference<T>? = byKey.get(key)

    override fun createIntrusiveHolder(value: T): Holder.Reference<T> {
        val holders = requireNotNull(unregisteredIntrusiveHolders) { "This registry cannot create intrusive holders!" }
        validateWrite()
        return holders.computeIfAbsent(value) { Holder.Reference.intrusive(asLookup(), it) }
    }

    private fun getOrCreateHolderOrThrow(key: ResourceKey<T>): Holder.Reference<T> = byKey.computeIfAbsent(key) {
        if (unregisteredIntrusiveHolders != null) error("This registry cannot create new holders without a value!")
        validateWrite(key)
        Holder.Reference.standalone(holderOwner(), it)
    }

    override fun wrapAsHolder(value: T & Any): Holder<T> = byValue.get(value) ?: Holder.Direct(value)

    /*
     * Tag API.
     */

    override fun tagKeys(): Set<TagKey<T>> = Collections.unmodifiableSet(tagEntries().keys)

    override fun tags(): Collection<TagSet<T>> {
        val transformed = Maps.transformValues(tags) { KryptonTagSet(this, it) }
        return Collections.unmodifiableCollection(transformed.values)
    }

    override fun tagEntries(): Map<TagKey<T>, HolderSet.Named<T>> = Collections.unmodifiableMap(tags)

    override fun tagNames(): Stream<TagKey<T>> = tags.keys.stream()

    override fun getTag(key: TagKey<T>): HolderSet.Named<T>? = tags.get(key)

    override fun getOrCreateTag(key: TagKey<T>): HolderSet.Named<T> {
        var tag = tags.get(key)
        if (tag == null) {
            tag = createTag(key)
            tags = IdentityHashMap(tags).apply { put(key, tag) }
        }
        return tag
    }

    override fun isKnownTagKey(key: TagKey<T>): Boolean = tags.containsKey(key)

    override fun bindTags(tags: Map<TagKey<T>, List<Holder<T>>>) {
        val tagsMap = IdentityHashMap<Holder.Reference<T>, MutableList<TagKey<T>>>()
        byKey.values.forEach { tagsMap.put(it, ArrayList()) }
        tags.forEach { (key, holders) ->
            holders.forEach { holder ->
                if (!holder.canSerializeIn(asLookup())) error("Cannot create tag set $key containing value $holder from outside registry $this!")
                if (holder !is Holder.Reference<*>) error("Found direct holder $holder value in tag $key!")
                tagsMap.get(holder as Holder.Reference<T>)!!.add(key)
            }
        }

        val difference = Sets.difference(this.tags.keys, tagsMap.keys)
        if (!difference.isEmpty()) {
            val missing = difference.stream().map { it.location.asString() }.sorted().collect(Collectors.joining(", "))
            LOGGER.warn("Not all defined tags for registry $key are present in data pack: $missing")
        }

        val tagsCopy = IdentityHashMap(this.tags)
        tags.forEach { (key, holders) -> tagsCopy.computeIfAbsent(key) { createTag(it) }.bind(holders) }
        tagsMap.forEach { (key, value) -> key.bindTags(value) }
        this.tags = tagsCopy
    }

    override fun resetTags() {
        tags.values.forEach { it.bind(ImmutableLists.of()) }
        byKey.values.forEach { it.bindTags(ImmutableSets.of()) }
    }

    private fun createTag(key: TagKey<T>): HolderSet.Named<T> = HolderSet.Named(holderOwner(), key)

    /*
     * Miscellaneous API.
     */

    override fun freeze(): KryptonRegistry<T> {
        if (frozen) return this
        frozen = true
        byValue.forEach { (value, holder) -> holder.bindValue(value) }
        val unbound = byKey.entries.stream().filter { !it.value.isBound() }.map { it.key.location }.sorted().toList()
        if (unbound.isNotEmpty()) error("Unbound values $unbound in registry $key!")
        val intrusiveHolders = unregisteredIntrusiveHolders
        if (intrusiveHolders != null) {
            if (intrusiveHolders.isNotEmpty()) error("Some intrusive holders were not registered: $intrusiveHolders")
            unregisteredIntrusiveHolders = null
        }
        return this
    }

    override fun createRegistrationLookup(): HolderGetter<T> {
        validateWrite()
        return object : HolderGetter<T> {
            override fun get(key: ResourceKey<T>): Holder.Reference<T> = getOrThrow(key)

            override fun getOrThrow(key: ResourceKey<T>): Holder.Reference<T> = getOrCreateHolderOrThrow(key)

            override fun get(key: TagKey<T>): HolderSet.Named<T> = getOrThrow(key)

            override fun getOrThrow(key: TagKey<T>): HolderSet.Named<T> = getOrCreateTag(key)
        }
    }

    override fun holderOwner(): HolderOwner<T> = lookup

    override fun asLookup(): HolderLookup.ForRegistry<T> = lookup

    override fun isEmpty(): Boolean = byKey.isEmpty()

    override fun iterator(): Iterator<T> = Iterators.transform(holdersInOrder().iterator()) { it.value() }

    override fun toString(): String = "SimpleRegistry(key=$key)"

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        fun <T> standard(key: ResourceKey<out Registry<T>>): KryptonSimpleRegistry<T> = KryptonSimpleRegistry(key, false)

        @JvmStatic
        fun <T> intrusive(key: ResourceKey<out Registry<T>>): KryptonSimpleRegistry<T> = KryptonSimpleRegistry(key, true)
    }
}
