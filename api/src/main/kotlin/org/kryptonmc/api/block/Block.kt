/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.fluid.FluidLike
import org.kryptonmc.api.item.ItemLike

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
public interface Block : PropertyHolder<Block>, ItemLike, FluidLike, Comparable<Block> {

    /**
     * The key associated with this block.
     */
    public val key: Key

    /**
     * The block ID of this block.
     */
    public val id: Int

    /**
     * The ID of the block state this block represents.
     */
    public val stateId: Int

    /**
     * The hardness of this block.
     */
    public val hardness: Double

    /**
     * How resistant this block is to explosions. Higher
     * means more resistant.
     */
    public val explosionResistance: Double

    /**
     * The amount of light this block emits, in levels.
     */
    public val lightEmission: Int

    /**
     * The friction of this block.
     */
    public val friction: Double

    /**
     * The speed factor of this block.
     */
    public val speedFactor: Double

    /**
     * The jump factor of this block.
     */
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
    public val renderShape: RenderShape

    /**
     * The translation component for translating the name
     * of this block.
     */
    public val translation: TranslatableComponent
}
