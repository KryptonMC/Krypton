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
     * If this fluid state is empty.
     *
     * @return true if this fluid state is empty
     */
    public fun isEmpty(): Boolean = fluid.isEmpty()

    /**
     * If this fluid state is a source fluid.
     *
     * @return true if this fluid state is a source fluid
     */
    public fun isSource(): Boolean

    /**
     * Converts this fluid state in to its corresponding block state.
     *
     * @return this fluid state as a block state.
     */
    public fun asBlock(): BlockState
}
