package org.kryptonmc.krypton.registry

/**
 * Represents a registry, which is a bi-map of Int to T and T to Int
 *
 * @author Callum Seabrook
 */
interface Registry<T> : Iterable<T> {

    /**
     * Get the id for the specified [value], or -1 if the specified value is not registered
     *
     * @param value the value
     * @return the ID of the [value], or -1 if the value isn't registered
     */
    fun idOf(value: T): Int

    /**
     * Get the value for the specified [id], or null if there is no value with the specified [id]
     *
     * @param id the ID
     * @return the value with this [id], or null if there is no value with this [id]
     */
    operator fun get(id: Int): T?
}