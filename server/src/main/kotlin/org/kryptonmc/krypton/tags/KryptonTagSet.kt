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
package org.kryptonmc.krypton.tags

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.registry.Holder
import java.util.Spliterator
import java.util.stream.Stream

class KryptonTagSet<T>(override val registry: Registry<T>, override val key: TagKey<T>) : TagSet<T> {

    private var values = ImmutableList.of<T>()
    override val size: Int
        get() = values.size

    fun bind(values: List<T>) {
        this.values = ImmutableList.copyOf(values)
    }

    fun bindHolders(values: List<Holder<T>>) {
        val result = ImmutableList.builder<T>()
        values.forEach { result.add(it.value()) }
        this.values = result.build()
    }

    override fun contains(value: T): Boolean = values.contains(value)

    override fun get(index: Int): T = values.get(index)

    override fun iterator(): Iterator<T> = values.iterator()

    override fun spliterator(): Spliterator<T> = values.spliterator()

    override fun stream(): Stream<T> = values.stream()
}
