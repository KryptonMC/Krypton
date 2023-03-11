/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
