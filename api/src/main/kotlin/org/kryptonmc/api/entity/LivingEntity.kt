/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.attribute.AttributeHolder
import org.spongepowered.math.vector.Vector3i

/**
 * Represents an entity that lives in a world.
 */
public interface LivingEntity : Entity, AttributeHolder {

    /**
     * The current health of this entity.
     */
    public var health: Float

    /**
     * The maximum health of this entity.
     */
    public val maxHealth: Float

    /**
     * The amount of absorption this living entity has.
     */
    public var absorption: Float

    /**
     * If this entity is currently using an item
     */
    public var isUsingItem: Boolean

    /**
     * The hand the entity is currently using
     */
    public var hand: Hand

    /**
     * If this entity is in a riptide spin attack
     */
    public var isInRiptideSpinAttack: Boolean

    /**
     * If this entity is gliding with an elytra.
     *
     * This can be used to detect when the player is gliding without using
     * scoreboard statistics.
     *
     * This field can only
     */
    public var isGliding: Boolean

    /**
     * If this entity is dead or not.
     */
    public val isDead: Boolean

    /**
     * The number of ticks this entity has been dead for.
     * This is used to control death animations.
     *
     * Will be 0 whilst this entity is alive.
     */
    public val deathTime: Int

    /**
     * The number of ticks this entity will turn red for after being hit.
     *
     * Will be 0 when not recently hit.
     */
    public val hurtTime: Int

    /**
     * The last time, in ticks, this entity was damaged.
     *
     * Calculated as the
     * [number of ticks since the entity's creation][ticksExisted].
     */
    public val lastHurtTimestamp: Int

    /**
     * The current position this entity is sleeping at.
     *
     * If this value is null, this entity is not currently sleeping.
     */
    public var sleepingPosition: Vector3i?
}
