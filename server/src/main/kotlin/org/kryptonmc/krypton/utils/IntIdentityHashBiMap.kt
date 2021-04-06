package org.kryptonmc.krypton.utils

import org.kryptonmc.krypton.registry.Registry
import kotlin.math.max

// this is essentially a Kotlin clone of CrudeIncrementalIntIdentityHashBiMap (lol)
class IntIdentityHashBiMap<K>(size: Int) : Registry<K> {

    private var keys = GenericArray<K>((size / 0.8F).toInt())
    private var values = IntArray((size / 0.8F).toInt())
    private var byId = GenericArray<K>((size / 0.8F).toInt())

    private var nextId = 0
        get() {
            while (field < byId.size && byId[field] != null) {
                field++
            }
            return field
        }
    var size = 0
        private set

    override fun idOf(value: K) = getValue(indexOf(value, value.hash))

    override fun get(id: Int): K? {
        if (id < 0 || id >= byId.size) return null
        return byId[id]
    }

    operator fun plus(key: K): Int {
        val id = nextId
        set(key, id)
        return id
    }

    private operator fun set(key: K, value: Int) {
        val id = max(value, size + 1)
        if (id >= keys.size * 0.8F) {
            var size = 0
            for (i in (keys.size shl 1) until value) size = size shl 1
            grow(size)
        }
        val emptyIndex = findEmpty(key.hash)
        keys[emptyIndex] = key
        values[emptyIndex] = value
        byId[value] = key
        size++
        if (value == nextId) nextId++
    }

    private fun getValue(index: Int): Int {
        if (index == -1) return -1
        return values[index]
    }

    private fun indexOf(key: K, hash: Int): Int {
        for (i in hash until keys.size) {
            if (keys[i] == key) return i
            if (keys[i] != null) continue
            return -1
        }
        for (i in 0 until hash) {
            if (keys[i] == key) return i
            if (keys[i] != null) continue
            return -1
        }
        return -1
    }

    private fun findEmpty(int: Int): Int {
        for (i in int until keys.size) {
            if (keys[i] != null) continue
            return i
        }
        for (i in 0 until int) {
            if (keys[i] != null) continue
            return i
        }
        throw RuntimeException("Overflowed :(")
    }

    private fun grow(size: Int) {
        val oldKeys = keys
        val oldValues = values
        keys = GenericArray(size)
        values = IntArray(size)
        byId = GenericArray(size)
        nextId = 0
        this.size = 0
        for (i in oldKeys.indices) {
            val oldIndex = oldKeys[i] ?: continue
            set(oldIndex, oldValues[i])
        }
    }

    private val K.hash: Int
        get() = (System.identityHashCode(this).murmurHash and Int.MAX_VALUE) % keys.size

    override fun iterator() = byId.filterNotNull().iterator()

    fun clear() {
        keys.fill(null)
        byId.fill(null)
        nextId = 0
        size = 0
    }
}

private val Int.murmurHash: Int
    get() {
        var result = this xor (this ushr 16)
        result *= -2048144789
        result = result xor (result ushr 13)
        result *= -1028477387
        result = result xor (result ushr 16)
        return result
    }