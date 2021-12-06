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
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidLike
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * A block with certain properties.
 *
 * These are immutable and do not contain any state-specific
 * information, such as the world or location they are in, so
 * they can be easily reused in many places, which from a
 * technical standpoint, reduces allocations, but also makes
 * them much more thread-safe.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Blocks::class)
public interface Block : Buildable<Block, Block.Builder>, PropertyHolder<Block>, BlockLike, ItemLike, FluidLike, TranslationHolder, Keyed {

    /**
     * The block ID of this block.
     */
    @get:JvmName("id")
    public val id: @Range(from = 0L, to = Int.MAX_VALUE.toLong()) Int

    /**
     * The ID of the block state this block represents.
     */
    @get:JvmName("stateId")
    public val stateId: @Range(from = 0L, to = Int.MAX_VALUE.toLong()) Int

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
    public val lightEmission: @Range(from = 0L, to = 15L) Int

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
     * If this block is solid blocking.
     */
    public val isSolidBlocking: Boolean

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
    public val lightBlock: @Range(from = 0L, to = 15L) Int

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
    public val opacity: @Range(from = 0L, to = 15L) Int

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
     * If the collision shape of this block is a full block.
     */
    public val isCollisionShapeFullBlock: Boolean

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

    /**
     * The reaction this block has to being pushed.
     */
    @get:JvmName("pushReaction")
    public val pushReaction: PushReaction

    override fun asBlock(): Block = this

    /**
     * Compares this block to the given [other] block.
     *
     * @param other the other block
     * @return the result of the comparison
     */
    public fun compare(other: Block): Boolean = compare(other, Comparator.ID)

    /**
     * Compares this block to the given [other] block, using
     * the given [comparator] to perform the comparison.
     *
     * @param other the other block
     * @param comparator the comparator to compare the blocks
     * @return the result of the comparison
     */
    public fun compare(other: Block, comparator: Comparator): Boolean = comparator(this, other)

    /**
     * A comparator for comparing 2 blocks.
     */
    public fun interface Comparator {

        /**
         * Compares the given [first] block with the given [second] block.
         *
         * @param first the first block
         * @param second the second block
         * @return the result of the comparison
         */
        public operator fun invoke(first: Block, second: Block): Boolean

        public companion object {

            /**
             * A comparator for comparing 2 blocks by their referential
             * equality, meaning both objects must point to the same
             * reference.
             */
            @JvmField
            public val IDENTITY: Comparator = Comparator { first, second -> first === second }

            /**
             * A comparator for comparing 2 blocks by their **block** ID,
             * meaning 2 blocks must have the same registry ID.
             */
            @JvmField
            public val ID: Comparator = Comparator { first, second -> first.id == second.id }

            /**
             * A comparator for comparing 2 blocks by their **state** ID,
             * meaning 2 blocks must represent the same state.
             */
            @JvmField
            public val STATE: Comparator = Comparator { first, second -> first.stateId == second.stateId }
        }
    }

    /**
     * A builder for building blocks.
     */
    public interface Builder : Buildable.Builder<Block>, PropertyHolder.Builder<Builder, Block>, TranslationHolder.Builder<Builder, Block> {

