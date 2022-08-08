/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder

/**
 * Represents a type of item.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(ItemTypes::class)
public interface ItemType : ItemLike, BlockLike, TranslationHolder, Keyed {

    /**
     * The rarity of the item.
     */
    @get:JvmName("rarity")
    public val rarity: ItemRarity

    /**
     * The maximum amount of this item type that can be stacked in a single
     * [item stack][ItemStack].
     */
    @get:JvmName("maximumStackSize")
    public val maximumStackSize: Int

    /**
     * If items of this type actually take damage when they use or break blocks.
     */
    @get:JvmName("canBreak")
    public val canBreak: Boolean

    /**
     * The maximum amount of things items of this type can use/break before
     * they break.
     */
    @get:JvmName("durability")
    public val durability: Int

    /**
     * If items of this type can be eaten.
     */
    public val isEdible: Boolean

    /**
     * If items of this type are resistant to fire, meaning they won't burn up
     * when thrown in to any source of fire, such as standard fire or lava.
     */
    public val isFireResistant: Boolean

    /**
     * The sound that items of this type will make when they are eaten.
     *
     * This should only be used for edible items, and clients should already
     * be expected to output this sound, so it is unlikely that you will need
     * to use this.
     */
    @get:JvmName("eatingSound")
    public val eatingSound: SoundEvent

    /**
     * The sound that items of this type will make when they are drank.
     *
     * This should only be used for drinkable items, and clients should already
     * be expected to output this sound, so it is unlikely that you will need
     * to use this.
     */
    @get:JvmName("drinkingSound")
    public val drinkingSound: SoundEvent

    override fun asItem(): ItemType = this
}
