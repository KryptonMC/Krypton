package org.kryptonmc.krypton.api.inventory.item.dsl

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.inventory.item.ItemType

@DslMarker
annotation class ItemDSL

@ItemDSL
fun item(builder: ItemBuilder.() -> Unit = {}) = ItemBuilder().apply(builder).build()

@ItemDSL
fun item(type: ItemType, builder: ItemBuilder.() -> Unit = {}) = ItemBuilder(type).apply(builder).build()

@ItemDSL
fun item(type: ItemType, amount: Int, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount).apply(builder).build()

@ItemDSL
fun item(type: ItemType, amount: Int, name: Component, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount, name).apply(builder).build()

fun item(type: ItemType, amount: Int, lore: List<Component>, builder: ItemBuilder.() -> Unit = {}) =
    ItemBuilder(type, amount, lore = lore).apply(builder).build()

@ItemDSL
fun item(type: ItemType, amount: Int, name: Component, lore: List<Component>) =
    ItemBuilder(type, amount, name, lore).build()