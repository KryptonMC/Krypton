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
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.world.dimension.DimensionEffect

object KryptonDimensionTypes {

    @JvmField val OVERWORLD: KryptonDimensionType = register(
        "overworld",
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
        BlockTags.INFINIBURN_OVERWORLD.key(),
        -64,
        384,
        384,
        1.0,
        KryptonDimensionEffects.OVERWORLD
    )
    @JvmField val OVERWORLD_CAVES: KryptonDimensionType = register(
        "overworld_caves",
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
        BlockTags.INFINIBURN_OVERWORLD.key(),
        -64,
        384,
        384,
        1.0,
        KryptonDimensionEffects.OVERWORLD
    )
    @JvmField val THE_NETHER: KryptonDimensionType = register(
        "the_nether",
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
        BlockTags.INFINIBURN_NETHER.key(),
        0,
        256,
        128,
        8.0,
        KryptonDimensionEffects.THE_NETHER
    )
    @JvmField val THE_END: KryptonDimensionType = register(
        "the_end",
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
        BlockTags.INFINIBURN_END.key(),
        0,
        256,
        256,
        1.0,
        KryptonDimensionEffects.THE_END
    )

    @JvmStatic
    private fun register(
        name: String,
        isPiglinSafe: Boolean,
        isNatural: Boolean,
        isUltrawarm: Boolean,
        hasSkylight: Boolean,
        hasCeiling: Boolean,
        hasRaids: Boolean,
        bedWorks: Boolean,
        respawnAnchorWorks: Boolean,
        ambientLight: Float,
        fixedTime: Long?,
        infiniburn: Key,
        minimumY: Int,
        height: Int,
        logicalHeight: Int,
        coordinateScale: Double,
        effects: DimensionEffect
    ): KryptonDimensionType {
        val key = key(name)
        return Registries.DIMENSION_TYPE.register(
            key,
            KryptonDimensionType(
                key,
                isPiglinSafe,
                isNatural,
                isUltrawarm,
                hasSkylight,
                hasCeiling,
                hasRaids,
                bedWorks,
                respawnAnchorWorks,
                ambientLight,
                fixedTime,
                infiniburn,
                minimumY,
                height,
                logicalHeight,
                coordinateScale,
                effects
            )
        )
    }
}
