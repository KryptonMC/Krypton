/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla tag types.
 */
@Catalogue(TagType::class)
public object TagTypes {

    // @formatter:off
    @JvmField
    public val BLOCKS: TagType<Block> = get("block")
    @JvmField
    public val ENTITY_TYPES: TagType<EntityType<*>> = get("entity_type")
    @JvmField
    public val FLUIDS: TagType<Fluid> = get("fluid")
    @JvmField
    public val ITEMS: TagType<ItemType> = get("item")

    // @formatter:on
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <T : Any> get(key: String): TagType<T> = Registries.TAG_TYPES[Key.key(key)]!! as TagType<T>
}
