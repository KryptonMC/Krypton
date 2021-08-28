/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.inventory

/**
 * Represents any object that can hold an inventory (e.g. entities, players, etc.)
 */
public interface InventoryHolder {

    /**
     * The inventory that is being held by this holder
     */
    public val inventory: Inventory
}
