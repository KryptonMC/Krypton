/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in statistic types.
 */
@Catalogue(StatisticType::class)
public object StatisticTypes {

    // @formatter:off
    @JvmField public val BLOCK_MINED: StatisticType<Block> = register("mined", Registries.BLOCK)
    @JvmField public val ITEM_CRAFTED: StatisticType<ItemType> = register("crafted", Registries.ITEM)
    @JvmField public val ITEM_USED: StatisticType<ItemType> = register("used", Registries.ITEM)
    @JvmField public val ITEM_BROKEN: StatisticType<ItemType> = register("broken", Registries.ITEM)
    @JvmField public val ITEM_PICKED_UP: StatisticType<ItemType> = register("picked_up", Registries.ITEM)
    @JvmField public val ITEM_DROPPED: StatisticType<ItemType> = register("dropped", Registries.ITEM)
    @JvmField public val ENTITY_KILLED: StatisticType<EntityType<*>> = register("killed", Registries.ENTITY_TYPE)
    @JvmField public val ENTITY_KILLED_BY: StatisticType<EntityType<*>> = register("killed_by", Registries.ENTITY_TYPE)
    @JvmField public val CUSTOM: StatisticType<Key> = register("custom", Registries.CUSTOM_STATISTIC)

    // @formatter:on
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <T : Any> register(name: String, registry: Registry<T>): StatisticType<T> {
        val key = Key.key(name)
        return Registries.STATISTIC_TYPE.register(key, StatisticType.of(key, registry))
    }
}
