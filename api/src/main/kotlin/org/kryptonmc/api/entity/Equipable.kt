/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.item.ItemStack

/**
 * Something that can be equipped with hand and armour items.
 */
public interface Equipable {

    /**
     * Gets the item that this equipable holds in the given [hand].
     *
     * This may return [ItemStack.empty] if this equipable is not holding an item in
     * the given [hand].
     *
     * @param hand the hand
     * @return the item this mob is holding in the hand
     */
    public fun getHeldItem(hand: Hand): ItemStack

    /**
     * Sets the item that this mob holds in the given [hand] to the given
     * [item].
     *
     * @param hand the hand
     * @param item the item
     */
    public fun setHeldItem(hand: Hand, item: ItemStack)

    /**
     * Gets the armour item that this mob has equipped in the given [slot].
     *
     * This may return [ItemStack.empty] if this mob does not have any armour
     * equipped in the given [slot].
     *
     * @param slot the slot
     */
    public fun getArmor(slot: ArmorSlot): ItemStack

    /**
     * Sets the armour item that this mob has equipped in the given [slot] to
     * the given [item].
     *
     * @param slot the slot
     * @param item the item
     */
    public fun setArmor(slot: ArmorSlot, item: ItemStack)

    /**
     * Gets the equipment item that this mob has equipped in the given [slot].
     *
     * This may return [ItemStack.empty] if this mob does not have any armour
     * equipped in the given [slot].
     *
     * @param slot the slot
     * @return the item in the given slot
     */
    public fun getEquipment(slot: EquipmentSlot): ItemStack

    /**
     * Sets the equipment item that this mob has equipped in the given [slot] to
     * the given [item].
     *
     * @param slot the slot
     * @param item the item
     */
    public fun setEquipment(slot: EquipmentSlot, item: ItemStack)
}
