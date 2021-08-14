/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in statistic types.
 */
object StatisticTypes {

    // @formatter:off
    @JvmField val BLOCK_MINED = get<Block>("mined")
    @JvmField val ITEM_CRAFTED = get<ItemType>("crafted")
    @JvmField val ITEM_USED = get<ItemType>("used")
    @JvmField val ITEM_BROKEN = get<ItemType>("broken")
    @JvmField val ITEM_PICKED_UP = get<ItemType>("picked_up")
    @JvmField val ITEM_DROPPED = get<ItemType>("dropped")
    @JvmField val ENTITY_KILLED = get<EntityType<*>>("killed")
    @JvmField val ENTITY_KILLED_BY = get<EntityType<*>>("killed_by")
    @JvmField val CUSTOM = get<Key>("custom")

    // @formatter:on
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> get(name: String) = Registries.STATISTIC_TYPE[Key.key(name)]!! as StatisticType<T>
}
