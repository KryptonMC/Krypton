/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

/**
 * Something that can be represented by an item stack.
 */
public fun interface ItemStackLike {

    /**
     * Converts this object to its item stack representation.
     *
     * @return the item stack representation
     */
    public fun asItem(): ItemStack
}
