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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.util.provider.ConstantInt
import org.kryptonmc.krypton.util.provider.IntProvider
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.asOptionalLong
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.OptionalLong

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
    override val infiniburn: Tag<Block>,
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

    override fun key(): Key = Registries.DIMENSION_TYPE[this] ?: UNREGISTERED_KEY

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
            val CODEC: MapCodec<MonsterSettings> = RecordCodecBuilder.createMap {
                it.group(
                    Codec.BOOLEAN.field("piglin_safe").getting(MonsterSettings::piglinSafe),
                    Codec.BOOLEAN.field("has_raids").getting(MonsterSettings::hasRaids),
                    IntProvider.codec(0, 15).field("monster_spawn_light_level").getting(MonsterSettings::monsterSpawnLightLevel),
                    Codec.intRange(0, 15).field("monster_spawn_block_light_limit").getting(MonsterSettings::monsterSpawnBlockLightLimit)
                ).apply(it, ::MonsterSettings)
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

        override fun piglinSafe(safe: Boolean): Builder = apply { piglinSafe = safe }

        override fun natural(natural: Boolean): Builder = apply { this.natural = natural }

        override fun ultrawarm(ultrawarm: Boolean): Builder = apply { this.ultrawarm = ultrawarm }

        override fun skylight(light: Boolean): Builder = apply { skylight = light }

        override fun ceiling(ceiling: Boolean): Builder = apply { this.ceiling = ceiling }

        override fun raids(raids: Boolean): Builder = apply { this.raids = raids }

        override fun beds(beds: Boolean): Builder = apply { this.beds = beds }

        override fun respawnAnchors(respawnAnchors: Boolean): Builder = apply { this.respawnAnchors = respawnAnchors }

        override fun ambientLight(light: Float): Builder = apply { ambientLight = light }

        override fun fixedTime(time: Long): Builder = apply { fixedTime = OptionalLong.of(time) }

        override fun noFixedTime(): Builder = apply { fixedTime = OptionalLong.empty() }

        override fun infiniburn(infiniburn: Tag<Block>): Builder = apply { this.infiniburn = infiniburn }

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

        override fun build(): KryptonDimensionType = KryptonDimensionType(
            fixedTime,
            skylight,
            ceiling,
            ultrawarm,
            natural,
            coordinateScale,
            beds,
            respawnAnchors,
            minimumY,
            height,
            logicalHeight,
            infiniburn,
            effects,
            ambientLight,
            MonsterSettings(piglinSafe, raids, createSpawnLightLevelProvider(), monsterSpawnBlockLightLimit)
        )

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
        @Suppress("UNCHECKED_CAST")
        val DIRECT_CODEC: Codec<DimensionType> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.LONG.optionalField("fixed_time").asOptionalLong().getting(DimensionType::fixedTime),
                Codec.BOOLEAN.field("has_skylight").getting(DimensionType::hasSkylight),
                Codec.BOOLEAN.field("has_ceiling").getting(DimensionType::hasCeiling),
                Codec.BOOLEAN.field("ultrawarm").getting(DimensionType::isUltrawarm),
                Codec.BOOLEAN.field("natural").getting(DimensionType::isNatural),
                Codec.doubleRange(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE).field("coordinate_scale")
                    .getting(DimensionType::coordinateScale),
                Codec.BOOLEAN.field("bed_works").getting(DimensionType::allowBeds),
                Codec.BOOLEAN.field("respawn_anchor_works").getting(DimensionType::allowRespawnAnchors),
                Codec.intRange(MIN_Y, MAX_Y).field("min_y").getting(DimensionType::minimumY),
                Codec.intRange(MINIMUM_HEIGHT, Y_SIZE).field("height").getting(DimensionType::height),
                Codec.intRange(0, Y_SIZE).field("logical_height").getting(DimensionType::logicalHeight),
                // TODO: This codec is complete rubbish, but it'll be changed in the next version when we rewrite the tag API
                Codecs.KEY.xmap({ KryptonTagManager.tags[Registries.TAG_TYPES[it]]!![0] as Tag<Block> }, { it.key() }).field("infiniburn")
                    .getting(DimensionType::infiniburn),
                Codecs.KEY.field("effects").getting(DimensionType::effects),
                Codec.FLOAT.field("ambient_light").getting(DimensionType::ambientLight),
                MonsterSettings.CODEC.getting { (it as KryptonDimensionType).monsterSettings }
            ).apply(instance, ::KryptonDimensionType)
        }
    }
}
