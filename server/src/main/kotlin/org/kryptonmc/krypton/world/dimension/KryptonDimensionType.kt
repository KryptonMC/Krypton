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
import com.mojang.serialization.Dynamic
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.api.util.log2
import org.kryptonmc.api.util.roundUpPow2
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.frac
import org.kryptonmc.krypton.world.biome.gen.BiomeZoomer
import org.kryptonmc.krypton.world.biome.gen.FuzzyOffsetBiomeZoomer
import java.util.Optional
import kotlin.math.PI
import kotlin.math.cos

data class KryptonDimensionType(
    override val isPiglinSafe: Boolean,
    override val isNatural: Boolean,
    override val isUltrawarm: Boolean,
    override val hasSkylight: Boolean,
    override val hasCeiling: Boolean,
    override val hasRaids: Boolean,
    override val bedWorks: Boolean,
    override val respawnAnchorWorks: Boolean,
    override val ambientLight: Float,
    override val fixedTime: Long?,
    override val infiniburn: Key,
    override val minimumY: Int,
    override val height: Int,
    override val logicalHeight: Int,
    override val coordinateScale: Double,
    val effects: Key,
    val biomeZoomer: BiomeZoomer = FuzzyOffsetBiomeZoomer
) : DimensionType {

    fun timeOfDay(time: Long): Float {
        val frac = ((fixedTime ?: time).toDouble() / 24000.0 - 0.25).frac()
        val cos = 0.5 - cos(frac * PI) / 2.0
        return (frac * 2.0 + cos).toFloat() / 3F
    }

    fun moonPhase(time: Long) = (time / 24000L % MOON_PHASES + MOON_PHASES).toInt() % MOON_PHASES

    companion object {

        private const val MINIMUM_COORDINATE_SCALE = 1.0E-5
        private const val MAXIMUM_COORDINATE_SCALE = 3.0E7
        private const val MINIMUM_HEIGHT = 16

        private val PACKED_Y = 64 - (1 + 30000000.roundUpPow2().log2()) * 2
        val Y_SIZE = (1 shl PACKED_Y) - 32
        val MAX_Y = (Y_SIZE shr 1) - 1
        val MIN_Y = MAX_Y - Y_SIZE + 1

        const val MOON_PHASES = 8
        val MOON_BRIGHTNESS_BY_PHASE = floatArrayOf(1F, 0.75F, 0.5F, 0.25F, 0F, 0.25F, 0.5F, 0.75F)

        val CODEC: Codec<KryptonDimensionType> = RecordCodecBuilder.create<KryptonDimensionType> { instance ->
            instance.group(
                Codec.BOOL.fieldOf("piglin_safe").forGetter(KryptonDimensionType::isPiglinSafe),
                Codec.BOOL.fieldOf("natural").forGetter(KryptonDimensionType::isNatural),
                Codec.BOOL.fieldOf("ultrawarm").forGetter(KryptonDimensionType::isUltrawarm),
                Codec.BOOL.fieldOf("has_skylight").forGetter(KryptonDimensionType::hasSkylight),
                Codec.BOOL.fieldOf("has_ceiling").forGetter(KryptonDimensionType::hasCeiling),
                Codec.BOOL.fieldOf("has_raids").forGetter(KryptonDimensionType::hasRaids),
                Codec.BOOL.fieldOf("bed_works").forGetter(KryptonDimensionType::bedWorks),
                Codec.BOOL.fieldOf("respawn_anchor_works").forGetter(KryptonDimensionType::respawnAnchorWorks),
                Codec.FLOAT.fieldOf("ambient_light").forGetter(KryptonDimensionType::ambientLight),
                Codec.LONG.optionalFieldOf("fixed_time").xmap(Optional<Long>::getIfPresent) { Optional.ofNullable(it) }.forGetter(KryptonDimensionType::fixedTime),
                Codecs.KEY.fieldOf("infiniburn").forGetter(KryptonDimensionType::infiniburn),
                Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").forGetter(KryptonDimensionType::minimumY),
                Codec.intRange(MINIMUM_HEIGHT, Y_SIZE).fieldOf("height").forGetter(KryptonDimensionType::height),
                Codec.intRange(0, Y_SIZE).fieldOf("logical_height").forGetter(KryptonDimensionType::logicalHeight),
                Codec.doubleRange(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE).fieldOf("coordinate_scale").forGetter(KryptonDimensionType::coordinateScale),
                Codecs.KEY.fieldOf("effects").forGetter(KryptonDimensionType::effects)
            ).apply(instance, ::KryptonDimensionType)
        }.comapFlatMap(::checkY) { it }

        private fun checkY(type: KryptonDimensionType): DataResult<KryptonDimensionType> {
            if (type.height < MINIMUM_HEIGHT) return DataResult.error("Height has to be at least $MINIMUM_HEIGHT!")
            if (type.minimumY + type.height > MAX_Y + 1) return DataResult.error("Minimum Y + height cannot be greater than ${MAX_Y + 1}!")
            if (type.logicalHeight > type.height) return DataResult.error("Logical height cannot be greater than height!")
            if (type.height % 16 != 0) return DataResult.error("Height must be a multiple of 16!")
            if (type.minimumY % 16 != 0) return DataResult.error("Minimum Y must be a multiple of 16!")
            return DataResult.success(type)
        }
    }
}
