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
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in inventory types.
 *
 * Note: The player inventory is not included in this list as it is not a valid
 * menu type, and cannot be created, or opened client-side.
 */
@Catalogue(InventoryType::class)
public object InventoryTypes {

    // @formatter:off
    @JvmField
    public val CHEST_ONE_ROW: RegistryReference<InventoryType> = of("chest_one_row")
    @JvmField
    public val CHEST_TWO_ROWS: RegistryReference<InventoryType> = of("chest_two_rows")
    @JvmField
    public val CHEST_THREE_ROWS: RegistryReference<InventoryType> = of("chest_three_rows")
    @JvmField
    public val CHEST_FOUR_ROWS: RegistryReference<InventoryType> = of("chest_four_rows")
    @JvmField
    public val CHEST_FIVE_ROWS: RegistryReference<InventoryType> = of("chest_five_rows")
    @JvmField
    public val CHEST_SIX_ROWS: RegistryReference<InventoryType> = of("chest_six_rows")
    @JvmField
    public val GENERIC_3x3: RegistryReference<InventoryType> = of("generic_3x3")
    @JvmField
    public val ANVIL: RegistryReference<InventoryType> = of("anvil")
    @JvmField
    public val BEACON: RegistryReference<InventoryType> = of("beacon")
    @JvmField
    public val BLAST_FURNACE: RegistryReference<InventoryType> = of("blast_furnace")
    @JvmField
    public val BREWING_STAND: RegistryReference<InventoryType> = of("brewing_stand")
    @JvmField
    public val CRAFTING_TABLE: RegistryReference<InventoryType> = of("crafting")
    @JvmField
    public val ENCHANTING_TABLE: RegistryReference<InventoryType> = of("enchantment")
    @JvmField
    public val FURNACE: RegistryReference<InventoryType> = of("furnace")
    @JvmField
    public val GRINDSTONE: RegistryReference<InventoryType> = of("grindstone")
    @JvmField
    public val HOPPER: RegistryReference<InventoryType> = of("hopper")
    @JvmField
    public val LECTERN: RegistryReference<InventoryType> = of("lectern")
    @JvmField
    public val LOOM: RegistryReference<InventoryType> = of("loom")
    @JvmField
    public val MERCHANT: RegistryReference<InventoryType> = of("merchant")
    @JvmField
    public val SHULKER_BOX: RegistryReference<InventoryType> = of("shulker_box")
    @JvmField
    public val SMITHING_TABLE: RegistryReference<InventoryType> = of("smithing")
    @JvmField
    public val SMOKER: RegistryReference<InventoryType> = of("smoker")
    @JvmField
    public val CARTOGRAPHY_TABLE: RegistryReference<InventoryType> = of("cartography_table")
    @JvmField
    public val STONECUTTER: RegistryReference<InventoryType> = of("stonecutter")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<InventoryType> = RegistryReference.of(Registries.INVENTORY_TYPE, Key.key(name))
}
