/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.block.Block

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
    @get:JvmName("baseDamage")
    public var baseDamage: Double

    /**
     * The block this arrow like object is currently stuck in, or null if this
     * object is not currently stuck in a block.
     */
    @get:JvmName("stuckInBlock")
    public var stuckInBlock: Block?

    /**
     * If this arrow like object is currently stuck in the ground.
     */
    public var isInGround: Boolean

    /**
     * The amount of remaining times that this arrow like object can pierce
     * through an entity.
     *
     * When this value reaches 0, it will no longer pierce through entities.
     */
    @get:JvmName("piercingLevel")
    public val piercingLevel: Int

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
     * The current pickup state of this arrow like object.
     */
    @get:JvmName("pickup")
    public val pickupState: PickupState

    /**
     * Arrow like pickup state.
     */
    public enum class PickupState {

        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY
    }
}
