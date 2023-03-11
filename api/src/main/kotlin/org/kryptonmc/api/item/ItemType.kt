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
package org.kryptonmc.api.item

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Represents a type of item.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(ItemTypes::class)
@ImmutableType
public interface ItemType : ItemLike, BlockLike, Translatable, Keyed {

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
