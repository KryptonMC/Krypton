/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.item.ItemStack

/**
 * A thrown trident.
 */
interface Trident : ArrowLike {

    /**
     * The item that is given to entities that pick up this trident.
     * Defaults to a single stack with [org.kryptonmc.api.item.ItemTypes.TRIDENT].
     */
    val item: ItemStack

    /**
     * If this trident has already damaged an entity, in which case subsequent collisions
     * with entities will deal no damage.
     */
    var dealtDamage: Boolean

    /**
     * The level of the loyalty enchantment on this trident.
     */
    var loyaltyLevel: Int

    /**
     * If this trident has the enchantment glint (is enchanted).
     */
    var isEnchanted: Boolean
}
