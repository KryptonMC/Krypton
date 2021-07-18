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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.api.util.toKey

class KryptonTag<T : Any>(
    override val name: Key,
    override val type: TagType<T>,
    override val values: MutableList<T> = mutableListOf(),
) : Tag<T> {

    constructor(manager: KryptonTagManager, name: Key, type: TagType<T>, previous: Tag<T>, data: TagData) : this(name, type) {
        if (!data.replace) values += previous.values
        data.values.forEach { key ->
            if (key.startsWith('#')) {
                val subTag = manager.load(key.drop(1).toKey(), type)
                values += subTag.values
            } else {
                type.registry[key.toKey()]?.let { values += it }
            }
        }
    }

    override fun contains(value: T) = values.contains(value)

    companion object {

        private val EMPTY_KEY = Key.key("krypton", "empty")

        fun <T : Any> empty(type: TagType<T>) = KryptonTag(EMPTY_KEY, type)
    }
}
