/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidLike
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder

/**
 * A block with certain properties.
 *
 * These are immutable and do not contain any state-specific
 * information, such as the world or location they are in, so
 * they can be easily reused in many places, which from a
 * technical standpoint, reduces allocations, but also makes
 * them much more thread-safe.
 */
// TODO: Review the fields in here and see if any we've removed need to be added,
// or any we've kept need to be removed.
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Blocks::class)
public interface Block : ImmutableDataHolder<Block>, PropertyHolder<Block>, BlockLike, ItemLike, FluidLike, TranslationHolder, Keyed {

    /**
     * The hardness of this block.
     *
     * This is used in determining the mining time of this block when a player
     * attempts to mine it.
     */
    @get:JvmName("hardness")
    public val hardness: Double

    /**
     * How resistant this block is to explosions. Higher
     * means more resistant.
     */
    @get:JvmName("explosionResistance")
    public val explosionResistance: Double

    /**
     * The friction of this block.
     */
    @get:JvmName("friction")
    public val friction: Double

    /**
     * If this block is air.
     */
    public val isAir: Boolean

    /**
     * If this block is a solid object with collision, meaning entities cannot
     * pass through it.
     */
    public val isSolid: Boolean

    /**
     * If this block represents a liquid rather than a solid object.
     *
     * All blocks that are liquids are also [Fluid]s.
     */
    public val isLiquid: Boolean

    /**
     * If this block can be set on fire.
     */
    public val isFlammable: Boolean

    /**
     * If this block can be replaced with another block when attempting to
     * place a block on this block.
     *
     * For example, when a player places a block on grass, instead of the block
     * being placed on the side in which the player clicked, the target grass
     * will be replaced with the block the player was placing.
     */
    public val isReplaceable: Boolean

    /**
     * If this block is opaque, not allowing any light to pass through it.
     */
    public val isOpaque: Boolean

    /**
     * Whether this block can be moved through.
     */
    @get:JvmName("blocksMotion")
    public val blocksMotion: Boolean

    /**
     * If this block has gravity, meaning that, when placed with one or more
     * air blocks beneath it, it will become a falling block entity, and fall
     * down towards the ground until it reaches a solid block, in which it will
     * stop falling and become a normal block.
     */
    @get:JvmName("hasGravity")
    public val hasGravity: Boolean

    /**
     * If this block can be respawned inside of.
     */
    @get:JvmName("canRespawnIn")
    public val canRespawnIn: Boolean

    /**
     * How this block reacts to being pushed or pulled by pistons.
     */
    @get:JvmName("pushReaction")
    public val pushReaction: PushReaction

    /**
     * If this block has an associated block entity.
     */
    @get:JvmName("hasBlockEntity")
    public val hasBlockEntity: Boolean

    override fun asBlock(): Block = this
}