        /**
         * Sets the ID of the block to the given [id].
         *
         * @param id the block ID
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun id(id: Int): Builder

        /**
         * Sets the state ID of the block to the given [id].
         *
         * @param id the state ID
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun stateId(id: Int): Builder

        /**
         * Sets the hardness of the block to the given [hardness].
         *
         * @param hardness the hardness
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun hardness(hardness: Double): Builder

        /**
         * Sets the explosion resistance of the block to the given
         * [resistance].
         *
         * @param resistance the explosion resistance
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun resistance(resistance: Double): Builder

        /**
         * Sets the friction of the block to the given [friction].
         *
         * @param friction the friction
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun friction(friction: Double): Builder

        /**
         * Sets the speed factor of the block to the given [factor].
         *
         * @param factor the speed factor
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun speedFactor(factor: Double): Builder

        /**
         * Sets the jump factor of the block to the given [factor].
         *
         * @param factor the jump factor
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun jumpFactor(factor: Double): Builder

        /**
         * Sets whether the block is air to the given setting [value].
         *
         * @param value whether the block is air
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun air(value: Boolean): Builder

        /**
         * Sets whether the block is solid to the given setting [value].
         *
         * @param value whether the block is solid
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun solid(value: Boolean): Builder

        /**
         * Sets whether the block is liquid to the given setting [value].
         *
         * @param value whether the block is liquid
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun liquid(value: Boolean): Builder

        /**
         * Sets whether the block is solid blocking to the given setting
         * [value].
         *
         * @param value whether the block is solid blocking
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun solidBlocking(value: Boolean): Builder

        /**
         * Sets whether the block has a block entity to the given setting
         * [value].
         *
         * @param value whether the block has a block entity
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun hasBlockEntity(value: Boolean): Builder

        /**
         * Sets the light emission of the block to the given [amount].
         *
         * @param amount the light emission
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun lightEmission(amount: Int): Builder

        /**
         * Sets whether the block occludes light to the given setting [value].
         *
         * @param value whether the block occludes light
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun occludes(value: Boolean): Builder

        /**
         * Sets whether the block blocks motion to the given [value].
         *
         * @param value whether the block blocks motion
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun blocksMotion(value: Boolean): Builder

        /**
         * Sets whether the block is flammable to the given setting [value].
         *
         * @param value whether the block is flammable
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun flammable(value: Boolean): Builder

        /**
         * Sets whether the block has gravity to the given [value].
         *
         * @param value whether the block has gravity
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun gravity(value: Boolean): Builder

        /**
         * Sets whether the block is replaceable to the given setting [value].
         *
         * @param value whether the block is replaceable
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun replaceable(value: Boolean): Builder

        /**
         * Sets whether the block has a dynamic shape to the given setting
         * [value].
         *
         * @param value whether the block has a dynamic shape
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun dynamicShape(value: Boolean): Builder

        /**
         * Sets whether the block's shape should be used for light occlusion to
         * the given setting [value].
         *
         * @param value whether the shape should be used for light occlusion
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun useShapeForOcclusion(value: Boolean): Builder

        /**
         * Sets whether the block propagates skylight down to the given setting
         * [value].
         *
         * @param value whether the block propagates skylight down
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun propagatesSkylightDown(value: Boolean): Builder

        /**
         * Sets the amount of light that the block blocks.
         *
         * @param value the amount of light the block blocks
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun lightBlock(value: Int): Builder

        /**
         * Sets whether the block is fully opaque in some contexts to the given
         * [value].
         *
         * @param value whether the block is fully opaque in context
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun conditionallyFullyOpaque(value: Boolean): Builder

        /**
         * Sets whether the render of the block is solid to the given [value].
         *
         * @param value whether the render of the block is solid
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun solidRender(value: Boolean): Builder

        /**
         * Sets the opacity of the block to the given [value].
         *
         * @param value the opacity
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun opacity(value: Int): Builder

        /**
         * Sets whether the block has a larger than normal shape for collision
         * to the given [value].
         *
         * @param value whether the block has a large collision shape
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun largeCollisionShape(value: Boolean): Builder

        /**
         * Sets whether the collision shape for the block is a full block to
         * the given [value].
         *
         * @param value whether the collision shape for the block is full
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun collisionShapeFullBlock(value: Boolean): Builder

        /**
         * Sets whether the block can be respawned in by players to the given
         * [value].
         *
         * @param value whether the block can be respawned in
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun canRespawnIn(value: Boolean): Builder

        /**
         * Sets whether the block requires the correct tool to mine to the
         * given [value].
         *
         * @param value whether the block requires the correct tool
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun requiresCorrectTool(value: Boolean): Builder

        /**
         * Sets the render shape of the block to the given [shape].
         *
         * @param shape the render shape
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun renderShape(shape: RenderShape): Builder

        /**
         * Sets the reaction the block has to being pushed to the given
         * [reaction].
         *
         * @param reaction the push reaction
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun pushReaction(reaction: PushReaction): Builder

        /**
         * Sets the item type key for the block to the given [key].
         *
         * This avoids a possible circular dependency, with items requiring
         * the block type, and blocks requiring the item type.
         *
         * @param key the key
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun item(key: Key): Builder

        /**
         * Sets the corresponding item type for the block to the given [item].
         *
         * @param item the corresponding item type
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun item(item: ItemType): Builder = item(item.key())

        /**
         * Sets the fluid key for the block to the given [key].
         *
         * This avoids a possible circular dependency, with fluids requiring
         * the block type, and blocks requiring the fluid type.
         *
         * @param key the key
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fluid(key: Key): Builder

        /**
         * Sets the corresponding fluid for the block to the given [fluid].
         *
         * @param fluid the corresponding fluid
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fluid(fluid: Fluid): Builder = fluid(fluid.key())
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(key: Key, id: Int, stateId: Int): Builder

        public fun fromId(id: Int): Block?

        public fun fromStateId(id: Int): Block?
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a block from the given values.
         *
         * @param key the key
         * @param id the ID of the block
         * @param stateId the state ID of the block
         * @return a new builder
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun builder(key: Key, id: Int, stateId: Int = id): Builder = FACTORY.builder(key, id, stateId)

        /**
         * Gets the block with the given block [id], or returns null if there
         * is no registered block with the given block [id].
         *
         * @param id the block ID
         * @return the block with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): Block? = FACTORY.fromId(id)

        /**
         * Gets the block with the given state [id], or returns null if there
         * is no registered block with the given state [id].
         *
         * @param id the state ID
         * @return the block with the ID, or null if not present
         */
        @JvmStatic
        public fun fromStateId(id: Int): Block? = FACTORY.fromStateId(id)
    }
}
