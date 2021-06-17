/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import com.google.common.collect.HashBiMap
import it.unimi.dsi.fastutil.Hash
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import java.util.OptionalInt
import kotlin.math.max
import kotlin.random.Random

/**
 * This is not internal as it is not restricted to usage within the API only, though it
 * is not recommended to use this class, as it is not designed for use outside of the
 * API
 */
open class MappedRegistry<T>(key: RegistryKey<out Registry<T>>) : WritableRegistry<T>(key) {

    @Suppress("MagicNumber")
    private val byId = ObjectArrayList<T>(256)
    private val toId = Object2IntOpenCustomHashMap<T>(identityStrategy())
    private val storage = HashBiMap.create<Key, T>()
    private val keyStorage = HashBiMap.create<RegistryKey<T>, T>()

    private var randomCache: Array<Any?>? = null
    private var nextId = 0

    override fun <V : T> register(key: RegistryKey<T>, value: V) = registerMapping(nextId, key, value)

    override fun <V : T> registerMapping(id: Int, key: RegistryKey<T>, value: V) =
        registerMapping(id, key, value, true)

    override fun <V : T> registerOrOverride(id: OptionalInt, key: RegistryKey<T>, value: V): V {
        val current = keyStorage[key]
        val intId = if (current == null) {
            if (id.isPresent) id.asInt else nextId
        } else {
            val temp = toId.getInt(value)
            require(!id.isPresent || id.asInt == temp) { "ID mismatch!" }
            toId -= current
            temp
        }
        return registerMapping(intId, key, value, false)
    }

    @Suppress("ReplacePutWithAssignment") // We want to use fastutil's specialised version
    private fun <V : T> registerMapping(id: Int, key: RegistryKey<T>, value: V, warnDuplicate: Boolean): V {
        byId.size(max(byId.size, id + 1))
        byId[id] = value
        toId.put(value, id)
        randomCache = null
        if (warnDuplicate && keyStorage.containsKey(key)) LOGGER.debug("Adding duplicate key '$key' to registry")
        if (storage.containsValue(value)) LOGGER.error("Adding duplicate value '$value' to registry")

        storage[key.location] = value
        keyStorage[key] = value
        if (nextId <= id) nextId = id + 1
        return value
    }

    override fun getKey(value: T) = storage.inverse()[value]

    override fun getRegistryKey(value: T) = keyStorage.inverse()[value]

    override fun idOf(value: T) = toId.getInt(value)

    override fun get(id: Int) = if (id in byId.indices) byId[id] else null

    override fun get(key: Key) = storage[key]

    override fun get(key: RegistryKey<T>) = keyStorage[key]

    @Suppress("UNCHECKED_CAST")
    override fun getRandom(random: Random): T? {
        if (randomCache == null) {
            val values: Collection<*> = storage.values
            if (values.isEmpty()) return null
            randomCache = values.toTypedArray()
        }
        return (randomCache!! as Array<T?>)[random.nextInt(randomCache!!.size)]
    }

    override val keySet: Set<Key>
        get() = storage.keys

    override val entries: Set<Map.Entry<RegistryKey<T>, T>>
        get() = keyStorage.entries

    override val keys: Set<RegistryKey<T>>
        get() = keyStorage.keys

    override val size: Int
        get() = storage.size

    override val values: Collection<T>
        get() = storage.values

    override fun isEmpty() = storage.isEmpty()

    override fun iterator() = byId.asSequence().filterNotNull().iterator()

    override fun containsKey(key: Key) = storage.containsKey(key)

    override fun containsKey(key: RegistryKey<T>) = keyStorage.containsKey(key)

    override fun containsValue(value: T) = storage.containsValue(value)

    companion object {

        private val LOGGER = LogManager.getLogger(MappedRegistry::class.java)
    }
}

@Suppress("UNCHECKED_CAST")
private fun <K> identityStrategy(): Hash.Strategy<K> = IdentityStrategy as Hash.Strategy<K>

private object IdentityStrategy : Hash.Strategy<Any> {

    override fun hashCode(o: Any?) = System.identityHashCode(o)
    override fun equals(a: Any?, b: Any?) = a === b
}
