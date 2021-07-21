/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.google.common.collect.Sets
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey

class StaticTagHelper<T : Any>(
    val key: ResourceKey<out Registry<T>>,
    val directory: String
) {

    private val wrappers = mutableListOf<Wrapper<T>>()
    var tags = TagCollection.empty<T>()
        private set

    fun bind(name: String): Tag.Named<T> = Wrapper<T>(Key.key(name)).apply { wrappers.add(this) }

    fun reset(container: TagContainer) {
        val collection = container.getOrEmpty(key)
        tags = collection
        wrappers.forEach { wrapper -> wrapper.rebind { collection[it] } }
    }

    fun missing(container: TagContainer): Set<Key> {
        val collection = container.getOrEmpty(key)
        val names = wrappers.mapTo(mutableSetOf()) { it.name }
        val tags = collection.availableTags.toSet()
        return Sets.difference(names, tags)
    }

    fun addToCollection(builder: TagContainer.Builder) {
        builder.add(key, TagCollection.of(wrappers.associateBy { it.name }))
    }

    class Wrapper<T : Any>(override val name: Key) : Tag.Named<T> {

        private var tag: Tag<T>? = null
        override val values: List<T>
            get() = resolve().values

        fun rebind(getter: (Key) -> Tag<T>?) {
            tag = getter(name)
        }

        override fun contains(value: T) = resolve().contains(value)

        private fun resolve(): Tag<T> {
            checkNotNull(tag) { "Tag $name was used before it was bound!" }
            return tag!!
        }
    }
}
