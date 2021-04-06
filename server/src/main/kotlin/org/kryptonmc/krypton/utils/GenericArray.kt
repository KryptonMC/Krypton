package org.kryptonmc.krypton.utils

class GenericArray<T>(size: Int) : Iterable<T?> {

    private val array = arrayOfNulls<Any>(size)

    @Suppress("UNCHECKED_CAST")
    operator fun get(index: Int) = array[index] as? T

    operator fun set(index: Int, value: T?) {
        array[index] = value
    }

    val size = array.size
    val indices = array.indices

    fun fill(value: T?) = array.fill(value)

    @Suppress("UNCHECKED_CAST")
    override fun iterator() = array.iterator() as Iterator<T?>
}