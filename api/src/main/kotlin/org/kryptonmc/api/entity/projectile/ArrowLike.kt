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
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.block.BlockState

/**
 * Something that shares some (or all) functionality with that of an [Arrow].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ArrowLike : Projectile {

    /**
     * The base damage that this arrow like object will do to an entity it
     * comes in to contact with.
     */
    public var baseDamage: Double

    /**
     * The block this arrow like object is currently stuck in, or null if this
     * object is not currently stuck in a block.
     */
    public var stuckInBlock: BlockState?

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
    public var piercingLevel: Int

    /**
     * The current pickup state of this arrow like object.
     */
    public var pickupRule: PickupRule

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
