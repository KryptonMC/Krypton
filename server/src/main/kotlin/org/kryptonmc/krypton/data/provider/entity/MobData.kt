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
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object MobData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMutableNoSet(Keys.IS_PERSISTENT, KryptonMob::isPersistent)
        registrar.registerMutable<KryptonMob, _>(Keys.HAS_AI, { !it.data.getFlag(MetadataKeys.MOB.FLAGS, 0) }) { holder, value ->
            holder.data.setFlag(MetadataKeys.MOB.FLAGS, 0, !value)
        }
        registrar.registerFlag<KryptonMob>(Keys.IS_AGGRESSIVE, MetadataKeys.MOB.FLAGS, 2)
        registrar.registerMutable<KryptonMob, _>(Keys.MAIN_HAND) {
            get { if (it.data.getFlag(MetadataKeys.MOB.FLAGS, 1)) MainHand.LEFT else MainHand.RIGHT }
            set { holder, value -> holder.data.setFlag(MetadataKeys.MOB.FLAGS, 1, value == MainHand.LEFT) }
        }
    }
}
