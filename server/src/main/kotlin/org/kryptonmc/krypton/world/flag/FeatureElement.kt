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

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.ImmutableSets

/**
 * An object that has a required set of features needed for it to work properly.
 */
interface FeatureElement {

    fun requiredFeatures(): FeatureFlagSet

    fun isEnabled(flags: FeatureFlagSet): Boolean = requiredFeatures().isSubsetOf(flags)

    companion object {

        @JvmField
        val FILTERED_REGISTRIES: Set<ResourceKey<out Registry<out FeatureElement>>> =
            ImmutableSets.of(KryptonResourceKeys.ITEM, KryptonResourceKeys.BLOCK, KryptonResourceKeys.ENTITY_TYPE)
    }
}
