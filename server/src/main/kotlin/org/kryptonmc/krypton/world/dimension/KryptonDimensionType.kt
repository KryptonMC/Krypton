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
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.world.dimension.DimensionEffect
import org.kryptonmc.api.world.dimension.DimensionEffects
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound

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
    override val effects: DimensionEffect
) : DimensionType {

    override fun key(): Key = key

    override fun toBuilder(): Builder = Builder(this)

    class Builder(private var key: Key) : DimensionType.Builder {

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
        private var infiniburn = BlockTags.INFINIBURN_OVERWORLD.key()
        private var minimumY = 0
        private var height = 0
        private var logicalHeight = 0
        private var coordinateScale = 1.0
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

        override fun key(key: Key): Builder = apply { this.key = key }

        override fun piglinSafe(safe: Boolean): Builder = apply { piglinSafe = safe }

        override fun natural(natural: Boolean): Builder = apply { this.natural = natural }

        override fun ultrawarm(ultrawarm: Boolean): Builder = apply { this.ultrawarm = ultrawarm }

        override fun skylight(light: Boolean): Builder = apply { skylight = light }

        override fun ceiling(ceiling: Boolean): Builder = apply { this.ceiling = ceiling }

        override fun raids(raids: Boolean): Builder = apply { this.raids = raids }

        override fun beds(beds: Boolean): Builder = apply { this.beds = beds }

        override fun respawnAnchors(respawnAnchors: Boolean): Builder = apply { this.respawnAnchors = respawnAnchors }

        override fun ambientLight(light: Float): Builder = apply { ambientLight = light }

        override fun fixedTime(time: Long): Builder = apply { fixedTime = time }

        override fun noFixedTime(): Builder = apply { fixedTime = null }

        override fun infiniburn(infiniburn: Key): Builder = apply { this.infiniburn = infiniburn }

        override fun minimumY(level: Int): Builder = apply { minimumY = level }

        override fun height(level: Int): Builder = apply {
            height = level
            logicalHeight = level
        }

        override fun logicalHeight(level: Int): Builder = apply { logicalHeight = level }

        override fun coordinateScale(scale: Double): Builder = apply { coordinateScale = scale }

        override fun effects(effects: DimensionEffect): Builder = apply { this.effects = effects }

        override fun build(): KryptonDimensionType = KryptonDimensionType(
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

        // The number of bits used to encode the Y value of a block position in to a long. See https://wiki.vg/Protocol#Position
        private const val ENCODED_Y_BITS = 12
        private const val Y_SIZE = (1 shl ENCODED_Y_BITS) - 32
        private const val MAX_Y = (Y_SIZE shr 1) - 1
        private const val MIN_Y = MAX_Y - Y_SIZE + 1
        private val MINIMUM_Y_CODEC = Codecs.range(MIN_Y, MAX_Y)
        private val HEIGHT_CODEC = Codecs.range(MINIMUM_HEIGHT, Y_SIZE)
        private val LOGICAL_HEIGHT_CODEC = Codecs.range(0, Y_SIZE)
        private val COORDINATE_SCALE_CODEC = Codecs.range(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE)

        @JvmField
        val ENCODER: CompoundEncoder<DimensionType> = CompoundEncoder {
            compound {
                encode(Codecs.BOOLEAN, "piglin_safe", it.isPiglinSafe)
                encode(Codecs.BOOLEAN, "natural", it.isNatural)
                encode(Codecs.BOOLEAN, "ultrawarm", it.isUltrawarm)
                encode(Codecs.BOOLEAN, "has_skylight", it.hasSkylight)
                encode(Codecs.BOOLEAN, "has_ceiling", it.hasCeiling)
                encode(Codecs.BOOLEAN, "has_raids", it.hasRaids)
                encode(Codecs.BOOLEAN, "bed_works", it.allowBeds)
                encode(Codecs.BOOLEAN, "respawn_anchor_works", it.allowRespawnAnchors)
                encode(Codecs.FLOAT, "ambient_light", it.ambientLight)
                encode(Codecs.LONG, "fixed_time", it.fixedTime)
                encode(Codecs.KEY, "infiniburn", it.infiniburn)
                encode(MINIMUM_Y_CODEC, "min_y", it.minimumY)
                encode(HEIGHT_CODEC, "height", it.height)
                encode(LOGICAL_HEIGHT_CODEC, "logical_height", it.logicalHeight)
                encode(COORDINATE_SCALE_CODEC, "coordinate_scale", it.coordinateScale)
                encode(Codecs.KEY, "effects", it.effects.key())
            }
        }
    }
}
