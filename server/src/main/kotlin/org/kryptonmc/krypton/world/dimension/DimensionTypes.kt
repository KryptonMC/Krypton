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
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.RegistryFileCodec
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.tags.BlockTags
import org.kryptonmc.krypton.util.KEY_CODEC
import org.kryptonmc.krypton.util.PACKED_Y
import java.util.Optional
import java.util.function.Supplier

object DimensionTypes {

    private const val MINIMUM_COORDINATE_SCALE = 1.0E-5
    private const val MAXIMUM_COORDINATE_SCALE = 3.0E7
    private const val MINIMUM_HEIGHT = 16
    private val Y_SIZE = (1 shl PACKED_Y) - 32
    private val MAX_Y = (Y_SIZE shr 1) - 1
    private val MIN_Y = MAX_Y - Y_SIZE + 1

    private val OVERWORLD_EFFECTS = key("overworld")
    private val THE_NETHER_EFFECTS = key("the_nether")
    private val THE_END_EFFECTS = key("the_end")

    val OVERWORLD_KEY = ResourceKey.of(ResourceKeys.DIMENSION_TYPE, key("overworld"))
    private val OVERWORLD_CAVES_LOCATION = ResourceKey.of(ResourceKeys.DIMENSION_TYPE, key("overworld_caves"))
    val NETHER_KEY = ResourceKey.of(ResourceKeys.DIMENSION_TYPE, key("the_nether"))
    val END_KEY = ResourceKey.of(ResourceKeys.DIMENSION_TYPE, key("the_end"))

    val OVERWORLD = DimensionType(
        false,
        true,
        false,
        true,
        false,
        true,
        true,
        false,
        0F,
        null,
        BlockTags.INFINIBURN_OVERWORLD.name,
        OVERWORLD_EFFECTS,
        0,
        256,
        256,
        1.0
    )
    val OVERWORLD_CAVES = DimensionType(
        false,
        true,
        false,
        true,
        true,
        true,
        true,
        false,
        0F,
        null,
        BlockTags.INFINIBURN_OVERWORLD.name,
        OVERWORLD_EFFECTS,
        0,
        256,
        256,
        1.0
    )
    val THE_NETHER = DimensionType(
        true,
        false,
        true,
        false,
        true,
        false,
        false,
        true,
        0.1F,
        18000L,
        BlockTags.INFINIBURN_NETHER.name,
        THE_NETHER_EFFECTS,
        0,
        256,
        128,
        8.0
    )
    val THE_END = DimensionType(
        false,
        false,
        false,
        false,
        false,
        true,
        false,
        false,
        0F,
        6000L,
        BlockTags.INFINIBURN_END.name,
        THE_END_EFFECTS,
        0,
        256,
        256,
        1.0
    )

    val DIRECT_CODEC: Codec<DimensionType> = RecordCodecBuilder.create<DimensionType?> { instance ->
        instance.group(
            Codec.BOOL.fieldOf("piglin_safe").forGetter(DimensionType::isPiglinSafe),
            Codec.BOOL.fieldOf("natural").forGetter(DimensionType::isNatural),
            Codec.BOOL.fieldOf("ultrawarm").forGetter(DimensionType::isUltrawarm),
            Codec.BOOL.fieldOf("has_skylight").forGetter(DimensionType::hasSkylight),
            Codec.BOOL.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling),
            Codec.BOOL.fieldOf("has_raids").forGetter(DimensionType::hasRaids),
            Codec.BOOL.fieldOf("bed_works").forGetter(DimensionType::bedWorks),
            Codec.BOOL.fieldOf("respawn_anchor_works").forGetter(DimensionType::respawnAnchorWorks),
            Codec.FLOAT.fieldOf("ambient_light").forGetter(DimensionType::ambientLight),
            Codec.LONG.optionalFieldOf("fixed_time").xmap(Optional<Long>::getIfPresent) { Optional.ofNullable(it) }.forGetter(DimensionType::fixedTime),
            KEY_CODEC.fieldOf("infiniburn").forGetter(DimensionType::infiniburn),
            KEY_CODEC.fieldOf("effects").forGetter(DimensionType::effects),
            Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").forGetter(DimensionType::minimumY),
            Codec.intRange(MINIMUM_HEIGHT, Y_SIZE).fieldOf("height").forGetter(DimensionType::height),
            Codec.intRange(0, Y_SIZE).fieldOf("logical_height").forGetter(DimensionType::logicalHeight),
            Codec.doubleRange(MINIMUM_COORDINATE_SCALE, MAXIMUM_COORDINATE_SCALE).fieldOf("coordinate_scale").forGetter(DimensionType::coordinateScale)
        ).apply(instance, ::DimensionType)
    }.comapFlatMap({ it.checkY() }, { it })

    val CODEC: Codec<Supplier<DimensionType>> = RegistryFileCodec(ResourceKeys.DIMENSION_TYPE, DIRECT_CODEC)

    private fun DimensionType.checkY(): DataResult<DimensionType> {
        if (height < MINIMUM_HEIGHT) return DataResult.error("Height has to be at least $MINIMUM_HEIGHT!")
        if (minimumY + height > MAX_Y + 1) return DataResult.error("Minimum Y + height cannot be greater than ${MAX_Y + 1}!")
        if (logicalHeight > height) return DataResult.error("Logical height cannot be greater than height!")
        if (height % 16 != 0) return DataResult.error("Height must be a multiple of 16!")
        if (minimumY % 16 != 0) return DataResult.error("Minimum Y must be a multiple of 16!")
        return DataResult.success(this)
    }

    fun registerBuiltins(holder: RegistryHolder) = holder.apply {
        ownedRegistryOrThrow(ResourceKeys.DIMENSION_TYPE).apply {
            register(OVERWORLD_KEY, OVERWORLD)
            register(OVERWORLD_CAVES_LOCATION, OVERWORLD_CAVES)
            register(NETHER_KEY, THE_NETHER)
            register(END_KEY, THE_END)
        }
    }
}
