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
import org.kryptonmc.krypton.util.provider.ConstantInt
import org.kryptonmc.krypton.util.provider.UniformInt
import java.util.OptionalLong

object KryptonDimensionTypes {

    @JvmField
    val OVERWORLD_EFFECTS: Key = Key.key("overworld")
    @JvmField
    val NETHER_EFFECTS: Key = Key.key("the_nether")
    @JvmField
    val END_EFFECTS: Key = Key.key("the_end")
    @JvmField
    val OVERWORLD: KryptonDimensionType = register("overworld", KryptonDimensionType(
        OptionalLong.empty(),
        true,
        false,
        false,
        true,
        1.0,
        true,
        false,
        -64,
        384,
        384,
        BlockTags.INFINIBURN_OVERWORLD,
        OVERWORLD_EFFECTS,
        0F,
        KryptonDimensionType.MonsterSettings(false, true, UniformInt(0, 7), 0)
    ))
    @JvmField
    val THE_NETHER: KryptonDimensionType = register("the_nether", KryptonDimensionType(
        OptionalLong.of(18000L),
        false,
        true,
        true,
        false,
        8.0,
        false,
        true,
        0,
        256,
        128,
        BlockTags.INFINIBURN_NETHER,
        NETHER_EFFECTS,
        0.1F,
        KryptonDimensionType.MonsterSettings(true, false, ConstantInt.of(11), 15)
    ))
    @JvmField
    val THE_END: KryptonDimensionType = register("the_end", KryptonDimensionType(
        OptionalLong.of(6000L),
        false,
        false,
        false,
        false,
        1.0,
        false,
        false,
        0,
        256,
        256,
        BlockTags.INFINIBURN_END,
        END_EFFECTS,
        0F,
        KryptonDimensionType.MonsterSettings(false, true, UniformInt(0, 7), 0)
    ))
    @JvmField
    val OVERWORLD_CAVES: KryptonDimensionType = register("overworld_caves", KryptonDimensionType(
        OptionalLong.empty(),
        true,
        true,
        false,
        true,
        1.0,
        true,
        false,
        -64,
        384,
        384,
        BlockTags.INFINIBURN_OVERWORLD,
        OVERWORLD_EFFECTS,
        0F,
        KryptonDimensionType.MonsterSettings(false, true, UniformInt(0, 7), 0)
    ))

    @JvmStatic
    private fun register(key: String, type: KryptonDimensionType): KryptonDimensionType = Registries.DIMENSION_TYPE.register(Key.key(key), type)
}
