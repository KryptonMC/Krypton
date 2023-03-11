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
