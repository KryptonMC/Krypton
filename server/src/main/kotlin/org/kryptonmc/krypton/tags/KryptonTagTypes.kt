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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.event.GameEvent

object KryptonTagTypes {

    private val MINECRAFT = KryptonPlatform.dataVersionPrefix
    private val PREFIX = "${MINECRAFT}_tags/${MINECRAFT}_"

    @JvmField val BLOCKS: KryptonTagType<Block> = register("block", Registries.BLOCK)
    @JvmField val ENTITY_TYPES: KryptonTagType<EntityType<*>> = register("entity_type", Registries.ENTITY_TYPE)
    @JvmField val FLUIDS: KryptonTagType<Fluid> = register("fluid", Registries.FLUID)
    @JvmField val GAME_EVENTS: KryptonTagType<GameEvent> = register("game_event", "gameplay", InternalRegistries.GAME_EVENT)
    @JvmField val ITEMS: KryptonTagType<ItemType> = register("item", Registries.ITEM)

    @JvmStatic
    private fun <T : Any> register(name: String, fileName: String, registry: Registry<T>): KryptonTagType<T> {
        val key = Key.key(name)
        return Registries.TAG_TYPES.register(key, KryptonTagType(key, "${PREFIX}${fileName}_tags.json", registry))
    }

    @JvmStatic
    private fun <T : Any> register(name: String, registry: Registry<T>): KryptonTagType<T> = register(name, name, registry)
}
