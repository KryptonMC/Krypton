/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.attribute.Attributable
import org.spongepowered.math.vector.Vector3i

/**
 * Represents an entity that lives in a world.
 */
interface LivingEntity : Entity, Attributable {

    /**
     * The current health of this entity.
     */
    var health: Float

    /**
     * The amount of absorption this living entity has.
     */
    var absorption: Float

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
    val isInRiptideSpinAttack: Boolean

    /**
     * If this entity is fall flying.
     *
     * Setting this value to true for non-player entities will cause this entity
     * to glide as long as they are wearing an elytra in their chestplate slot.
     *
     * This can be used to detect when the player is gliding without using
     * scoreboard statistics.
     */
    val isFallFlying: Boolean

    /**
     * If this entity is dead or not.
     */
    val isDead: Boolean

    /**
     * The number of ticks this entity has been dead for.
     * This is used to control death animations.
     *
     * Will be 0 whilst this entity is alive.
     */
    val deathTime: Short

    /**
     * The number of ticks this entity will turn red for after being hit.
     *
     * Will be 0 when not recently hit.
     */
    val hurtTime: Short

    /**
     * The last time, in ticks, this entity was damaged.
     *
     * Calculated as the [number of ticks since the entity's creation][ticksExisted].
     */
    val lastHurtTimestamp: Int

    /**
     * The current position this entity is sleeping at.
     *
     * If this value is null, this entity is not currently sleeping.
     */
    val sleepingPosition: Vector3i?
}
