/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.inventory.item.dsl

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.inventory.item.Material

@DslMarker
annotation class ItemDSL

/**
 * Build an item stack
 */
@ItemDSL
fun item(builder: ItemBuilder.() -> Unit = {}) = ItemBuilder().apply(builder).build()

/**
 * Build an item stack with the specified [type]
 *
 * @param type the type of item this stack holds
 * @return the built item stack
 */
@ItemDSL
fun item(type: Material, builder: ItemBuilder.() -> Unit = {}) = ItemBuilder(type).apply(builder).build()

/**
 * Build an item stack with the specified [type] and [amount]
 *
 * @param type the type of item this stack holds
 * @param amount the amount of this item this stack holds
 * @return the built item stack
 */
@ItemDSL
fun item(type: Material, amount: Int, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount).apply(builder).build()

/**
 * Build an item stack with the specified [type], [amount] and custom [name]
 *
 * @param type the type of item this stack holds
 * @param amount the amount of this item this stack holds
 * @param name the name of the item
 * @return the built item stack
 */
@ItemDSL
fun item(type: Material, amount: Int, name: Component, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount, name).apply(builder).build()

/**
 * Build an item stack with the specified [type], [amount] and custom [lore]
 *
 * @param type the type of item this stack holds
 * @param amount the amount of this item this stack holds
 * @param lore the lore of the item
 * @return the built item stack
 */
fun item(type: Material, amount: Int, lore: List<Component>, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount, lore = lore).apply(builder).build()

/**
 * Build an item stack with the specified [type], [amount], custom [name] and [lore]
 *
 * @param type the type of item this stack holds
 * @param amount the amount of this item this stack holds
 * @param name the name of the item
 * @param lore the lore of the item
 * @return the built item stack
 */
@ItemDSL
fun item(type: Material, amount: Int, name: Component, lore: List<Component>) =
    ItemBuilder(type, amount, name, lore).build()
