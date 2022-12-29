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
package org.kryptonmc.krypton.world.data

import org.kryptonmc.krypton.pack.DataPackConfig
import org.kryptonmc.krypton.world.flag.FeatureFlagSet
import org.kryptonmc.krypton.world.flag.FeatureFlags
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class WorldDataConfiguration(val dataPacks: DataPackConfig, val enabledFeatures: FeatureFlagSet) {

    fun expandFeatures(other: FeatureFlagSet): WorldDataConfiguration = WorldDataConfiguration(dataPacks, enabledFeatures.join(other))

    companion object {

        const val ENABLED_FEATURES_ID: String = "enabled_features"
        @JvmField
        val CODEC: Codec<WorldDataConfiguration> = RecordCodecBuilder.create { instance ->
            instance.group(
                DataPackConfig.CODEC.optionalFieldOf("DataPacks", DataPackConfig.DEFAULT).getting { it.dataPacks },
                FeatureFlags.CODEC.optionalFieldOf(ENABLED_FEATURES_ID, FeatureFlags.DEFAULT_FLAGS).getting { it.enabledFeatures }
            ).apply(instance) { packs, features -> WorldDataConfiguration(packs, features) }
        }
        @JvmField
        val DEFAULT: WorldDataConfiguration = WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.DEFAULT_FLAGS)
    }
}
