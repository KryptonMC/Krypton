/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent

/**
 * Something that shares some (or all) functionality with that of an [Arrow].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ArrowLike : Projectile {

    /**
     * If this arrow like object is critical.
     */
    public var isCritical: Boolean

    /**
     * The damage multiplier of this arrow like object.
     */
    public var damage: Double

    /**
     * The block this arrow like object is currently stuck in, or null if this
     * object is not currently stuck in a block.
     */
    public var stuckInBlock: Block?

    /**
     * If this arrow like object is currently stuck in the ground.
     */
    public var isInGround: Boolean

    /**
     * The life of this arrow like object.
     *
     * This will increase by 1 for every tick this object is not moving.
     * When this value reaches 1200, it will despawn.
     */
    public val life: Int

    /**
     * The amount of remaining times that this arrow like object can pierce
     * through an entity.
     *
     * When this value reaches 0, it will no longer pierce through entities.
     */
    public val piercingLevel: Int

    /**
     * The amount of ticks this arrow like object will shake for until it can
     * be picked up by players.
     *
     * When it hits a block, this value will be initially set to 7.
     */
    public val shakeTime: Int

    /**
     * If this arrow like object ignores physics.
     */
    @get:JvmName("ignoresPhysics")
    public var ignoresPhysics: Boolean

    /**
     * If this arrow like object was shot from a crossbow.
     */
    @get:JvmName("wasShotFromCrossbow")
    public var wasShotFromCrossbow: Boolean

    /**
     * The sound event to play when hitting a block/mob.
     */
    public val sound: SoundEvent

    /**
     * The current pickup state of this arrow like object.
     */
    public val pickup: Pickup

    /**
     * Arrow like pickup state.
     */
    public enum class Pickup {

        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY
    }
}
