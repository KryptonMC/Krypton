/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.dimension.DimensionEffect
import org.kryptonmc.api.world.dimension.DimensionEffects
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.Vectors
import org.kryptonmc.krypton.util.getIfPresent
import org.kryptonmc.krypton.world.biome.gen.BiomeZoomer
import org.kryptonmc.krypton.world.biome.gen.FuzzyOffsetBiomeZoomer
import java.util.Optional

@JvmRecord
data class KryptonDimensionType(
    private val key: Key,
    override val isPiglinSafe: Boolean,
    override val isNatural: Boolean,
    override val isUltrawarm: Boolean,
    override val hasSkylight: Boolean,
    override val hasCeiling: Boolean,
    override val hasRaids: Boolean,
    override val allowBeds: Boolean,
    override val allowRespawnAnchors: Boolean,
    override val ambientLight: Float,
    override val fixedTime: Long?,
    override val infiniburn: Key,
    override val minimumY: Int,
    override val height: Int,
    override val logicalHeight: Int,
    override val coordinateScale: Double,
    override val effects: DimensionEffect,
    val biomeZoomer: BiomeZoomer = FuzzyOffsetBiomeZoomer
) : DimensionType {

    override fun key(): Key = key

    override fun toBuilder(): DimensionType.Builder = Builder(this)

    class Builder(private val key: Key) : DimensionType.Builder {

        private var piglinSafe = false
        private var natural = false
        private var ultrawarm = false
        private var skylight = false
        private var ceiling = false
        private var raids = false
        private var beds = false
        private var respawnAnchors = false
        private var ambientLight = 0F
        private var fixedTime: Long? = null
        private var infiniburn = KryptonDimensionTypes.OVERWORLD.infiniburn
        private var minimumY = 0
        private var height = 0
        private var logicalHeight = 0
        private var coordinateScale = 0.0
        private var effects = DimensionEffects.OVERWORLD

        constructor(type: DimensionType) : this(type.key()) {
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

        override fun piglinSafe(safe: Boolean): DimensionType.Builder = apply { piglinSafe = safe }

        override fun natural(natural: Boolean): DimensionType.Builder = apply { this.natural = natural }

        override fun ultrawarm(ultrawarm: Boolean): DimensionType.Builder = apply { this.ultrawarm = ultrawarm }

        override fun skylight(light: Boolean): DimensionType.Builder = apply { skylight = light }

        override fun ceiling(ceiling: Boolean): DimensionType.Builder = apply { this.ceiling = ceiling }

        override fun raids(raids: Boolean): DimensionType.Builder = apply { this.raids = raids }

        override fun beds(beds: Boolean): DimensionType.Builder = apply { this.beds = beds }

        override fun respawnAnchors(respawnAnchors: Boolean): DimensionType.Builder = apply { this.respawnAnchors = respawnAnchors }

        override fun ambientLight(light: Float): DimensionType.Builder = apply { ambientLight = light }

        override fun fixedTime(time: Long): DimensionType.Builder = apply { fixedTime = time }

        override fun noFixedTime(): DimensionType.Builder = apply { fixedTime = null }

        override fun infiniburn(infiniburn: Key): DimensionType.Builder = apply { this.infiniburn = infiniburn }

        override fun minimumY(level: Int): DimensionType.Builder = apply { minimumY = level }

        override fun height(level: Int): DimensionType.Builder = apply { height = level }

        override fun logicalHeight(level: Int): DimensionType.Builder = apply { logicalHeight = level }

        override fun coordinateScale(scale: Double): DimensionType.Builder = apply { coordinateScale = scale }

        override fun effects(effects: DimensionEffect): DimensionType.Builder = apply { this.effects = effects }

        override fun build(): DimensionType = KryptonDimensionType(
            key,
            piglinSafe,
            natural,
            ultrawarm,
            skylight,
            ceiling,
            raids,
            beds,
            respawnAnchors,
            ambientLight,
            fixedTime,
            infiniburn,
            minimumY,
            height,
            logicalHeight,
            coordinateScale,
            effects
        )
    }

    object Factory : DimensionType.Factory {

        override fun builder(key: Key): DimensionType.Builder = Builder(key)
    }

    companion object {

        private const val MINIMUM_COORDINATE_SCALE = 1.0E-5
        private const val MAXIMUM_COORDINATE_SCALE = 3.0E7
        private const val MINIMUM_HEIGHT = 16

        val Y_SIZE = (1 shl Vectors.PACKED_Y) - 32
        val MAX_Y = (Y_SIZE shr 1) - 1
        val MIN_Y = MAX_Y - Y_SIZE + 1

        val CODEC: Codec<DimensionType> = RecordCodecBuilder.create<DimensionType> { instance ->
            instance.group(
                Codec.BOOL.fieldOf("piglin_safe").forGetter(DimensionType::isPiglinSafe),
                Codec.BOOL.fieldOf("natural").forGetter(DimensionType::isNatural),
                Codec.BOOL.fieldOf("ultrawarm").forGetter(DimensionType::isUltrawarm),
                Codec.BOOL.fieldOf("has_skylight").forGetter(DimensionType::hasSkylight),
                Codec.BOOL.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling),
                Codec.BOOL.fieldOf("has_raids").forGetter(DimensionType::hasRaids),
                Codec.BOOL.fieldOf("bed_works").forGetter(DimensionType::allowBeds),
                Codec.BOOL.fieldOf("respawn_anchor_works").forGetter(DimensionType::allowRespawnAnchors),
                Codec.FLOAT.fieldOf("ambient_light").forGetter(DimensionType::ambientLight),
                Codec.LONG.optionalFieldOf("fixed_time")
                    .xmap(Optional<Long>::getIfPresent) { Optional.ofNullable(it) }
                    .forGetter(DimensionType::fixedTime),
                Codecs.KEY.fieldOf("infiniburn").forGetter(DimensionType::infiniburn),
                Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").forGetter(DimensionType::minimumY),
                Codec.intRange(MINIMUM_HEIGHT, Y_SIZE).fieldOf("height").forGetter(DimensionType::height),
                Codec.intRange(0, Y_SIZE).fieldOf("logical_height").forGetter(DimensionType::logicalHeight),
                Codec.doubleRange(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE)
                    .fieldOf("coordinate_scale")
                    .forGetter(DimensionType::coordinateScale),
                Codecs.KEY.fieldOf("effects").forGetter { it.effects.key() }
            ).apply(instance) { _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _ -> error("Cannot decode dimension types!") }
        }.comapFlatMap(::checkY) { it }

        private fun checkY(type: DimensionType): DataResult<DimensionType> {
            if (type.height < MINIMUM_HEIGHT) return DataResult.error("Height has to be at least $MINIMUM_HEIGHT!")
            if (type.minimumY + type.height > MAX_Y + 1) return DataResult.error("Minimum Y + height cannot be greater than ${MAX_Y + 1}!")
            if (type.logicalHeight > type.height) return DataResult.error("Logical height cannot be greater than height!")
            if (type.height % 16 != 0) return DataResult.error("Height must be a multiple of 16!")
            if (type.minimumY % 16 != 0) return DataResult.error("Minimum Y must be a multiple of 16!")
            return DataResult.success(type)
        }
    }
}
