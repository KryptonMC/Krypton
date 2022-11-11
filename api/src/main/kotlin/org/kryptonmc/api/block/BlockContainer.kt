/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.util.Vec3i

/**
 * Something that contains blocks.
 *
 * The default value that will be returned instead of null if no block is
 * found is the block state representing air.
 */
public interface BlockContainer {

    /**
     * Gets the block at the given [x], [y], and [z] coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the block at the given coordinates
     */
    public fun getBlock(x: Int, y: Int, z: Int): BlockState

    /**
     * Gets the block at the given [position].
     *
     * @param position the position
     * @return the block at the given position
     */
    public fun getBlock(position: Vec3i): BlockState

    /**
     * Sets the block at the given coordinates to the given [block].
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param block the block
     * @return true if the block was set, false otherwise
     */
    public fun setBlock(x: Int, y: Int, z: Int, block: BlockState): Boolean

    /**
     * Sets the block at the given [position] to the given [block].
     *
     * @param position the position
     * @param block the block
     * @return true if the block was set, false otherwise
     */
    public fun setBlock(position: Vec3i, block: BlockState): Boolean
}
