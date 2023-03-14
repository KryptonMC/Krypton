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
