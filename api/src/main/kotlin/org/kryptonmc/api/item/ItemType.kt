/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.KeyedBuilder
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * Represents a type of item.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(ItemTypes::class)
public interface ItemType : Buildable<ItemType, ItemType.Builder>, ItemLike, BlockLike, TranslationHolder, Keyed {

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

    /**
     * A builder that can be used to build item type instances.
     */
    @ItemTypeDsl
    public interface Builder : Buildable.Builder<ItemType>, KeyedBuilder<ItemType, Builder>, TranslationHolder.Builder<Builder, ItemType> {

        /**
         * Sets the rarity of the item type.
         *
         * @param rarity the rarity
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun rarity(rarity: ItemRarity): Builder

        /**
         * Makes this item type stackable, with the given [maximumStackSize].
         *
         * @param maximumStackSize the maximum stack size
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun stackable(maximumStackSize: Int): Builder

        /**
         * Makes this item type unstackable.
         * This also sets the maximum stack size back to 0.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun unstackable(): Builder

        /**
         * Makes this item type breakable.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun breakable(): Builder

        /**
         * Makes this item type breakable, setting the durability to the given
         * [durability].
         *
         * @param durability the durability
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun breakable(durability: Int): Builder

        /**
         * Makes this item type unbreakable.
         * This will also set the durability to 0.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun unbreakable(): Builder

        /**
         * Sets the durability of this item type.
         *
         * @param durability the durability
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun durability(durability: Int): Builder

        /**
         * Makes this item type edible.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun edible(): Builder

        /**
         * Makes this item type inedible.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun inedible(): Builder

        /**
         * Makes this item type fire resistant.
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun fireResistant(): Builder

        /**
         * Makes this item type flammable (not fire resistant).
         *
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("-> this", mutates = "this")
        public fun flammable(): Builder

        /**
         * Sets the eating sound of this item type.
         *
         * @param eatingSound the eating sound
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun eatingSound(eatingSound: SoundEvent): Builder

        /**
         * Sets the drinking sound of this item type.
         *
         * @param drinkingSound the eating sound
         * @return this builder
         */
        @ItemTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun drinkingSound(drinkingSound: SoundEvent): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(key: Key): Builder
    }

    public companion object {

        /**
         * Creates a new builder for item types.
         *
         * @param key the key
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(key: Key): Builder = Krypton.factoryProvider.provide<Factory>().builder(key)
    }
}
