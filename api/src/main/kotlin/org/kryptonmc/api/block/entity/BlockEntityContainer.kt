/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.util.Vec3i

/**
 * Something that contains block entities.
 */
public interface BlockEntityContainer {

    /**
     * Gets the block entity at the given [x], [y], and [z] coordinates, or
     * returns null if there is no block entity at the given [x], [y], and [z]
     * coordinates.
     *
     * @param T the type of block entity
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the block entity at the coordinates, or null if not present
     */
    public fun <T : BlockEntity> getBlockEntity(x: Int, y: Int, z: Int): T?

    /**
     * Gets the block entity at the given [position], or returns null if there
     * is no block entity at the given [position].
     *
     * @param T the type of block entity
     * @param position the position of the block entity
     * @return the block entity at the position, or null if not present
     */
    public fun <T : BlockEntity> getBlockEntity(position: Vec3i): T?
}
