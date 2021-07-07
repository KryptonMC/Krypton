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
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.space.Direction

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
interface Block : PropertyHolder<Block>, ItemLike, Comparable<Block> {

    /**
     * The key associated with this block.
     */
    val key: Key

    /**
     * The enum name of this block.
     *
     * For example, if the [key] was "minecraft:air", the
     * name would likely be "AIR".
     */
    val name: String

    /**
     * The block ID of this block.
     */
    val id: Int

    /**
     * The ID of the block state this block represents.
     */
    val stateId: Int

    /**
     * The hardness of this block.
     */
    val hardness: Double

    /**
     * How resistant this block is to explosions. Higher
     * means more resistant.
     */
    val explosionResistance: Double

    /**
     * The amount of light this block emits, in levels.
     */
    val lightEmission: Int

    /**
     * The friction of this block.
     */
    val friction: Double

    /**
     * The speed factor of this block.
     */
    val speedFactor: Double

    /**
     * The jump factor of this block.
     */
    val jumpFactor: Double

    /**
     * If this block is air.
     */
    val isAir: Boolean

    /**
     * If this block is solid.
     */
    val isSolid: Boolean

    /**
     * If this block is liquid.
     */
    val isLiquid: Boolean

    /**
     * If this block is flammable (can be set on fire).
     */
    val isFlammable: Boolean

    /**
     * If this block can be replaced.
     */
    val isReplaceable: Boolean

    /**
     * If this block has an associated block entity.
     */
    @get:JvmName("hasBlockEntity")
    val hasBlockEntity: Boolean

    /**
     * If light cannot pass through this block.
     */
    @get:JvmName("occludes")
    val occludes: Boolean

    /**
     * If this block has a dynamic shape.
     */
    @get:JvmName("hasDynamicShape")
    val hasDynamicShape: Boolean

    /**
     * If the shape of this block should be used to calculate light occlusion.
     */
    @get:JvmName("useShapeForOcclusion")
    val useShapeForOcclusion: Boolean

    /**
     * If this block propagates skylight down.
     */
    @get:JvmName("propagatesSkylightDown")
    val propagatesSkylightDown: Boolean

    /**
     * The amount of light that this block will block from passing through it.
     */
    val lightBlock: Int

    /**
     * If this block is sometimes fully opaque.
     */
    val isConditionallyFullyOpaque: Boolean

    /**
     * If this block is rendered solid.
     */
    val isSolidRender: Boolean

    /**
     * The opacity of this block.
     */
    val opacity: Int

    /**
     * If this block cannot be moved through.
     */
    @get:JvmName("blocksMotion")
    val blocksMotion: Boolean

    /**
     * If this block has gravity.
     */
    @get:JvmName("hasGravity")
    val hasGravity: Boolean

    /**
     * If this block can be respawned inside of.
     */
    @get:JvmName("canRespawnIn")
    val canRespawnIn: Boolean

    /**
     * If the shape used for collision is very large.
     */
    @get:JvmName("hasLargeCollisionShape")
    val hasLargeCollisionShape: Boolean

    /**
     * If this block requires the correct tool to be used to break it.
     */
    @get:JvmName("requiresCorrectTool")
    val requiresCorrectTool: Boolean

    /**
     * The translation component for translating the name
     * of this block.
     */
    val translation: TranslatableComponent

    /**
     * Gets the occlusion shape of the face for the specified [direction].
     */
    fun faceOcclusionShape(direction: Direction): BoundingBox
}
