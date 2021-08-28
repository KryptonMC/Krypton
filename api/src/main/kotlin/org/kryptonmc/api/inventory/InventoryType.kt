/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

import net.kyori.adventure.key.Key

/**
 * Represents a type of [Inventory] that holds items.
 *
 * @param key the key
 * @param size the size of the inventory
 */
public open class InventoryType(
    public val key: Key,
    public val size: Int
)

/**
 * Represents a type of inventory in a grid shape, such as a chest or a dropper.
 *
 * @param key the key
 * @param columns the number of columns
 * @param rows the number of rows
 */
@Suppress("MemberVisibilityCanBePrivate")
public class GridInventoryType(
    key: Key,
    public val columns: Int,
    public val rows: Int,
) : InventoryType(key, rows * columns)
