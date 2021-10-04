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
import org.jetbrains.annotations.Contract

/**
 * Represents a type of inventory in a grid shape, such as a chest or a
 * dropper.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface GridInventoryType : InventoryType {

    /**
     * The number of columns in the inventory.
     */
    @get:JvmName("columns")
    public val columns: Int

    /**
     * The number of rows in the inventory.
     */
    @get:JvmName("rows")
    public val rows: Int

    public companion object {

        /**
         * Creates a new grid inventory type with the given values.
         *
         * @param key the key
         * @param columns the number of columns
         * @param rows the number of rows
         * @return a new grid inventory type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, columns: Int, rows: Int): GridInventoryType = InventoryType.FACTORY.grid(key, columns, rows)
    }
}
