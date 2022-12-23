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

import com.google.common.collect.Iterators
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.holder.HolderSet
import java.util.stream.Stream

class KryptonTagSet<T>(override val registry: KryptonRegistry<T>, private val delegate: HolderSet.Named<T>) : TagSet<T> {

    override val key: TagKey<T>
        get() = delegate.key
    override val size: Int
        get() = delegate.size()

    override fun contains(value: T): Boolean = delegate.contains(registry.wrapAsHolder(value!!))

    override fun get(index: Int): T = delegate.get(index).value()

    override fun iterator(): Iterator<T> = Iterators.transform(delegate.iterator()) { it.value() }

    override fun stream(): Stream<T> = delegate.stream().map { it.value() }
}
