/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.item.ItemAttribute
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.GameMode

/**
 * Holder for various item metadata values for an item stack.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemMeta {

    /**
     * The current damage on the item.
     */
    @get:JvmName("damage")
    public val damage: Int

    /**
     * If the item cannot be broken.
     */
    public val isUnbreakable: Boolean

    /**
     * The custom model data for the item.
     *
     * The meaning of this may vary, but it is usually used by resource packs
     * to determine what variant of an item should be displayed, in the case
     * of adding custom items to the game only using a resource pack.
     */
    @get:JvmName("customModelData")
    public val customModelData: Int

    /**
     * The display name of the item.
     *
     * If null, the default display name of the item will be used.
     * This will usually be the [item's translation][ItemType.translation].
     */
    @get:JvmName("name")
    public val name: Component?

    /**
     * The lore of the item.
     */
    @get:JvmName("lore")
    public val lore: List<Component>

    /**
     * The flags that determine what is hidden for the item.
     *
     * To check whether the item has a specific flag, use [hasFlag].
     */
    @get:JvmName("hideFlags")
    public val hideFlags: Int

    /**
     * All of the blocks that the item can destroy.
     *
     * In vanilla, this is only applicable when the holder of the item is in
     * [adventure mode][GameMode.ADVENTURE].
     *
     * If this is empty, there are no restrictions on what blocks the item can
     * destroy.
     */
    @get:JvmName("canDestroy")
    public val canDestroy: Set<Block>

    /**
     * All of the blocks that the item can be placed on.
     *
     * In vanilla, this is only applicable when the holder of the item is in
     * [adventure mode][GameMode.ADVENTURE].
     *
     * If this is empty, there are no restrictions on what blocks the item can
     * be placed on.
     */
    @get:JvmName("canPlaceOn")
    public val canPlaceOn: Set<Block>

    /**
     * All of the attribute modifiers that will be applied to entities holding
     * items with this metadata.
     */
    @get:JvmName("attributeModifiers")
    public val attributeModifiers: Set<ItemAttribute>

    /**
     * Checks whether the item has the given item [flag].
     *
     * @param flag the flag
     * @return true if the item has the flag, false otherwise
     */
    public fun hasFlag(flag: ItemFlag): Boolean

    /**
     * Creates new item metadata with the given [damage].
     *
     * @param damage the new damage
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withDamage(damage: Int): ItemMeta

    /**
     * Creates new item metadata with the given [unbreakable] setting.
     *
     * @param unbreakable the new unbreakable setting
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withUnbreakable(unbreakable: Boolean): ItemMeta

    /**
     * Creates new item metadata with the given custom model [data].
     *
     * @param data the new custom model data
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withCustomModelData(data: Int): ItemMeta

    /**
     * Creates new item metadata with the given [name].
     *
     * @param name the new name
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withName(name: Component?): ItemMeta

    /**
     * Creates new item metadata with the given [lore].
     *
     * @param lore the new lore
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withLore(lore: Iterable<Component>): ItemMeta

    /**
     * Creates new item metadata with the given [lore] line added to the bottom
     * of the lore text.
     *
     * @param lore the lore line to add
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun addLore(lore: Component): ItemMeta

    /**
     * Creates new item metadata with the lore line at the given [index]
     * removed from the lore.
     *
     * @param index the index of the lore line to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    @Contract("_ -> new", pure = true)
    public fun removeLore(index: Int): ItemMeta

    /**
     * Creates new item metadata with the given [lore] line removed from the
     * lore.
     *
     * @param lore the lore line to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun removeLore(lore: Component): ItemMeta

    /**
     * Creates new item metadata with the given hide [flags].
     *
     * @param flags the new hide flags
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withHideFlags(flags: Int): ItemMeta

    /**
     * Creates new item metadata with the given hide [flag] set.
     *
     * @param flag the flag to set
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withHideFlag(flag: ItemFlag): ItemMeta

    /**
     * Creates new item metadata without the given hide [flag] set.
     *
     * @param flag the flag to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutHideFlag(flag: ItemFlag): ItemMeta

    /**
     * Creates new item metadata with the given can destroy [blocks].
     *
     * @param blocks the new blocks the item can destroy
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withCanDestroy(blocks: Iterable<Block>): ItemMeta

    /**
     * Creates new item metadata with the given can destroy [blocks].
     *
     * @param blocks the new blocks the item can destroy
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withCanPlaceOn(blocks: Iterable<Block>): ItemMeta

    /**
     * Gets the attribute modifiers that are applied to the attributes on this
     * item when it is in the given [slot].
     *
     * @param slot the slot
     * @return the modifiers associated with the attribute types
     */
    public fun attributeModifiers(slot: EquipmentSlot): Map<AttributeType, Set<AttributeModifier>>

    /**
     * Gets the attribute modifiers that are applied to the given attribute
     * [type] when this item is on the given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @return the modifiers associated with the given type
     */
    @Contract("_, _ -> new", pure = true)
    public fun attributeModifiers(type: AttributeType, slot: EquipmentSlot): Set<AttributeModifier>

    /**
     * Creates a new item metadata with the given [attributes] applied to
     * entities wearing the items this metadata is applied to.
     *
     * @param attributes the attributes
     * @return new item metadata
     */
    @Contract("_, _ -> new", pure = true)
    public fun withAttributeModifiers(attributes: Set<ItemAttribute>): ItemMeta

    /**
     * Creates new item metadata with the given [modifiers] applied to the
     * given attribute [type] when this item is on the given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @param modifiers the attributes
     * @return new item metadata
     */
    @Contract("_, _, _ -> new", pure = true)
    public fun withAttributeModifiers(type: AttributeType, slot: EquipmentSlot, modifiers: Set<AttributeModifier>): ItemMeta

    /**
     * Creates new item metadata with the given [modifiers] added to the list
     * of modifiers applied to the given attribute [type] when this item is in
     * the given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @param modifiers the modifiers to add
     * @return new item metadata
     */
    @Contract("_, _, _ -> new", pure = true)
    public fun addAttributeModifiers(type: AttributeType, slot: EquipmentSlot, modifiers: Iterable<AttributeModifier>): ItemMeta

    /**
     * Creates new item metadata with the given [modifiers] removed from the
     * list of modifiers applied to the given attribute [type] when this item
     * is in the given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @param modifiers the modifiers to remove
     * @return new item metadata
     */
    @Contract("_, _, _ -> new", pure = true)
    public fun removeAttributeModifiers(type: AttributeType, slot: EquipmentSlot, modifiers: Iterable<AttributeModifier>): ItemMeta

    /**
     * Creates new item metadata with all modifiers applied to the given [type]
     * when this item is in the given [slot] removed.
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @return new item metadata
     */
    @Contract("_, _ -> new", pure = true)
    public fun removeAttributeModifiers(type: AttributeType, slot: EquipmentSlot): ItemMeta

    /**
     * Creates new item metadata with the given [modifier] added to the list of
     * modifiers applied to the given attribute [type] when this item is on the
     * given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @param modifier the modifier to add
     * @return new item metadata
     */
    @Contract("_, _, _ -> new", pure = true)
    public fun addAttributeModifier(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemMeta

    /**
     * Creates new item metadata with the given [modifier] removed from the
     * list of modifiers applied to the given attribute [type] when this item
     * is on the given [slot].
     *
     * @param type the attribute type the modifiers are applied to
     * @param slot the slot
     * @param modifier the modifier to remove
     * @return new item metadata
     */
    @Contract("_, _, _ -> new", pure = true)
    public fun removeAttributeModifier(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemMeta

    /**
     * A builder for building item metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, ItemMeta>

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder

        public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B
    }

    public companion object {

        @JvmSynthetic
        internal val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building item metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(): Builder = FACTORY.builder()

        /**
         * Creates a new builder of type [B] for building metadata of type [P].
         *
         * @param type the class of the metadata type
         * @param B the builder type
         * @param P the metadata type
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = FACTORY.builder(type)
    }
}
