/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.state.property

import org.kryptonmc.api.state.Property
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.serialization.Codec
import java.util.concurrent.atomic.AtomicInteger

abstract class KryptonProperty<T : Comparable<T>> protected constructor(
    final override val name: String,
    final override val type: Class<T>
) : Property<T> {

    val id: Int = ID_COUNTER.getAndIncrement()
    val codec: Codec<T> =
        Codec.STRING.comapFlatMap({ fromString(it).resultOrError { "Unable to read property $this with value $it!" } }, { toString(it) })
    val valueCodec: Codec<Value<T>> = codec.xmap({ value(it) }, { it.value })
    // We cache the hash code because we frequently look up by property in maps, and computing the hash code is expensive.
    private var hashCode: Int? = null

    abstract fun fromString(value: String): T?

    abstract fun toString(value: T): String

    abstract fun idFor(value: T): Int

    fun value(value: T): Value<T> = Value(this, value)

    fun value(holder: KryptonState<*, *>): Value<T> = Value(this, holder.requireProperty(this))

    final override fun equals(other: Any?): Boolean = this === other

    final override fun hashCode(): Int {
        if (hashCode == null) hashCode = generateHashCode()
        return hashCode!!
    }

    final override fun toString(): String = "${javaClass.simpleName}(name=$name, type=$type, values=$values)"

    @Suppress("MagicNumber") // This is a hash code function
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
