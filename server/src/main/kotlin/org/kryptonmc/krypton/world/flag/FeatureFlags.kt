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
package org.kryptonmc.krypton.world.flag

import org.kryptonmc.serialization.Codec

object FeatureFlags {

    @JvmField
    val VANILLA: FeatureFlag
    @JvmField
    val BUNDLE: FeatureFlag
    @JvmField
    val UPDATE_1_20: FeatureFlag
    @JvmField
    val REGISTRY: FeatureFlagRegistry
    @JvmField
    val CODEC: Codec<FeatureFlagSet>
    @JvmField
    val VANILLA_SET: FeatureFlagSet
    @JvmField
    val DEFAULT_FLAGS: FeatureFlagSet

    init {
        val registry = FeatureFlagRegistry.Builder("main")
        VANILLA = registry.createVanilla("vanilla")
        BUNDLE = registry.createVanilla("bundle")
        UPDATE_1_20 = registry.createVanilla("update_1_20")
        REGISTRY = registry.build()
        CODEC = REGISTRY.codec()
        VANILLA_SET = FeatureFlagSet.of(VANILLA)
        DEFAULT_FLAGS = VANILLA_SET
    }
}
