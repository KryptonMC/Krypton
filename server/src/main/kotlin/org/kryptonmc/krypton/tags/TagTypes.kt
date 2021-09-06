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

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalRegistries

object TagTypes {

    private val MINECRAFT = KryptonPlatform.minecraftVersion.replace('.', '_')
    private val PREFIX = "${MINECRAFT}_tags/${MINECRAFT}_"
    val VALUES = mutableListOf<TagType<*>>()

    val BLOCKS = create("block", InternalRegistries.BLOCK)
    val ENTITY_TYPES = create("entity_type", InternalRegistries.ENTITY_TYPE)
    val FLUIDS = create("fluid", InternalRegistries.FLUID)
    val GAME_EVENTS = create("game_event", "gameplay", InternalRegistries.GAME_EVENT)
    val ITEMS = create("item", InternalRegistries.ITEM)

    private fun <T : Any> create(
        name: String,
        fileName: String,
        registry: Registry<T>
    ) = TagType("minecraft:$name", "${PREFIX}${fileName}_tags.json", registry).apply { VALUES += this }

    private fun <T : Any> create(name: String, registry: Registry<T>) = create(name, name, registry)
}
