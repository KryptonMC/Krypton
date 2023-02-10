/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.state.State
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A state that a block may be in.
 *
 * This is a representation of a block with specific properties that affect its
 * behaviour.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BlockState : State<BlockState> {

    /**
     * The block this is a state of.
     */
    @get:JvmName("block")
    public val block: Block

    /**
     * The hardness of this block state.
     *
     * This is used in determining the mining time of this block when a player
     * attempts to mine it.
     */
    @get:JvmName("hardness")
    public val hardness: Double

    /**
     * How this block reacts to being pushed or pulled by pistons.
     */
    @get:JvmName("pushReaction")
    public val pushReaction: PushReaction

    /**
     * If this block state is air.
     *
     * @return true if this block state is air
     */
    public fun isAir(): Boolean

    /**
     * If this block state is a solid object with collision, meaning
     * entities cannot pass through it.
     *
     * @return true if this block state is solid
     */
    public fun isSolid(): Boolean

    /**
     * If this block state represents a liquid rather than a solid object.
     *
     * All block states that are liquids are also fluid states.
     *
     * @return true if this block state is a liquid
     */
    public fun isLiquid(): Boolean

    /**
     * If this block state can be set on fire.
     *
     * @return true if this block state is flammable
     */
    public fun isFlammable(): Boolean

    /**
     * If this block state can be replaced with another block state
     * when attempting to place a block on this block.
     *
     * For example, when a player places a block on grass, instead of the
     * block being placed on the side in which the player clicked, the target
     * grass will be replaced with the block the player was placing.
     *
     * @return true if this block state is replaceable
     */
    public fun isReplaceable(): Boolean

    /**
     * If this block state is opaque, meaning no light can pass through it.
     *
     * @return true if this block state is opaque
     */
    public fun isOpaque(): Boolean

    /**
     * If this block can be moved through.
     *
     * @return true if this block state blocks motion
     */
    public fun blocksMotion(): Boolean

    /**
     * Converts this block state in to its corresponding fluid state.
     *
     * If this block state is not a liquid, this will return the default state
     * of Fluids.EMPTY.
     *
     * @return this block state as a fluid state.
     */
    public fun asFluid(): FluidState
}
