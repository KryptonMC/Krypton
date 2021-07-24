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
package org.kryptonmc.krypton.world.generation.feature

import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.generation.feature.config.FeatureConfig
import org.kryptonmc.krypton.world.generation.feature.config.NoneFeatureConfig

object Features {

    val NO_OP: Feature<NoneFeatureConfig> = register("no_op", NoOpFeature(NoneFeatureConfig.CODEC))

    @Suppress("UNCHECKED_CAST")
    private fun <C : FeatureConfig, F : Feature<C>> register(name: String, feature: F): F =
        Registries.register(InternalRegistries.FEATURE, name, feature) as F
}
