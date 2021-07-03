/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.inventory.item.Material
import org.spongepowered.math.vector.Vector3i

/**
 * Represents a block.
 */
interface Block {

    /**
     * The type of this block
     */
    val type: Material

    /**
     * The location of this block
     */
    val location: Vector3i

    /**
     * If this block is empty
     *
     * A block is defined as being empty if it's type is [Material.AIR]
     */
    val isEmpty: Boolean

    /**
     * If this block is a liquid
     *
     * A block is defined as being a liquid if its type is [Material.WATER] or [Material.LAVA]
     */
    val isLiquid: Boolean
}
