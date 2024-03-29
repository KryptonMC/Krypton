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
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.builder.AbstractBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.DimensionTypeDsl
import java.util.OptionalLong

/**
 * Represents data for a dimension.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(DimensionTypes::class)
@ImmutableType
public interface DimensionType : Keyed {

    /**
     * If piglins will transform in to zombified piglins over time.
     */
    public val isPiglinSafe: Boolean

    /**
     * If portals created will spawn zombified piglins naturally, and if the
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
    public val infiniburn: TagKey<Block>

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
        public fun piglinSafe(): Builder

        /**
         * Makes the dimension type natural.
         *
         * @return this builder
         * @see DimensionType.isNatural
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun natural(): Builder

        /**
         * Makes the dimension type ultrawarm.
         *
         * @return this builder
         * @see DimensionType.isUltrawarm
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun ultrawarm(): Builder

        /**
         * Makes the dimension type have skylight.
         *
         * @return this builder
         * @see DimensionType.hasSkylight
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun skylight(): Builder

        /**
         * Makes the dimension type have a ceiling.
         *
         * @return this builder
         * @see DimensionType.hasCeiling
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun ceiling(): Builder

        /**
         * Makes the dimension type have raids.
         *
         * @return this builder
         * @see DimensionType.hasRaids
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun raids(): Builder

        /**
         * Makes the dimension type allow beds to be used.
         *
         * @return this builder
         * @see DimensionType.allowBeds
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun beds(): Builder

        /**
         * Makes the dimension type allow respawn anchors to be used.
         *
         * @return this builder
         * @see DimensionType.allowRespawnAnchors
         */
        @DimensionTypeDsl
        @Contract("-> this", mutates = "this")
        public fun respawnAnchors(): Builder

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
        public fun infiniburn(infiniburn: TagKey<Block>): Builder

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
    @TypeFactory
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
