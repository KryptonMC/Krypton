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
package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.provider.ConstantInt
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType.MonsterSettings
import java.util.OptionalLong

object KryptonDimensionTypes {

    @JvmField
    val OVERWORLD_EFFECTS: Key = Key.key("overworld")
    @JvmField
    val NETHER_EFFECTS: Key = Key.key("the_nether")
    @JvmField
    val END_EFFECTS: Key = Key.key("the_end")
    @JvmField
    val OVERWORLD: KryptonDimensionType = register("overworld", KryptonDimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true,
        false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, OVERWORLD_EFFECTS, 0F, MonsterSettings(false, true, UniformInt(0, 7), 0)))
    @JvmField
    val THE_NETHER: KryptonDimensionType = register("the_nether", KryptonDimensionType(OptionalLong.of(18000L), false, true, true, false, 8.0, false,
        true, 0, 256, 128, BlockTags.INFINIBURN_NETHER, NETHER_EFFECTS, 0.1F, MonsterSettings(true, false, ConstantInt.of(11), 15)))
    @JvmField
    val THE_END: KryptonDimensionType = register("the_end", KryptonDimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0, false,
        false, 0, 256, 256, BlockTags.INFINIBURN_END, END_EFFECTS, 0F, MonsterSettings(false, true, UniformInt(0, 7), 0)))
    @JvmField
    val OVERWORLD_CAVES: KryptonDimensionType = register("overworld_caves", KryptonDimensionType(OptionalLong.empty(), true, true, false, true, 1.0,
        true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, OVERWORLD_EFFECTS, 0F, MonsterSettings(false, true, UniformInt(0, 7), 0)))

    @JvmStatic
    private fun register(key: String, type: KryptonDimensionType): KryptonDimensionType =
        KryptonRegistries.register(KryptonDynamicRegistries.DIMENSION_TYPE, Key.key(key), type)
}
