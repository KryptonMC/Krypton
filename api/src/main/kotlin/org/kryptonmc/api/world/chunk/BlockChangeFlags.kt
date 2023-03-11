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
package org.kryptonmc.api.world.chunk

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A set of flags used to determine what happens when a block is changed.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BlockChangeFlags {

    /**
     * The raw value of this set of flags.
     */
    @get:JvmName("raw")
    public val raw: Int

    /**
     * If neighbours should be updated when the block is updated.
     */
    @get:JvmName("updateNeighbours")
    public val updateNeighbours: Boolean

    /**
     * If clients with the block in render distance should be notified of its
     * update.
     */
    @get:JvmName("notifyClients")
    public val notifyClients: Boolean

    /**
     * If observer blocks should be updated.
     */
    @get:JvmName("updateNeighbourShapes")
    public val updateNeighbourShapes: Boolean

    /**
     * If blocks can be destroyed as a result of updating neighbour shapes.
     */
    @get:JvmName("neighbourDrops")
    public val neighbourDrops: Boolean

    /**
     * If the block change considers that blocks can be moved in the world.
     */
    @get:JvmName("blockMoving")
    public val blockMoving: Boolean

    /**
     * If lighting updates should be performed.
     */
    @get:JvmName("lighting")
    public val lighting: Boolean

    /**
     * Creates a new set of flags with the given [updateNeighbours] setting.
     *
     * @param updateNeighbours the new setting
     * @return the resulting flags
     */
    public fun withUpdateNeighbours(updateNeighbours: Boolean): BlockChangeFlags

    /**
     * Creates a new set of flags with the given [notifyClients] setting.
     *
     * @param notifyClients the new setting
     * @return the resulting flags
     */
    public fun withNotifyClients(notifyClients: Boolean): BlockChangeFlags

    /**
     * Creates a new set of flags with the given [updateNeighbourShapes]
     * setting.
     *
     * @param updateNeighbourShapes the new setting
     * @return the resulting flags
     */
    public fun withUpdateNeighbourShapes(updateNeighbourShapes: Boolean): BlockChangeFlags

    /**
     * Creates a new set of flags with the given [neighbourDrops] setting.
     *
     * @param neighbourDrops the new setting
     * @return the resulting flags
     */
    public fun withNeighbourDrops(neighbourDrops: Boolean): BlockChangeFlags

    /**
     * Creates a new set of flags with the given [blockMoving] setting.
     *
     * @param blockMoving the new setting
     * @return the resulting flags
     */
    public fun withBlockMoving(blockMoving: Boolean): BlockChangeFlags

    /**
     * Creates a new set of flags with the given [lighting] setting.
     *
     * @param lighting the new setting
     * @return the resulting flags
     */
    public fun withLighting(lighting: Boolean): BlockChangeFlags

    /**
     * Performs a bitwise NOT operation on this set of flags.
     *
     * @return the resulting flags
     */
    public fun not(): BlockChangeFlags

    /**
     * Performs a bitwise AND operation between this set of flags and the
     * given [other] set of flags.
     *
     * @param other the other flags
     * @return the resulting flags
     */
    public fun and(other: BlockChangeFlags): BlockChangeFlags

    /**
     * Performs a bitwise OR operation between this set of flags and the
     * given [other] set of flags.
     *
     * @param other the other flags
     * @return the resulting flags
     */
    public fun or(other: BlockChangeFlags): BlockChangeFlags

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun none(): BlockChangeFlags

        public fun all(): BlockChangeFlags
    }

    public companion object {

        /**
         * A set of flags with no flags set.
         *
         * @return a flag set with no flags set
         */
        @JvmStatic
        public fun none(): BlockChangeFlags = Krypton.factory<Factory>().none()

        /**
         * A set of flags with all the flags set.
         *
         * @return a flag set with all flags set
         */
        @JvmStatic
        public fun all(): BlockChangeFlags = Krypton.factory<Factory>().all()
    }
}
