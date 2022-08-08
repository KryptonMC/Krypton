/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import org.spongepowered.math.vector.Vector3i

/**
 * Something that contains fluids.
 *
 * The default value that will be returned instead of null if no fluid is
 * found is [Fluids.EMPTY].
 */
public interface FluidContainer {

    /**
     * Gets the fluid at the given [x], [y], and [z] coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the fluid at the given coordinates
     */
    public fun getFluid(x: Int, y: Int, z: Int): FluidState

    /**
     * Gets the fluid at the given [position].
     *
     * @param position the position
     * @return the fluid at the given position
     */
    public fun getFluid(position: Vector3i): FluidState
}
