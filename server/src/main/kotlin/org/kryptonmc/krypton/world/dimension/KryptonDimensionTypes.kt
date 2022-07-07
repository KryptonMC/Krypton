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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.krypton.util.provider.ConstantIntProvider
import org.kryptonmc.krypton.util.provider.UniformIntProvider

object KryptonDimensionTypes {

    @JvmField
    val OVERWORLD: KryptonDimensionType = register("overworld") {
        natural()
        skylight()
        raids()
        allowBeds()
        allowRespawnAnchors()
        infiniburn(BlockTags.INFINIBURN_OVERWORLD)
        minimumY(-64)
        height(384)
        effects(KryptonDimensionEffects.OVERWORLD)
        monsterSpawnLightLevel(UniformIntProvider(0, 7))
        monsterSpawnBlockLightLimit(0)
    }
    @JvmField
    val OVERWORLD_CAVES: KryptonDimensionType = register(Key.key("overworld_caves"), OVERWORLD.toBuilder().ceiling(true))
    @JvmField
    val THE_NETHER: KryptonDimensionType = register("the_nether") {
        piglinSafe()
        ultrawarm()
        ceiling()
        allowRespawnAnchors()
        ambientLight(0.1F)
        fixedTime(18000L)
        infiniburn(BlockTags.INFINIBURN_NETHER)
        height(256)
        logicalHeight(128)
        coordinateScale(8.0)
        effects(KryptonDimensionEffects.THE_NETHER)
        monsterSpawnLightLevel(ConstantIntProvider(11))
        monsterSpawnBlockLightLimit(15)
    }
    @JvmField
    val THE_END: KryptonDimensionType = register("the_end") {
        raids()
        fixedTime(6000L)
        infiniburn(BlockTags.INFINIBURN_END)
        height(256)
        effects(KryptonDimensionEffects.THE_END)
        monsterSpawnLightLevel(UniformIntProvider(0, 7))
        monsterSpawnBlockLightLimit(0)
    }

    @JvmStatic
    private fun register(name: String, builder: KryptonDimensionType.Builder.() -> Unit): KryptonDimensionType {
        val key = Key.key(name)
        return register(key, KryptonDimensionType.Builder(key).apply(builder))
    }

    @JvmStatic
    private fun register(key: Key, builder: KryptonDimensionType.Builder): KryptonDimensionType =
        Registries.DIMENSION_TYPE.register(key, builder.build())
}
