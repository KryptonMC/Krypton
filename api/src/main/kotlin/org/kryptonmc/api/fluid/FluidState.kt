/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.state.State
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A state of a fluid.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface FluidState : State<FluidState> {

    /**
     * The fluid this is a state of.
     */
    @get:JvmName("fluid")
    public val fluid: Fluid

    /**
     * If this fluid state is empty.
     */
    public val isEmpty: Boolean
        get() = fluid.isEmpty

    /**
     * If this fluid state is a source fluid.
     */
    public val isSource: Boolean

    /**
     * The level of this fluid state.
     *
     * Should be either a constant value, such as 0 for the empty fluid, or 8
     * for source fluids, or the value of the
     * [level][org.kryptonmc.api.state.Properties.LIQUID_LEVEL]
     * property for flowing fluids.
     */
    @get:JvmName("level")
    public val level: Int

    /**
     * Converts this fluid state in to its corresponding block state.
     *
     * @return this fluid state as a block state.
     */
    public fun asBlock(): BlockState
}
