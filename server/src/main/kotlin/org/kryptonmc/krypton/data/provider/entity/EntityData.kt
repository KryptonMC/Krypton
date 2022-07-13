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
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object EntityData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonEntity, _>(Keys.CUSTOM_NAME, MetadataKeys.CUSTOM_NAME)
        registrar.registerMetadata<KryptonEntity, _>(Keys.IS_CUSTOM_NAME_VISIBLE, MetadataKeys.CUSTOM_NAME_VISIBILITY)
        registrar.registerMutable(Keys.DISPLAY_NAME, KryptonEntity::displayName)
        // TODO: Review and decide if we need to call another function to do stuff this should do here
        registrar.registerMutable(Keys.LOCATION, KryptonEntity::location) { holder, value -> holder.location = value }
        registrar.registerMutable(Keys.ROTATION, KryptonEntity::rotation) { holder, value -> holder.rotation = value }
        registrar.registerMutable(Keys.VELOCITY, KryptonEntity::velocity) { holder, value -> holder.velocity = value }
        registrar.registerMutable<KryptonEntity, _>(Keys.PASSENGERS) {
            get { it.passengers }
            set { holder, value ->
                holder.ejectPassengers()
                value.forEach(holder::addPassenger)
            }
        }
        registrar.registerMutable(Keys.VEHICLE, KryptonEntity::vehicle) { holder, value -> holder.vehicle = value }
        registrar.registerMutable(Keys.IS_INVULNERABLE, KryptonEntity::isInvulnerable) { holder, value -> holder.isInvulnerable = value }
        registrar.registerMutable(Keys.IS_ON_GROUND, KryptonEntity::isOnGround) { holder, value -> holder.isOnGround = value }
        registrar.registerFlag<KryptonEntity>(Keys.IS_ON_FIRE, MetadataKeys.FLAGS, 0)
        registrar.registerFlag<KryptonEntity>(Keys.IS_SNEAKING, MetadataKeys.FLAGS, 1)
        registrar.registerFlag<KryptonEntity>(Keys.IS_SPRINTING, MetadataKeys.FLAGS, 3)
        registrar.registerFlag<KryptonEntity>(Keys.IS_SWIMMING, MetadataKeys.FLAGS, 4)
        registrar.registerFlag<KryptonEntity>(Keys.IS_INVISIBLE, MetadataKeys.FLAGS, 5)
        registrar.registerFlag<KryptonEntity>(Keys.IS_GLOWING, MetadataKeys.FLAGS, 6)
        registrar.registerFlag<KryptonEntity>(Keys.IS_GLIDING, MetadataKeys.FLAGS, 7)
        registrar.registerMetadata<KryptonEntity, _>(Keys.IS_SILENT, MetadataKeys.SILENT)
        registrar.registerMutable<KryptonEntity, _>(Keys.HAS_GRAVITY, { !it.data[MetadataKeys.NO_GRAVITY] }) { holder, value ->
            holder.data[MetadataKeys.NO_GRAVITY] = value
        }
        registrar.registerMutable(Keys.TICKS_EXISTED, KryptonEntity::ticksExisted)
        registrar.registerMetadata<KryptonEntity, _>(Keys.AIR, MetadataKeys.AIR_TICKS)
        registrar.registerMutable(Keys.AIR, KryptonEntity::fireTicks) { holder, value -> holder.fireTicks = value }
        registrar.registerMetadata<KryptonEntity, _>(Keys.FROZEN_TICKS, MetadataKeys.FROZEN_TICKS)
        registrar.registerMutable(Keys.IN_WATER, KryptonEntity::inWater)
        registrar.registerMutable(Keys.IN_LAVA, KryptonEntity::inLava)
        registrar.registerMutable(Keys.UNDERWATER, KryptonEntity::underwater)
    }
}
