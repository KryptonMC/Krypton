/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

/**
 * Represents an entity that lives in a world.
 */
interface LivingEntity : Entity {

    /**
     * If this entity is currently using an item
     */
    val isUsingItem: Boolean

    /**
     * The hand the entity is currently using
     */
    val hand: Hand

    /**
     * If this entity is in a riptide spin attack
     */
    val isAutoSpinAttack: Boolean

    /**
     * The health of this entity
     */
    val health: Float

    /**
     * The absorption of this entity
     */
    val absorption: Float
}
