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
package org.kryptonmc.krypton.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonStatisticTypes {

    val BLOCK_MINED = register("mined", InternalRegistries.BLOCK)
    val ITEM_CRAFTED = register("crafted", InternalRegistries.ITEM)
    val ITEM_USED = register("used", InternalRegistries.ITEM)
    val ITEM_BROKEN = register("broken", InternalRegistries.ITEM)
    val ITEM_PICKED_UP = register("picked_up", InternalRegistries.ITEM)
    val ITEM_DROPPED = register("dropped", InternalRegistries.ITEM)
    val ENTITY_KILLED = register("killed", InternalRegistries.ENTITY_TYPE)
    val ENTITY_KILLED_BY = register("killed_by", InternalRegistries.ENTITY_TYPE)
    val CUSTOM = register("custom", Registries.CUSTOM_STATISTIC)

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> register(name: String, registry: Registry<T>): KryptonStatisticType<T> {
        val key = Key.key(name)
        return Registries.register(
            InternalRegistries.STATISTIC_TYPE,
            key,
            KryptonStatisticType(key, registry)
        ) as KryptonStatisticType<T>
    }
}
