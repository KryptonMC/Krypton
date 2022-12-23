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
package org.kryptonmc.krypton.registry.holder

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.util.ImmutableSets
import org.kryptonmc.util.Either
import java.util.Optional
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * A holder of a registry value.
 *
 * This is used to allow registries to be dynamically repopulated when reloaded and not have the problem
 * of updating existing references to the new registry values.
 */
interface Holder<T> {

    fun kind(): Kind

    fun isBound(): Boolean

    fun value(): T & Any

    fun tags(): Stream<TagKey<T>>

    fun eq(key: Key): Boolean

    fun eq(key: ResourceKey<T>): Boolean

    fun eq(predicate: Predicate<ResourceKey<T>>): Boolean

    fun eq(key: TagKey<T>): Boolean

    fun unwrap(): Either<ResourceKey<T>, T>

    fun unwrapKey(): Optional<ResourceKey<T>>

    fun canSerializeIn(owner: HolderOwner<T>): Boolean

    enum class Kind {

        REFERENCE,
        DIRECT
    }

    @JvmRecord
    data class Direct<T>(private val value: T & Any) : Holder<T> {

        override fun kind(): Kind = Kind.DIRECT

        override fun isBound(): Boolean = true

        override fun value(): T & Any = value

        override fun tags(): Stream<TagKey<T>> = Stream.empty()

        override fun eq(key: Key): Boolean = false

        override fun eq(key: ResourceKey<T>): Boolean = false

        override fun eq(predicate: Predicate<ResourceKey<T>>): Boolean = false

        override fun eq(key: TagKey<T>): Boolean = false

        override fun unwrap(): Either<ResourceKey<T>, T> = Either.right(value)

        override fun unwrapKey(): Optional<ResourceKey<T>> = Optional.empty()

        override fun canSerializeIn(owner: HolderOwner<T>): Boolean = true

        override fun toString(): String = "Direct(value=$value)"
    }

    class Reference<T> private constructor(
        private val type: Type,
        private val owner: HolderOwner<T>,
        private var key: ResourceKey<T>?,
        private var value: T?
    ) : Holder<T> {

        private var tags = ImmutableSets.of<TagKey<T>>()

        override fun kind(): Kind = Kind.REFERENCE

        override fun isBound(): Boolean = key != null && value != null

        fun key(): ResourceKey<T> = checkNotNull(key) { "Trying to access unbound value $value from registry $owner!" }

        override fun value(): T & Any = checkNotNull(value) { "Trying to access unbound value $key from registry $owner!" }

        override fun tags(): Stream<TagKey<T>> = tags.stream()

        override fun eq(key: Key): Boolean = key().location == key

        override fun eq(key: ResourceKey<T>): Boolean = key() == key

        override fun eq(predicate: Predicate<ResourceKey<T>>): Boolean = predicate.test(key())

        override fun eq(key: TagKey<T>): Boolean = tags.contains(key)

        override fun unwrap(): Either<ResourceKey<T>, T> = Either.left(key())

        override fun unwrapKey(): Optional<ResourceKey<T>> = Optional.of(key())

        override fun canSerializeIn(owner: HolderOwner<T>): Boolean = this.owner.canSerializeIn(owner)

        internal fun bindKey(key: ResourceKey<T>) {
            if (this.key != null && key !== this.key) error("Cannot modify holder key! Existing: ${this.key}, New: $key")
            this.key = key
        }

        internal fun bindValue(value: T) {
            if (type == Type.INTRUSIVE && this.value !== value) error("Cannot modify intrusive holder value! Existing: ${this.value}, New: $value")
            this.value = value
        }

        internal fun bindTags(tags: Collection<TagKey<T>>) {
            this.tags = ImmutableSets.copyOf(tags)
        }

        override fun toString(): String = "Reference(key=$key, value=$value)"

        enum class Type {

            STANDALONE,
            INTRUSIVE
        }

        companion object {

            @JvmStatic
            fun <T> standalone(owner: HolderOwner<T>, key: ResourceKey<T>): Reference<T> = Reference(Type.STANDALONE, owner, key, null)

            @JvmStatic
            fun <T> intrusive(owner: HolderOwner<T>, value: T): Reference<T> = Reference(Type.INTRUSIVE, owner, null, value)
        }
    }
}
