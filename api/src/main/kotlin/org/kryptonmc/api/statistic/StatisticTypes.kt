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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in statistic types.
 */
@Catalogue(StatisticType::class)
public object StatisticTypes {

    // @formatter:off
    @JvmField
    public val BLOCK_MINED: RegistryReference<StatisticType<Block>> = get("mined")
    @JvmField
    public val ITEM_CRAFTED: RegistryReference<StatisticType<ItemType>> = get("crafted")
    @JvmField
    public val ITEM_USED: RegistryReference<StatisticType<ItemType>> = get("used")
    @JvmField
    public val ITEM_BROKEN: RegistryReference<StatisticType<ItemType>> = get("broken")
    @JvmField
    public val ITEM_PICKED_UP: RegistryReference<StatisticType<ItemType>> = get("picked_up")
    @JvmField
    public val ITEM_DROPPED: RegistryReference<StatisticType<ItemType>> = get("dropped")
    @JvmField
    public val ENTITY_KILLED: RegistryReference<StatisticType<EntityType<*>>> = get("killed")
    @JvmField
    public val ENTITY_KILLED_BY: RegistryReference<StatisticType<EntityType<*>>> = get("killed_by")
    @JvmField
    public val CUSTOM: RegistryReference<StatisticType<Key>> = get("custom")

    // @formatter:on
    @JvmStatic
    private fun <T : Any> get(name: String): RegistryReference<StatisticType<T>> = RegistryReference.of(Registries.STATISTIC_TYPE, Key.key(name))
}
