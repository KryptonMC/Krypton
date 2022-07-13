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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object FoxData {

    private val TYPES = FoxType.values()

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonFox, _, _>(Keys.FOX_TYPE, MetadataKeys.FOX.TYPE, ::type, FoxType::ordinal)
        registrar.registerFlag<KryptonFox>(Keys.IS_SITTING, MetadataKeys.FOX.FLAGS, 0)
        registrar.registerFlag<KryptonFox>(Keys.IS_CROUCHING, MetadataKeys.FOX.FLAGS, 2)
        registrar.registerFlag<KryptonFox>(Keys.IS_INTERESTED, MetadataKeys.FOX.FLAGS, 3)
        registrar.registerFlag<KryptonFox>(Keys.IS_POUNCING, MetadataKeys.FOX.FLAGS, 4)
        registrar.registerFlag<KryptonFox>(Keys.IS_SLEEPING, MetadataKeys.FOX.FLAGS, 5)
        registrar.registerFlag<KryptonFox>(Keys.HAS_FACEPLANTED, MetadataKeys.FOX.FLAGS, 6)
        registrar.registerFlag<KryptonFox>(Keys.IS_DEFENDING, MetadataKeys.FOX.FLAGS, 7)
        registrar.registerMetadata<KryptonFox, _>(Keys.FIRST_TRUSTED, MetadataKeys.FOX.FIRST_TRUSTED)
        registrar.registerMetadata<KryptonFox, _>(Keys.FIRST_TRUSTED, MetadataKeys.FOX.SECOND_TRUSTED)
    }

    @JvmStatic
    private fun type(id: Int): FoxType = TYPES.getOrNull(id) ?: FoxType.RED
}
