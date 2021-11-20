/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.util.provide

/**
 * A type of entity.
 *
 * @param T the type of entity
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(EntityTypes::class)
public interface EntityType<T : Entity> : Keyed, TranslationHolder {

    /**
     * The category entities of this type are part of.
     */
    @get:JvmName("category")
    public val category: EntityCategory

    /**
     * If this entity type can be summoned, with the /summon command, or by
     * spawning the entity through [org.kryptonmc.api.world.World.spawnEntity].
     */
    public val isSummonable: Boolean

    /**
     * If entities of this type are immune to all types of fire damage.
     */
    public val isImmuneToFire: Boolean

    /**
     * If entities of this type can be ridden.
     */
    public val isRideable: Boolean

    /**
     * The radius of the circle in which the client will track the movement of
     * entities of this type.
     */
    @get:JvmName("clientTrackingRange")
    public val clientTrackingRange: Int

    /**
     * The interval between when entities of this type will be updated.
     */
    @get:JvmName("updateInterval")
    public val updateInterval: Int

    /**
     * The base dimensions for entities of this type.
     */
    @get:JvmName("dimensions")
    public val dimensions: EntityDimensions

    /**
     * All blocks that entities of this type will not take damage from.
     */
    @get:JvmName("immuneTo")
    public val immuneTo: Set<Block>

    /**
     * The identifier for the loot table that entities of this type will use to
     * determine what drops they will have when they are killed.
     */
    @get:JvmName("lootTable")
    public val lootTable: Key

    /**
     * Returns true if entities of this type are immune (they will not be
     * damaged by) the given [block], or false otherwise.
     *
     * @param block the block to check
     * @return true if entities are immune to the block, false otherwise
     */
    public fun isImmuneTo(block: Block): Boolean

    /**
     * A builder for building entity types.
     */
    public interface Builder<T : Entity> : Buildable.Builder<EntityType<T>> {

        /**
         * Sets the category for the entity type to the given [category].
         *
         * @param category the category
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun category(category: EntityCategory): Builder<T>

        /**
         * Sets whether entities of the type are summonable or not to the given
         * setting [value].
         *
         * @param value the value of the setting
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun summonable(value: Boolean): Builder<T>

        /**
         * Makes entities of the type summonable.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun summonable(): Builder<T> = summonable(true)

        /**
         * Makes entities of the type not summonable.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun notSummonable(): Builder<T> = summonable(false)

        /**
         * Sets whether entities of the type are immune to fire or not to the
         * given setting [value].
         *
         * @param value the value of the setting
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fireImmune(value: Boolean): Builder<T>

        /**
         * Makes entities of the type immune to fire.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fireImmune(): Builder<T> = fireImmune(true)

        /**
         * Sets whether entities of the type can be ridden.
         *
         * @param value the value of the setting
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun rideable(value: Boolean): Builder<T>

        /**
         * Makes entities of the type able to be ridden.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun rideable(): Builder<T> = rideable(true)

        /**
         * Makes entities of the type flammable.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun flammable(): Builder<T> = fireImmune(false)

        /**
         * Sets the client tracking range for entities of the type to the given
         * [range].
         *
         * @param range the range
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun clientTrackingRange(range: Int): Builder<T>

        /**
         * Sets the interval between updates for entities of the type to the
         * given [interval].
         *
         * @param interval the update interval
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun updateInterval(interval: Int): Builder<T>

        /**
         * Sets the base dimensions for the entity type to the given
         * [dimensions].
         *
         * @param dimensions the dimensions
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun dimensions(dimensions: EntityDimensions): Builder<T>

        /**
         * Sets the base dimensions for the entity type to the given [width]
         * and [height].
         *
         * @param width the width
         * @param height the height
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun dimensions(width: Float, height: Float): Builder<T> = dimensions(EntityDimensions.scalable(width, height))

        /**
         * Adds the given [block] to the list of blocks that entities of the
         * type will not be damaged by.
         *
         * @param block the block
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun immuneTo(block: Block): Builder<T>

        /**
         * Adds the given [blocks] to the list of blocks that entities of the
         * type will not be damaged by.
         *
         * @param blocks the blocks
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun immuneTo(vararg blocks: Block): Builder<T>

        /**
         * Adds the given [blocks] to the list of blocks that entities of the
         * type will not be damaged by.
         *
         * @param blocks the blocks
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun immuneTo(blocks: Iterable<Block>): Builder<T>

        /**
         * Sets the identifier for the loot table that entities of the type
         * will use to determine loot.
         *
         * @param identifier the loot table identifier
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun lootTable(identifier: Key): Builder<T>

        /**
         * Sets the translation to use when translating entities of the type
         * client-side.
         *
         * @param translation the translation
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun translation(translation: TranslatableComponent): Builder<T>
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun <T : Entity> of(
            key: Key,
            category: EntityCategory,
            summonable: Boolean,
            fireImmune: Boolean,
            rideable: Boolean,
            trackingRange: Int,
            updateInterval: Int,
            dimensions: EntityDimensions,
            immuneTo: Set<Block>,
            lootTable: Key,
            translation: TranslatableComponent
        ): EntityType<T>

        public fun <T : Entity> builder(key: Key, category: EntityCategory): Builder<T>
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new entity type with the given values.
         *
         * @param key the key
         * @param category the category of the type
         * @param summonable if entities of the type can be summoned
         * @param fireImmune if the type is immune to fire
         * @param clientTrackingRange the range the client will track movement
         * @param updateInterval the interval between updates
         * @param dimensions the base dimensions for the type
         * @param immuneTo all blocks the type is immune to
         * @param lootTable the identifier for the loot table
         * @param translation the translation
         * @return a new entity type
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun <T : Entity> of(
            key: Key,
            category: EntityCategory,
            summonable: Boolean,
            fireImmune: Boolean,
            rideable: Boolean,
            clientTrackingRange: Int,
            updateInterval: Int,
            dimensions: EntityDimensions,
            immuneTo: Set<Block>,
            lootTable: Key,
            translation: TranslatableComponent = Component.translatable(
                "entity.${key.namespace()}.${key.value().replace('/', '.')}"
            )
        ): EntityType<T> = FACTORY.of(
            key,
            category,
            summonable,
            fireImmune,
            rideable,
            clientTrackingRange,
            updateInterval,
            dimensions,
            immuneTo,
            lootTable,
            translation
        )

        /**
         * Creates a new builder for building an entity type with the given
         * values.
         *
         * @param key the key
         * @param category the category
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun <T : Entity> builder(key: Key, category: EntityCategory): Builder<T> = FACTORY.builder(key, category)
    }
}
