package org.kryptonmc.krypton.registry.impl

import org.kryptonmc.krypton.registry.Registry

open class MappedRegistry<T>(values: Map<out T, Int>? = null) : Registry<T> {

    private var nextID = 0
    private val byT = mutableMapOf<T, Int>()
    private val byId = mutableListOf<T?>()

    init {
        if (values != null) {
            byT.putAll(values)
            byId += values.keys
        }
    }

    override fun get(id: Int): T? {
        if (id >= 0 && id < byId.size) return byId[id]
        return null
    }

    override fun idOf(value: T) = byT[value] ?: -1

    operator fun set(id: Int, value: T) {
        byT[value] = id
        while (byId.size <= id) {
            byId += null
        }
        byId[id] = value
        if (nextID <= id) nextID = id + 1
    }

    operator fun plus(value: T) = set(nextID, value)

    override fun iterator() = byId.filterNotNull().iterator()

    val size = byT.size
}