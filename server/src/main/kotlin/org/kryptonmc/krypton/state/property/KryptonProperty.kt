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
package org.kryptonmc.krypton.state.property

import org.kryptonmc.api.state.Property
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.util.mapSuccess
import org.kryptonmc.krypton.util.orElseError
import org.kryptonmc.serialization.Codec
import java.util.Optional
import java.util.concurrent.atomic.AtomicInteger

abstract class KryptonProperty<T : Comparable<T>>(final override val name: String, final override val type: Class<T>) : Property<T> {

    val id: Int = ID_COUNTER.getAndIncrement()
    val codec: Codec<T> = Codec.STRING.comapFlatMap(
        { Optional.ofNullable(fromString(it)).mapSuccess().orElseError("Unable to read property $this with value $it!") },
        ::toString
    )
    val valueCodec: Codec<Value<T>> = codec.xmap(::value, Value<T>::value)
    // We cache the hash code because we frequently look up by property in maps, and computing the hash code is expensive.
    private var hashCode: Int? = null

    abstract fun fromString(value: String): T?

    abstract fun toString(value: T): String

    abstract fun idFor(value: T): Int

    fun value(value: T): Value<T> = Value(this, value)

    fun value(holder: KryptonState<*, *>): Value<T> = Value(this, holder.require(this))

    final override fun equals(other: Any?): Boolean = this === other

    final override fun hashCode(): Int {
        if (hashCode == null) hashCode = generateHashCode()
        return hashCode!!
    }

    final override fun toString(): String = "KryptonProperty(name=$name, type=$type, values=$values)"

    protected open fun generateHashCode(): Int = 31 * type.hashCode() + name.hashCode()

    @JvmRecord
    data class Value<T : Comparable<T>>(val property: KryptonProperty<T>, val value: T) {

        init {
            require(property.values.contains(value)) { "Value $value does not belong to property $property!" }
        }

        override fun toString(): String = "${property.name}=${property.toString(value)}"
    }

    companion object {

        private val ID_COUNTER = AtomicInteger()
    }
}
