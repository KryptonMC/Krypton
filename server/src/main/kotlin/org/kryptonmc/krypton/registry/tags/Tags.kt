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
package org.kryptonmc.krypton.registry.tags

import kotlinx.serialization.Serializable
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

/**
 * Represents a tag, which holds information about its name and its set of values (including those of its children)
 */
data class Tag(val name: NamespacedKey, val values: MutableSet<NamespacedKey> = mutableSetOf()) {

    constructor(manager: TagManager, name: NamespacedKey, type: String, previous: Tag, data: TagData) : this(name) {
        if (!data.replace) values += previous.values
        data.values.forEach {
            if (it.startsWith('#')) {
                val subTag = manager.load(it.drop(1).toNamespacedKey(), type)
                values += subTag.values
            } else {
                values += it.toNamespacedKey()
            }
        }
    }

    companion object {

        val EMPTY = Tag(NamespacedKey("krypton", "empty"))
    }
}

data class RequiredTag(
    val type: TagType,
    val name: NamespacedKey
)

@Serializable
data class TagData(
    val replace: Boolean,
    val values: List<String>
)

enum class TagType {

    BLOCKS,
    ITEMS,
    FLUIDS,
    ENTITY_TYPES
}
