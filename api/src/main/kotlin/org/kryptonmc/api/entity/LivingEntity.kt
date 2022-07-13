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
@Suppress("INAPPLICABLE_JVM_NAME")
public interface LivingEntity : Entity, AttributeHolder {

    /**
     * The current health of this entity.
     */
    @get:JvmName("health")
    public var health: Float

    /**
     * The maximum health of this entity.
     */
    @get:JvmName("maxHealth")
    public val maxHealth: Float

    /**
     * The amount of absorption this living entity has.
     */
    @get:JvmName("absorption")
    public var absorption: Float

    /**
     * If this entity is currently using an item
     */
    public val isUsingItem: Boolean

    /**
     * The hand the entity is currently using
     */
    @get:JvmName("hand")
    public val hand: Hand

    /**
     * If this entity is in a riptide spin attack
     */
    public val isInRiptideSpinAttack: Boolean

    /**
     * The last time, in ticks, this entity was damaged.
     *
     * Calculated as the
     * [number of ticks since the entity's creation][ticksExisted].
     */
    @get:JvmName("lastHurtTimestamp")
    public val lastHurtTimestamp: Int

    /**
     * The current position this entity is sleeping at.
     *
     * If this value is null, this entity is not currently sleeping.
     */
    @get:JvmName("sleepingPosition")
    public val sleepingPosition: Vector3i?
}
