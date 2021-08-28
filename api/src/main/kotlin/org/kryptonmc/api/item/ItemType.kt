/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.registry.Registries

/**
 * Represents a type of item.
 *
 * @param key the key of the item
 * @param rarity the rarity of the item
 * @param maximumAmount the maximum amount of items that can be held in a single
 * stack
 * @param durability the durability of the item
 * @param isFireResistant if this item is resistant to fire
 * @param translation the client-side translation for this item
 */
@JvmRecord
public data class ItemType(
    public val key: Key,
    public val rarity: ItemRarity,
    public val maximumAmount: Int,
    public val durability: Int,
    public val isFireResistant: Boolean,
    public val translation: TranslatableComponent
) : BlockLike, ComponentLike {

    override fun asBlock(): Block? = Registries.BLOCK[key]

    override fun asComponent(): TranslatableComponent = translation
}
