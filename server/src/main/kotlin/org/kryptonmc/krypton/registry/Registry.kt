package org.kryptonmc.krypton.registry

interface Registry<T> : Iterable<T> {

    fun idOf(value: T): Int

    operator fun get(id: Int): T?
}