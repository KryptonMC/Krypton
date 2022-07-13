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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object LivingEntityData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonLivingEntity, _>(Keys.HEALTH, MetadataKeys.LIVING.HEALTH)
        registrar.registerMutableNoSet<KryptonLivingEntity, _>(Keys.MAX_HEALTH) { it.attributes.value(AttributeTypes.MAX_HEALTH).toFloat() }
        registrar.registerMutable(Keys.ABSORPTION, KryptonLivingEntity::absorption) { holder, value -> holder.absorption = value }
        registrar.registerFlag<KryptonLivingEntity>(Keys.IS_USING_ITEM, MetadataKeys.LIVING.FLAGS, 0)
        registrar.registerMutable<KryptonLivingEntity, _>(Keys.HAND) {
            get { if (it.data.getFlag(MetadataKeys.LIVING.FLAGS, 1)) Hand.OFF else Hand.MAIN }
            set { holder, value -> holder.data.setFlag(MetadataKeys.LIVING.FLAGS, 1, value == Hand.OFF) }
        }
        registrar.registerFlag<KryptonLivingEntity>(Keys.IS_IN_RIPTIDE_SPIN_ATTACK, MetadataKeys.LIVING.FLAGS, 2)
        registrar.registerMutable(Keys.LAST_HURT_TIMESTAMP, KryptonLivingEntity::lastHurtTimestamp) { holder, value -> holder.lastHurtTimestamp = value }
        registrar.registerMutable(Keys.SLEEPING_POSITION, KryptonLivingEntity::sleepingPosition) { holder, value -> holder.sleepingPosition = value }
    }
}
