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
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object BeeData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerFlag<KryptonBee>(Keys.IS_ANGRY, MetadataKeys.BEE.FLAGS, 1)
        registrar.registerFlag<KryptonBee>(Keys.HAS_STUNG, MetadataKeys.BEE.FLAGS, 2)
        registrar.registerFlag<KryptonBee>(Keys.HAS_NECTAR, MetadataKeys.BEE.FLAGS, 3)
        registrar.registerMutable(Keys.CANNOT_ENTER_HIVE_TICKS, KryptonBee::cannotEnterHiveTicks) { holder, value ->
            holder.cannotEnterHiveTicks = value
        }
        registrar.registerMutable(Keys.HIVE, KryptonBee::hive) { holder, value -> holder.hive = value }
        registrar.registerMutable(Keys.FLOWER, KryptonBee::flower) { holder, value -> holder.flower = value }
    }
}
