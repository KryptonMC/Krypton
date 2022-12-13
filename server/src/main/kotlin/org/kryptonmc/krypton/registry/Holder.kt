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

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.util.Either
import java.util.Optional
import java.util.function.Predicate
import java.util.stream.Stream

interface Holder<T> {

    fun kind(): Kind

    fun isBound(): Boolean

    fun value(): T

    fun tags(): Stream<TagKey<T>>

    fun isValidIn(registry: Registry<T>): Boolean

    fun eq(key: Key): Boolean

    fun eq(key: ResourceKey<T>): Boolean

    fun eq(predicate: Predicate<ResourceKey<T>>): Boolean

    fun eq(key: TagKey<T>): Boolean

    fun unwrap(): Either<ResourceKey<T>, T>

    fun unwrapKey(): Optional<ResourceKey<T>>

    enum class Kind {

        REFERENCE,
        DIRECT
    }

    @JvmRecord
    data class Direct<T>(private val value: T & Any) : Holder<T> {

        override fun kind(): Kind = Kind.DIRECT

        override fun isBound(): Boolean = true

        override fun value(): T = value

        override fun tags(): Stream<TagKey<T>> = Stream.empty()

        override fun isValidIn(registry: Registry<T>): Boolean = true

        override fun eq(key: Key): Boolean = false

        override fun eq(key: ResourceKey<T>): Boolean = false

        override fun eq(predicate: Predicate<ResourceKey<T>>): Boolean = false

        override fun eq(key: TagKey<T>): Boolean = false

        override fun unwrap(): Either<ResourceKey<T>, T> = Either.right(value)

        override fun unwrapKey(): Optional<ResourceKey<T>> = Optional.empty()

        override fun toString(): String = "Direct($value)"
    }

    class Reference<T> private constructor(
        private val type: Type,
        private val registry: Registry<T>,
        private var key: ResourceKey<T>?,
        private var value: T?
    ) : Holder<T> {

        private var tags = ImmutableSet.of<TagKey<T>>()

        override fun kind(): Kind = Kind.REFERENCE

        override fun isBound(): Boolean = key != null && value != null

        fun key(): ResourceKey<T> = checkNotNull(key) { "Trying to access unbound value $value from registry $registry!" }

        override fun value(): T = checkNotNull(value) { "Trying to access unbound value $key from registry $registry!" }

        override fun tags(): Stream<TagKey<T>> = tags.stream()

        override fun isValidIn(registry: Registry<T>): Boolean = this.registry == registry

        override fun eq(key: Key): Boolean = key().location == key

        override fun eq(key: ResourceKey<T>): Boolean = key() == key

        override fun eq(predicate: Predicate<ResourceKey<T>>): Boolean = predicate.test(key())

        override fun eq(key: TagKey<T>): Boolean = tags.contains(key)

        override fun unwrap(): Either<ResourceKey<T>, T> = Either.left(key())

        override fun unwrapKey(): Optional<ResourceKey<T>> = Optional.of(key())

        internal fun bind(key: ResourceKey<T>, value: T) {
            require(this.key == null || key == this.key) { "Cannot modify holder key! Existing: ${this.key}, New: $key" }
            require(type != Type.INTRUSIVE || this.value == value) { "Cannot modify holder! Existing: ${this.value}, New: $value" }
            this.key = key
            this.value = value
        }

        internal fun bindTags(tags: Collection<TagKey<T>>) {
            this.tags = ImmutableSet.copyOf(tags)
        }

        override fun toString(): String = "Reference($key=$value)"

        enum class Type {

            STANDALONE,
            INTRUSIVE
        }

        companion object {

            @JvmStatic
            fun <T> standalone(registry: Registry<T>, key: ResourceKey<T>): Reference<T> = Reference(Type.STANDALONE, registry, key, null)

            @JvmStatic
            fun <T> intrusive(registry: Registry<T>, value: T): Reference<T> = Reference(Type.INTRUSIVE, registry, null, value)
        }
    }
}
