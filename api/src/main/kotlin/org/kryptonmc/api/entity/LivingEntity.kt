/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.attribute.AttributeHolder
import org.kryptonmc.api.util.Vec3i

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
     * If this entity is currently using an item.
     */
    public var isUsingItem: Boolean

    /**
     * The hand the entity is currently using.
     */
    public var hand: Hand

    /**
     * If this entity is in a riptide spin attack.
     */
    public var isInRiptideSpinAttack: Boolean

    /**
     * If this entity is gliding with an elytra.
     *
     * This can be used to detect when the player is gliding without using
     * scoreboard statistics.
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
    public var sleepingPosition: Vec3i?
}
