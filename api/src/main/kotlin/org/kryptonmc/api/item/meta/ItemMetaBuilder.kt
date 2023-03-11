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
package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * The base builder for item metadata.
 */
@MetaDsl
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemMetaBuilder<B : ItemMetaBuilder<B, I>, I : ItemMeta> {

    /**
     * Sets the damage of the item to the given [damage].
     *
     * @param damage the damage
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun damage(damage: Int): B

    /**
     * Sets whether the item is unbreakable to the given [value].
     *
     * @param value whether the item is unbreakable
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun unbreakable(value: Boolean): B

    /**
     * Makes the item unbreakable.
     *
     * @return this builder
     */
    @MetaDsl
    @Contract("-> this", mutates = "this")
    public fun unbreakable(): B = unbreakable(true)

    /**
     * Sets the custom model data for the item to the given [data].
     *
     * @param data the custom model data
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun customModelData(data: Int): B

    /**
     * Sets the name of the item to the given [name].
     *
     * @param name the name
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun name(name: Component?): B

    /**
     * Sets the lore of the item to the given [lore].
     *
     * @param lore the lore
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun lore(lore: Collection<Component>): B

    /**
     * Sets the lore of the item to the given [lore].
     *
     * @param lore the lore
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun lore(vararg lore: Component): B = lore(lore.asList())

    /**
     * Adds the given [lore] line to the lore of the item.
     *
     * @param lore the lore line to add
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun addLore(lore: Component): B

    /**
     * Sets the hide flags for the item to the given [flags].
     *
     * @param flags the hide flags
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun hideFlags(flags: Int): B

    /**
     * Sets the given hide [flag] on the item.
     *
     * @param flag the hide flag to set
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun addFlag(flag: ItemFlag): B

    /**
     * Sets the list of blocks the item can destroy to the given [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun canDestroy(blocks: Collection<Block>): B

    /**
     * Sets the list of blocks the item can destroy to the given [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun canDestroy(vararg blocks: Block): B = canDestroy(blocks.asList())

    /**
     * Adds the given [block] to the list of blocks the item can destroy.
     *
     * @param block the block to add
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun addCanDestroy(block: Block): B

    /**
     * Sets the list of blocks the item can be placed on to the given
     * [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun canPlaceOn(blocks: Collection<Block>): B

    /**
     * Sets the list of blocks the item can be placed on to the given
     * [blocks].
     *
     * @param blocks the blocks
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun canPlaceOn(vararg blocks: Block): B = canPlaceOn(blocks.asList())

    /**
     * Adds the given [block] to the list of blocks the item can be placed on.
     *
     * @param block the block to add
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun addCanPlaceOn(block: Block): B

    /**
     * Sets the list of attribute modifiers applied to entities wearing items
     * that the metadata is applied to to the given [modifiers].
     *
     * @param modifiers the attribute modifiers
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun attributeModifiers(modifiers: Collection<ItemAttributeModifier>): B

    /**
     * Sets the list of attribute modifiers applied to entities wearing items
     * that the metadata is applied to to the given [modifiers].
     *
     * @param modifiers the attribute modifiers
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun attributeModifiers(vararg modifiers: ItemAttributeModifier): B = attributeModifiers(modifiers.asList())

    /**
     * Adds the given [modifier] modifier to the list of attribute modifiers
     * for the item.
     *
     * @param modifier the attribute modifier to add
     * @return this builder
     */
    @MetaDsl
    @Contract("_ -> this", mutates = "this")
    public fun addAttributeModifier(modifier: ItemAttributeModifier): B

    /**
     * Builds the resulting item metadata.
     *
     * @return the built item metadata
     */
    @MetaDsl
    @Contract("-> new", pure = true)
    public fun build(): I

    /**
     * A provider of an item meta builder.
     */
    public interface Provider<T> {

        /**
         * Converts this object to a builder with all of the properties of this
         * object set by default.
         *
         * @return a new builder
         */
        @Contract("-> new", pure = true)
        public fun toBuilder(): T
    }
}
