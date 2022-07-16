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
     * The base damage that this arrow like object will do to an entity it
     * comes in to contact with.
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
     * If this arrow like object is critical.
     */
    public var isCritical: Boolean

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
     * The amount of remaining times that this arrow like object can pierce
     * through an entity.
     *
     * When this value reaches 0, it will no longer pierce through entities.
     */
    @get:JvmName("piercingLevel")
    public val piercingLevel: Int

    /**
     * The current pickup state of this arrow like object.
     */
    @get:JvmName("pickupRule")
    public val pickupRule: PickupRule

    /**
     * A rule that determines whether an arrow like object can be picked up.
     */
    public enum class PickupRule {

        /**
         * The arrow like object cannot ever be picked up.
         */
        DISALLOWED,

        /**
         * The arrow like object can always be picked up.
         */
        ALLOWED,

        /**
         * The arrow like object can only be picked up in creative mode.
         */
        CREATIVE_ONLY
    }
}
