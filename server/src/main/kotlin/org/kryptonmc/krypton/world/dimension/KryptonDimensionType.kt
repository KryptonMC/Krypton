/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.tags.KryptonTagKey
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.provider.ConstantInt
import org.kryptonmc.krypton.util.provider.IntProvider
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.Dynamic
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.OptionalLong
import kotlin.math.cos

@JvmRecord
data class KryptonDimensionType(
    override val fixedTime: OptionalLong,
    override val hasSkylight: Boolean,
    override val hasCeiling: Boolean,
    override val isUltrawarm: Boolean,
    override val isNatural: Boolean,
    override val coordinateScale: Double,
    override val allowBeds: Boolean,
    override val allowRespawnAnchors: Boolean,
    override val minimumY: Int,
    override val height: Int,
    override val logicalHeight: Int,
    override val infiniburn: TagKey<Block>,
    override val effects: Key,
    override val ambientLight: Float,
    val monsterSettings: MonsterSettings
) : DimensionType {

    override val isPiglinSafe: Boolean
        get() = monsterSettings.piglinSafe
    override val hasRaids: Boolean
        get() = monsterSettings.hasRaids
    override val minimumMonsterSpawnLightLevel: Int
        get() = monsterSettings.monsterSpawnLightLevel.minimumValue
    override val maximumMonsterSpawnLightLevel: Int
        get() = monsterSettings.monsterSpawnLightLevel.maximumValue
    override val monsterSpawnBlockLightLimit: Int
        get() = monsterSettings.monsterSpawnBlockLightLimit

    init {
        check(height >= MINIMUM_HEIGHT) { "Height must be at least $MINIMUM_HEIGHT!" }
        check(minimumY + height <= MAX_Y + 1) { "Minimum Y + height cannot be higher than ${MAX_Y + 1}!" }
        check(logicalHeight <= height) { "Logical height cannot be higher than height!" }
        check(height % 16 == 0) { "Height must be a multiple of 16!" }
        check(minimumY % 16 == 0) { "Minimum Y must be a multiple of 16!" }
    }

    fun timeOfDay(dayTime: Long): Float {
        val fraction = Maths.fraction(fixedTime.orElse(dayTime).toDouble() / 24000.0 - 0.25)
        val offset = 0.5 - cos(fraction * Math.PI) / 2.0
        return (fraction * 2.0 + offset).toFloat() / 3F
    }

    fun moonPhase(dayTime: Long): Int = (dayTime / 24000L % 8L + 8L).toInt() % 8

    override fun key(): Key = KryptonRegistries.DIMENSION_TYPE.getKey(this) ?: UNREGISTERED_KEY

    override fun toBuilder(): Builder = Builder(this)

    @JvmRecord
    data class MonsterSettings(
        val piglinSafe: Boolean,
        val hasRaids: Boolean,
        val monsterSpawnLightLevel: IntProvider,
        val monsterSpawnBlockLightLimit: Int
    ) {

        companion object {

            @JvmField
            val CODEC: MapCodec<MonsterSettings> = RecordCodecBuilder.createMap { instance ->
                instance.group(
                    Codec.BOOLEAN.fieldOf("piglin_safe").getting { it.piglinSafe },
                    Codec.BOOLEAN.fieldOf("has_raids").getting { it.hasRaids },
                    IntProvider.codec(0, 15).fieldOf("monster_spawn_light_level").getting { it.monsterSpawnLightLevel },
                    Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit").getting { it.monsterSpawnBlockLightLimit }
                ).apply(instance) { piglinSafe, raids, level, limit -> MonsterSettings(piglinSafe, raids, level, limit) }
            }
        }
    }

    class Builder() : DimensionType.Builder {

        private var piglinSafe = false
        private var natural = false
        private var ultrawarm = false
        private var skylight = false
        private var ceiling = false
        private var raids = false
        private var beds = false
        private var respawnAnchors = false
        private var ambientLight = 0F
        private var fixedTime = OptionalLong.empty()
        private var infiniburn = BlockTags.INFINIBURN_OVERWORLD
        private var minimumY = 0
        private var height = 0
        private var logicalHeight = 0
        private var coordinateScale = 1.0
        private var effects = KryptonDimensionTypes.OVERWORLD_EFFECTS
        private var minimumMonsterSpawnLightLevel = 0
        private var maximumMonsterSpawnLightLevel = 0
        private var monsterSpawnBlockLightLimit = 0

        constructor(type: DimensionType) : this() {
            piglinSafe = type.isPiglinSafe
            natural = type.isNatural
            ultrawarm = type.isUltrawarm
            skylight = type.hasSkylight
            ceiling = type.hasCeiling
            raids = type.hasRaids
            beds = type.allowBeds
            respawnAnchors = type.allowRespawnAnchors
            ambientLight = type.ambientLight
            fixedTime = type.fixedTime
            infiniburn = type.infiniburn
            minimumY = type.minimumY
            height = type.height
            logicalHeight = type.logicalHeight
            coordinateScale = type.coordinateScale
            effects = type.effects
        }

        override fun piglinSafe(): Builder = apply { piglinSafe = true }

        override fun natural(): Builder = apply { natural = true }

        override fun ultrawarm(): Builder = apply { ultrawarm = true }

        override fun skylight(): Builder = apply { skylight = true }

        override fun ceiling(): Builder = apply { ceiling = true }

        override fun raids(): Builder = apply { raids = true }

        override fun beds(): Builder = apply { beds = true }

        override fun respawnAnchors(): Builder = apply { respawnAnchors = true }

        override fun ambientLight(light: Float): Builder = apply { ambientLight = light }

        override fun fixedTime(time: Long): Builder = apply { fixedTime = OptionalLong.of(time) }

        override fun noFixedTime(): Builder = apply { fixedTime = OptionalLong.empty() }

        override fun infiniburn(infiniburn: TagKey<Block>): Builder = apply { this.infiniburn = infiniburn }

        override fun minimumY(level: Int): Builder = apply { minimumY = level }

        override fun height(level: Int): Builder = apply {
            height = level
            logicalHeight = level
        }

        override fun logicalHeight(level: Int): Builder = apply { logicalHeight = level }

        override fun coordinateScale(scale: Double): Builder = apply { coordinateScale = scale }

        override fun effects(effects: Key): Builder = apply { this.effects = effects }

        override fun minimumMonsterSpawnLightLevel(level: Int): Builder = apply { minimumMonsterSpawnLightLevel = level }

        override fun maximumMonsterSpawnLightLevel(level: Int): Builder = apply { maximumMonsterSpawnLightLevel = level }

        override fun monsterSpawnBlockLightLimit(limit: Int): Builder = apply { monsterSpawnBlockLightLimit = limit }

        override fun build(): KryptonDimensionType {
            val monsterSettings = MonsterSettings(piglinSafe, raids, createSpawnLightLevelProvider(), monsterSpawnBlockLightLimit)
            return KryptonDimensionType(fixedTime, skylight, ceiling, ultrawarm, natural, coordinateScale, beds, respawnAnchors, minimumY, height,
                logicalHeight, infiniburn, effects, ambientLight, monsterSettings)
        }

        private fun createSpawnLightLevelProvider(): IntProvider {
            if (minimumMonsterSpawnLightLevel == maximumMonsterSpawnLightLevel) return ConstantInt.of(minimumMonsterSpawnLightLevel)
            return UniformInt(minimumMonsterSpawnLightLevel, maximumMonsterSpawnLightLevel)
        }
    }

    object Factory : DimensionType.Factory {

        override fun builder(): DimensionType.Builder = Builder()
    }

    companion object {

        private val UNREGISTERED_KEY = Key.key("krypton", "unregistered_dimension_type")
        private const val MINIMUM_COORDINATE_SCALE = 1.0E-5
        private const val MAXIMUM_COORDINATE_SCALE = 3.0E7
        private const val MINIMUM_HEIGHT = 16

        // The number of bits used to encode the Y value of a block position in to a long. See https://wiki.vg/Protocol#Position
        private const val ENCODED_Y_BITS = 12
        private const val Y_SIZE = (1 shl ENCODED_Y_BITS) - 32
        private const val MAX_Y = (Y_SIZE shr 1) - 1
        private const val MIN_Y = MAX_Y - Y_SIZE + 1
        @JvmField
        val DIRECT_CODEC: Codec<DimensionType> = Codecs.catchDecoderException(RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.asOptionalLong(Codec.LONG.optionalFieldOf("fixed_time")).getting { it.fixedTime },
                Codec.BOOLEAN.fieldOf("has_skylight").getting { it.hasSkylight },
                Codec.BOOLEAN.fieldOf("has_ceiling").getting { it.hasCeiling },
                Codec.BOOLEAN.fieldOf("ultrawarm").getting { it.isUltrawarm },
                Codec.BOOLEAN.fieldOf("natural").getting { it.isNatural },
                Codec.doubleRange(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE).fieldOf("coordinate_scale").getting { it.coordinateScale },
                Codec.BOOLEAN.fieldOf("bed_works").getting { it.allowBeds },
                Codec.BOOLEAN.fieldOf("respawn_anchor_works").getting { it.allowRespawnAnchors },
                Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").getting { it.minimumY },
                Codec.intRange(MINIMUM_HEIGHT, Y_SIZE).fieldOf("height").getting { it.height },
                Codec.intRange(0, Y_SIZE).fieldOf("logical_height").getting { it.logicalHeight },
                KryptonTagKey.hashedCodec(ResourceKeys.BLOCK).fieldOf("infiniburn").getting { it.infiniburn },
                Codecs.KEY.fieldOf("effects").orElse(KryptonDimensionTypes.OVERWORLD_EFFECTS).getting { it.effects },
                Codec.FLOAT.fieldOf("ambient_light").getting { it.ambientLight },
                MonsterSettings.CODEC.getting { (it as KryptonDimensionType).monsterSettings }
            ).apply(instance, ::KryptonDimensionType)
        })
        @JvmField
        val MOON_BRIGHTNESS_PER_PHASE: FloatArray = floatArrayOf(1F, 0.75F, 0.5F, 0.25F, 0F, 0.25F, 0.5F, 0.75F)

        @JvmStatic
        fun parseLegacy(data: Dynamic<*>): DataResult<ResourceKey<World>> {
            val number = data.asNumber().result()
            if (number.isPresent) {
                when (number.get().toInt()) {
                    -1 -> DataResult.success(World.NETHER)
                    0 -> DataResult.success(World.OVERWORLD)
                    1 -> DataResult.success(World.END)
                }
            }
            return Codecs.DIMENSION.read(data)
        }
    }
}
