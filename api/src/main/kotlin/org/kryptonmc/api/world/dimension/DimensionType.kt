/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.builder.AbstractBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.util.Buildable
import org.kryptonmc.api.util.CataloguedBy
import java.util.OptionalLong

/**
 * Represents data for a dimension.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DimensionTypes::class)
public interface DimensionType : Buildable<DimensionType.Builder, DimensionType>, Keyed {

    /**
     * If [Piglin]s will transform in to [ZombifiedPiglin]s over time.
     */
    public val isPiglinSafe: Boolean

    /**
     * If portals created will spawn [ZombifiedPiglin]s naturally, and if the
     * compass works properly.
     */
    public val isNatural: Boolean

    /**
     * If water will evaporate or wet sponges will become regular sponges when
     * placed, and if laval will flow faster and thinner.
     */
    public val isUltrawarm: Boolean

    /**
     * If there is global lighting (light from the sky).
     */
    @get:JvmName("hasSkylight")
    public val hasSkylight: Boolean

    /**
     * If there is a ceiling made of blocks.
     */
    @get:JvmName("hasCeiling")
    public val hasCeiling: Boolean

    /**
     * If raids will spawn naturally.
     */
    @get:JvmName("hasRaids")
    public val hasRaids: Boolean

    /**
     * If beds can be slept in. If false, beds will explode when used (boom).
     */
    @get:JvmName("allowBeds")
    public val allowBeds: Boolean

    /**
     * If respawn anchors can be charged and used.
     */
    @get:JvmName("allowRespawnAnchors")
    public val allowRespawnAnchors: Boolean

    /**
     * The amount of lighting clients will display when in this dimension.
     */
    @get:JvmName("ambientLight")
    public val ambientLight: Float

    /**
     * The time it will always be. If null, the time will progress normally.
     */
    @get:JvmName("fixedTime")
    public val fixedTime: OptionalLong

    /**
     * The settings used to define which blocks burn infinitely.
     */
    @get:JvmName("infiniburn")
    public val infiniburn: Tag<Block>

    /**
     * The minimum Y level that can be built at.
     */
    @get:JvmName("minimumY")
    public val minimumY: Int

    /**
     * The maximum Y level that can be built at.
     */
    @get:JvmName("height")
    public val height: Int

    /**
     * The maximum logical Y level that can be built at.
     *
     * For example, in the nether, there is a bedrock roof at 128 blocks, so
     * the logical height for the nether is 128, as whilst you can still build
     * above he nether roof, it is not intended for you to do so.
     */
    @get:JvmName("logicalHeight")
    public val logicalHeight: Int

    /**
     * The scale of coordinates. For example, in the nether, the coordinate
     * scale is 8.0, as for every 1 block you walk in the nether, you will walk
     * 8 blocks in dimensions with a coordinate scale of 1.0, such as the
     * overworld and the end.
     */
    @get:JvmName("coordinateScale")
    public val coordinateScale: Double

    /**
     * The location where the client can find the settings for the effects this
     * dimension type will have on the environment.
     */
    @get:JvmName("effects")
    public val effects: Key

    /**
     * The minimum light level that monsters can spawn at.
     */
    @get:JvmName("minimumMonsterSpawnLightLevel")
    public val minimumMonsterSpawnLightLevel: Int

    /**
     * The maximum light level that monsters can spawn at.
     */
    @get:JvmName("maximumMonsterSpawnLightLevel")
    public val maximumMonsterSpawnLightLevel: Int

    /**
     * The minimum block light level that is required for monsters to spawn.
     */
    @get:JvmName("monsterSpawnBlockLightLimit")
    public val monsterSpawnBlockLightLimit: Int

    /**
     * A builder for dimension types.
     */
    @DimensionTypeDsl
    public interface Builder : AbstractBuilder<DimensionType> {

        /**
         * Makes the dimension type safe for piglins.
         *
         * @return this builder
         * @see DimensionType.isPiglinSafe
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun piglinSafe(): Builder = piglinSafe(true)

        /**
         * Makes the dimension type not safe for piglins.
         *
         * This is the opposite of [isPiglinSafe].
         *
         * @return this builder
         * @see DimensionType.isPiglinSafe
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun notPiglinSafe(): Builder = piglinSafe(false)

        /**
         * Sets whether the dimension type is safe for piglins.
         *
         * @param safe whether the dimension type is safe for piglins
         * @return this builder
         * @see DimensionType.isPiglinSafe
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun piglinSafe(safe: Boolean): Builder

        /**
         * Makes the dimension type natural.
         *
         * @return this builder
         * @see DimensionType.isNatural
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun natural(): Builder = natural(true)

        /**
         * Makes the dimension type unnatural.
         *
         * This is the opposite of [isNatural].
         *
         * @return this builder
         * @see DimensionType.isNatural
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun unnatural(): Builder = natural(false)

        /**
         * Sets whether the dimension type is natural.
         *
         * @param natural whether the dimension type is natural
         * @return this builder
         * @see DimensionType.isNatural
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun natural(natural: Boolean): Builder

        /**
         * Makes the dimension type ultrawarm.
         *
         * @return this builder
         * @see DimensionType.isUltrawarm
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun ultrawarm(): Builder = ultrawarm(true)

        /**
         * Makes the dimension type not ultrawarm.
         *
         * This is the opposite of [isUltrawarm].
         *
         * @return this builder
         * @see DimensionType.isUltrawarm
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun notUltrawarm(): Builder = ultrawarm(false)

        /**
         * Sets whether the dimension type is ultrawarm.
         *
         * @param ultrawarm whether the dimension type is ultrawarm
         * @return this builder
         * @see DimensionType.isUltrawarm
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun ultrawarm(ultrawarm: Boolean): Builder

        /**
         * Makes the dimension type have skylight.
         *
         * @return this builder
         * @see DimensionType.hasSkylight
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun skylight(): Builder = skylight(true)

        /**
         * Makes the dimension type not have skylight.
         *
         * This is the opposite of [skylight].
         *
         * @return this builder
         * @see DimensionType.hasSkylight
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noSkylight(): Builder = skylight(false)

        /**
         * Sets whether the dimension type has skylight.
         *
         * @param light whether the dimension type has skylight
         * @return this builder
         * @see DimensionType.hasSkylight
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun skylight(light: Boolean): Builder

        /**
         * Makes the dimension type have a ceiling.
         *
         * @return this builder
         * @see DimensionType.hasCeiling
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun ceiling(): Builder = ceiling(true)

        /**
         * Makes the dimension type have no ceiling.
         *
         * This is the opposite of [ceiling].
         *
         * @return this builder
         * @see DimensionType.hasCeiling
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noCeiling(): Builder = ceiling(false)

        /**
         * Sets whether the dimension type has a ceiling.
         *
         * @param ceiling whether the dimension type has a ceiling
         * @return this builder
         * @see DimensionType.hasCeiling
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun ceiling(ceiling: Boolean): Builder

        /**
         * Makes the dimension type have raids.
         *
         * @return this builder
         * @see DimensionType.hasRaids
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun raids(): Builder = raids(true)

        /**
         * Makes the dimension type not have raids.
         *
         * This is the opposite of [raids].
         *
         * @return this builder
         * @see DimensionType.hasRaids
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noRaids(): Builder = raids(false)

        /**
         * Sets whether the dimension type has raids.
         *
         * @param raids whether the dimension type has raids
         * @return this builder
         * @see DimensionType.hasRaids
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun raids(raids: Boolean): Builder

        /**
         * Makes the dimension type allow beds to be used.
         *
         * @return this builder
         * @see DimensionType.allowBeds
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun allowBeds(): Builder = beds(true)

        /**
         * Makes the dimension type not allow beds to be used.
         *
         * This is the opposite of [allowBeds].
         *
         * @return this builder
         * @see DimensionType.allowBeds
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noBeds(): Builder = beds(false)

        /**
         * Sets whether the dimension type allows beds.
         *
         * @param beds whether the dimension type allows beds
         * @return this builder
         * @see DimensionType.allowBeds
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun beds(beds: Boolean): Builder

        /**
         * Makes the dimension type allow respawn anchors to be used.
         *
         * @return this builder
         * @see DimensionType.allowRespawnAnchors
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun allowRespawnAnchors(): Builder = respawnAnchors(true)

        /**
         * Makes the dimension type not allow respawn anchors to be used.
         *
         * This is the opposite of [allowRespawnAnchors].
         *
         * @return this builder
         * @see DimensionType.allowRespawnAnchors
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noRespawnAnchors(): Builder = respawnAnchors(false)

        /**
         * Sets whether the dimension type allows respawn anchors.
         *
         * @param respawnAnchors whether the dimension type allows respawn
         * anchors
         * @return this builder
         * @see DimensionType.allowRespawnAnchors
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun respawnAnchors(respawnAnchors: Boolean): Builder

        /**
         * Sets the ambient light amount for the dimension type.
         *
         * @param light the light amount
         * @return this builder
         * @see DimensionType.ambientLight
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun ambientLight(light: Float): Builder

        /**
         * Sets the fixed time for the dimension type.
         *
         * @param time the time
         * @return this builder
         * @see DimensionType.fixedTime
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun fixedTime(time: Long): Builder

        /**
         * Makes the dimension type have no fixed time.
         *
         * @return this builder
         * @see DimensionType.fixedTime
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun noFixedTime(): Builder

        /**
         * Sets the infiniburn settings for the dimension type.
         *
         * @param infiniburn the infiniburn settings
         * @return this builder
         * @see DimensionType.infiniburn
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun infiniburn(infiniburn: Tag<Block>): Builder

        /**
         * Sets the minimum Y level for the dimension type.
         *
         * @param level the level
         * @return this builder
         * @see DimensionType.minimumY
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun minimumY(level: Int): Builder

        /**
         * Sets the maximum Y level (height) for the dimension type.
         *
         * @param level the level
         * @return this builder
         * @see DimensionType.height
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun height(level: Int): Builder

        /**
         * Sets the maximum logical Y level (height) for the dimension type.
         *
         * @param level the level
         * @return this builder
         * @see DimensionType.logicalHeight
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun logicalHeight(level: Int): Builder

        /**
         * Sets the coordinate scale for the dimension type.
         *
         * @param scale the scale
         * @return this builder
         * @see DimensionType.coordinateScale
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun coordinateScale(scale: Double): Builder

        /**
         * Sets the location that will be used by the client to look up the
         * effects for the dimension type.
         *
         * @param effects the effects settings location
         * @return this builder
         * @see DimensionType.effects
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(effects: Key): Builder

        /**
         * Sets the minimum monster spawn light level for the dimension type to
         * the given [level].
         *
         * @param level the level
         * @return this builder
         * @see DimensionType.minimumMonsterSpawnLightLevel
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun minimumMonsterSpawnLightLevel(level: Int): Builder

        /**
         * Sets the maximum monster spawn light level for the dimension type to
         * the given [level].
         *
         * @param level the level
         * @return this builder
         * @see DimensionType.maximumMonsterSpawnLightLevel
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun maximumMonsterSpawnLightLevel(level: Int): Builder

        /**
         * Sets the light level range at which monsters will spawn for the
         * dimension type to the given [minimum] and [maximum].
         *
         * @param minimum the minimum level
         * @param maximum the maximum level
         * @return this builder
         * @see minimumMonsterSpawnLightLevel
         * @see maximumMonsterSpawnLightLevel
         */
        @DimensionTypeDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun monsterSpawnLightLevels(minimum: Int, maximum: Int): Builder = apply {
            minimumMonsterSpawnLightLevel(minimum)
            maximumMonsterSpawnLightLevel(maximum)
        }

        /**
         * Sets the monster spawn block light limit for the dimension type to
         * the given [limit].
         *
         * @param limit the limit
         * @return this builder
         * @see DimensionType.monsterSpawnBlockLightLimit
         */
        @DimensionTypeDsl
        @Contract("_ -> this", mutates = "this")
        public fun monsterSpawnBlockLightLimit(limit: Int): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building dimension types.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}
