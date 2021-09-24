/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.HitResult
import org.kryptonmc.api.util.provide
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

/**
 * The result of a player hitting (attacking) a block.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BlockHitResult : HitResult {

    /**
     * The position of the block that was hit.
     */
    @get:JvmName("position")
    public val position: Vector3i

    /**
     * The direction the block was hit from.
     */
    @get:JvmName("direction")
    public val direction: Direction

    /**
     * If the player is inside of the block.
     */
    public val isInside: Boolean

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(
            clickLocation: Vector3d,
            position: Vector3i,
            direction: Direction,
            missed: Boolean,
            isInside: Boolean
        ): BlockHitResult
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new block hit result with the given values.
         *
         * @param clickLocation the location where the player clicked
         * @param position the position of the block that was hit
         * @param direction the direction the block was hit from
         * @param missed if the player missed the block
         * @param isInside if the player is inside the block
         * @return a new block hit result
         */
        @JvmStatic
        public fun of(
            clickLocation: Vector3d,
            position: Vector3i,
            direction: Direction,
            missed: Boolean,
            isInside: Boolean
        ): BlockHitResult = FACTORY.of(clickLocation, position, direction, missed, isInside)
    }
}
