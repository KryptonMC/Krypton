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

import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.ImmutableSets
import org.kryptonmc.util.Either
import java.util.Optional
import java.util.Spliterator
import java.util.stream.Stream

interface HolderSet<T> : Iterable<Holder<T>> {

    fun size(): Int

    fun contains(holder: Holder<T>): Boolean

    fun get(index: Int): Holder<T>

    fun unwrap(): Either<TagKey<T>, List<Holder<T>>>

    fun unwrapKey(): Optional<TagKey<T>>

    fun canSerializeIn(owner: HolderOwner<T>): Boolean

    fun stream(): Stream<Holder<T>>

    abstract class ListBacked<T> : HolderSet<T> {

        protected abstract fun contents(): List<Holder<T>>

        override fun size(): Int = contents().size

        override fun get(index: Int): Holder<T> = contents().get(index)

        override fun canSerializeIn(owner: HolderOwner<T>): Boolean = true

        override fun stream(): Stream<Holder<T>> = contents().stream()

        override fun iterator(): Iterator<Holder<T>> = contents().iterator()

        override fun spliterator(): Spliterator<Holder<T>> = contents().spliterator()
    }

    class Direct<T>(private val contents: List<Holder<T>>) : ListBacked<T>() {

        private var contentsSet: Set<Holder<T>>? = null

        override fun contents(): List<Holder<T>> = contents

        override fun contains(holder: Holder<T>): Boolean {
            if (contentsSet == null) contentsSet = ImmutableSets.copyOf(contents)
            return contentsSet!!.contains(holder)
        }

        override fun unwrap(): Either<TagKey<T>, List<Holder<T>>> = Either.right(contents)

        override fun unwrapKey(): Optional<TagKey<T>> = Optional.empty()

        override fun toString(): String = "DirectSet(contents=$contents)"
    }

    class Named<T>(private val owner: HolderOwner<T>, val key: TagKey<T>) : ListBacked<T>() {

        private var contents: List<Holder<T>> = ImmutableLists.of()

        fun bind(contents: List<Holder<T>>) {
            this.contents = ImmutableLists.copyOf(contents)
        }

        override fun contents(): List<Holder<T>> = contents

        override fun contains(holder: Holder<T>): Boolean = holder.eq(key)

        override fun unwrap(): Either<TagKey<T>, List<Holder<T>>> = Either.left(key)

        override fun unwrapKey(): Optional<TagKey<T>> = Optional.of(key)

        override fun canSerializeIn(owner: HolderOwner<T>): Boolean = this.owner.canSerializeIn(owner)

        override fun toString(): String = "NamedSet(key=$key, contents=$contents)"
    }

    companion object {

        @JvmStatic
        fun <T> direct(contents: List<Holder<T>>): Direct<T> = Direct(ImmutableLists.copyOf(contents))

        @JvmStatic
        fun <T> direct(vararg contents: Holder<T>): Direct<T> = Direct(ImmutableLists.ofArray(contents))
    }
}
