/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.fluid.FluidLike
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.util.TranslationHolder

/**
 * Represents a block with certain properties.
 *
 * These are immutable and do not contain any state-specific
 * information, such as the world or location they are in, so
 * they can be easily reused in many places, which from a
 * technical standpoint, reduces allocations, but also makes
 * them much more thread-safe.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Block : PropertyHolder<Block>, ItemLike, FluidLike, TranslationHolder, Keyed, Comparable<Block> {

    /**
     * The block ID of this block.
     */
    @get:JvmName("id")
    public val id: Int

    /**
     * The ID of the block state this block represents.
     */
    @get:JvmName("stateId")
    public val stateId: Int

    /**
     * The hardness of this block.
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
     * The amount of light this block emits, in levels.
     */
    @get:JvmName("lightEmission")
    public val lightEmission: Int

    /**
     * The friction of this block.
     */
    @get:JvmName("friction")
    public val friction: Double

    /**
     * The speed factor of this block.
     */
    @get:JvmName("speedFactor")
    public val speedFactor: Double

    /**
     * The jump factor of this block.
     */
    @get:JvmName("jumpFactor")
    public val jumpFactor: Double

    /**
     * If this block is air.
     */
    public val isAir: Boolean

    /**
     * If this block is solid.
     */
    public val isSolid: Boolean

    /**
     * If this block is liquid.
     */
    public val isLiquid: Boolean

    /**
     * If this block is flammable (can be set on fire).
     */
    public val isFlammable: Boolean

    /**
     * If this block can be replaced.
     */
    public val isReplaceable: Boolean

    /**
     * If this block has an associated block entity.
     */
    @get:JvmName("hasBlockEntity")
    public val hasBlockEntity: Boolean

    /**
     * If light cannot pass through this block.
     */
    @get:JvmName("occludes")
    public val occludes: Boolean

    /**
     * If this block has a dynamic shape.
     */
    @get:JvmName("hasDynamicShape")
    public val hasDynamicShape: Boolean

    /**
     * If the shape of this block should be used to calculate light occlusion.
     */
    @get:JvmName("useShapeForOcclusion")
    public val useShapeForOcclusion: Boolean

    /**
     * If this block propagates skylight down.
     */
    @get:JvmName("propagatesSkylightDown")
    public val propagatesSkylightDown: Boolean

    /**
     * The amount of light that this block will block from passing through it.
     */
    @get:JvmName("lightBlock")
    public val lightBlock: Int

    /**
     * If this block is sometimes fully opaque.
     */
    public val isConditionallyFullyOpaque: Boolean

    /**
     * If this block is rendered solid.
     */
    public val isSolidRender: Boolean

    /**
     * The opacity of this block.
     */
    @get:JvmName("opacity")
    public val opacity: Int

    /**
     * If this block cannot be moved through.
     */
    @get:JvmName("blocksMotion")
    public val blocksMotion: Boolean

    /**
     * If this block has gravity.
     */
    @get:JvmName("hasGravity")
    public val hasGravity: Boolean

    /**
     * If this block can be respawned inside of.
     */
    @get:JvmName("canRespawnIn")
    public val canRespawnIn: Boolean

    /**
     * If the shape used for collision is very large.
     */
    @get:JvmName("hasLargeCollisionShape")
    public val hasLargeCollisionShape: Boolean

    /**
     * If this block requires the correct tool to be used to break it.
     */
    @get:JvmName("requiresCorrectTool")
    public val requiresCorrectTool: Boolean

    /**
     * The render shape of this block.
     */
    @get:JvmName("renderShape")
    public val renderShape: RenderShape
}
