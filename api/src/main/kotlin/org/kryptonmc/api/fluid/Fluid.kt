/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A fluid with certain properties.
 *
 * The design of this is very similar to that of the [Block].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Fluids::class)
public interface Fluid : Buildable<Fluid, Fluid.Builder>, PropertyHolder<Fluid>, FluidLike, BlockLike, Keyed {

    /**
     * The ID of this fluid.
     */
    @get:JvmName("id")
    public val id: Int

    /**
     * The ID of the fluid state this fluid represents.
     */
    @get:JvmName("stateId")
    public val stateId: Int

    /**
     * The type of the bucket this fluid can be held in.
     */
    @get:JvmName("bucket")
    public val bucket: ItemType

    /**
     * If this fluid is an empty fluid.
     */
    public val isEmpty: Boolean

    /**
     * The value for this fluid's resistance to explosions.
     */
    @get:JvmName("explosionResistance")
    public val explosionResistance: Double

    /**
     * If this fluid is a source fluid.
     */
    public val isSource: Boolean

    /**
     * The height of this fluid.
     */
    @get:JvmName("height")
    public val height: Float

    /**
     * The level of this fluid.
     *
     * Should be either a constant value, such as 0 for the empty fluid, or 8
     * for source fluids, or the value of the
     * [level][org.kryptonmc.api.block.property.Properties.LIQUID_LEVEL]
     * property for flowing fluids.
     */
    @get:JvmName("level")
    public val level: Int

    /**
     * Compares this fluid to the given [other] fluid.
     *
     * @param other the other fluid
     * @return the result of the comparison
     */
    public fun compare(other: Fluid): Boolean = compare(other, Comparator.ID)

    /**
     * Compares this fluid to the given [other] fluid, using
     * the given [comparator] to perform the comparison.
     *
     * @param other the other fluid
     * @param comparator the comparator to compare the fluids
     * @return the result of the comparison
     */
    public fun compare(other: Fluid, comparator: Comparator): Boolean = comparator(this, other)

    override fun asFluid(): Fluid = this

    override fun asBlock(): Block

    /**
     * A comparator for comparing 2 fluids.
     */
    public fun interface Comparator {

        /**
         * Compares the given [first] fluid with the given [second] fluid.
         *
         * @param first the first fluid
         * @param second the second fluid
         * @return the result of the comparison
         */
        public operator fun invoke(first: Fluid, second: Fluid): Boolean

        public companion object {

            /**
             * A comparator for comparing 2 fluids by their referential
             * equality, meaning both objects must point to the same
             * reference.
             */
            @JvmField
            public val IDENTITY: Comparator = Comparator { first, second -> first === second }

            /**
             * A comparator for comparing 2 fluids by their **fluid** ID,
             * meaning 2 fluids must have the same registry ID.
             */
            @JvmField
            public val ID: Comparator = Comparator { first, second -> first.id == second.id }

            /**
             * A comparator for comparing 2 fluids by their **state** ID,
             * meaning 2 fluids must represent the same state.
             */
            @JvmField
            public val STATE: Comparator = Comparator { first, second -> first.stateId == second.stateId }
        }
    }

    /**
     * A builder for building fluids.
     */
    @FluidDsl
    public interface Builder : Buildable.Builder<Fluid>, PropertyHolder.Builder<Builder, Fluid> {

        /**
         * Sets the ID of the fluid to the given [id].
         *
         * @param id the fluid ID
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun id(id: Int): Builder

        /**
         * Sets the state ID of the fluid to the given [id].
         *
         * @param id the state ID
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun stateId(id: Int): Builder

        /**
         * Sets the bucket item type of the fluid to the given [type].
         *
         * @param type the bucket type
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun bucket(type: ItemType): Builder

        /**
         * Sets whether the fluid is an empty fluid to the given [value].
         *
         * @param value whether the fluid is an empty fluid
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun empty(value: Boolean): Builder

        /**
         * Sets the explosion resistance of the fluid to the given
         * [resistance].
         *
         * @param resistance the explosion resistance
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun resistance(resistance: Double): Builder

        /**
         * Sets whether the fluid is a source fluid to the given [value].
         *
         * @param value whether the fluid is a source fluid
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun source(value: Boolean): Builder

        /**
         * Sets the height of the fluid to the given [height].
         *
         * @param height the height
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun height(height: Float): Builder

        /**
         * Sets the level of the fluid to the given [level].
         *
         * @param level the level
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun level(level: Int): Builder

        /**
         * Sets the block key for the fluid to the given [key].
         *
         * This avoids a possible circular dependency, with blocks requiring
         * the fluid type, and fluids requiring the block type.
         *
         * @param key the key
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun block(key: Key): Builder

        /**
         * Sets the corresponding block for the fluid to the given [block].
         *
         * @param block the corresponding block
         * @return this builder
         */
        @FluidDsl
        @Contract("_ -> this", mutates = "this")
        public fun block(block: Block): Builder = block(block.key())
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(key: Key, id: Int, stateId: Int): Builder

        public fun fromId(id: Int): Fluid?

        public fun fromStateId(id: Int): Fluid?
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a fluid from the given values.
         *
         * @param key the key
         * @param id the ID of the fluid
         * @param stateId the state ID of the fluid
         * @return a new builder
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun builder(key: Key, id: Int, stateId: Int = id): Builder = FACTORY.builder(key, id, stateId)

        /**
         * Gets the fluid with the given block [id], or returns null if there
         * is no registered fluid with the given fluid [id].
         *
         * @param id the fluid ID
         * @return the fluid with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): Fluid? = FACTORY.fromId(id)

        /**
         * Gets the fluid with the given state [id], or returns null if there
         * is no registered fluid with the given state [id].
         *
         * @param id the state ID
         * @return the fluid with the ID, or null if not present
         */
        @JvmStatic
        public fun fromStateId(id: Int): Fluid? = FACTORY.fromStateId(id)
    }
}
