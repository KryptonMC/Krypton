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
package org.kryptonmc.krypton.data.provider.impl

import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object EntityData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata(Keys.CUSTOM_NAME, MetadataKeys.CUSTOM_NAME)
        registrar.registerMetadata(Keys.IS_CUSTOM_NAME_VISIBLE, MetadataKeys.CUSTOM_NAME_VISIBILITY)
        registrar.register(Keys.DISPLAY_NAME) {
            get { it.displayName }
        }
        registrar.register(Keys.LOCATION) {
            get { it.location }
            set { holder, value -> holder.location = value }
        }
        registrar.register(Keys.ROTATION) {
            get { it.rotation }
            set { holder, value -> holder.rotation = value }
        }
        registrar.register(Keys.VELOCITY) {
            get { it.velocity }
            set { holder, value -> holder.velocity = value }
        }
        registrar.register(Keys.PASSENGERS) {
            get { it.passengers }
            set { holder, value ->
                holder.ejectPassengers()
                value.forEach(holder::addPassenger)
            }
        }
        registrar.register(Keys.VEHICLE) {
            get { it.vehicle }
            set { holder, value -> holder.vehicle = value }
        }
        registrar.register(Keys.IS_INVULNERABLE) {
            get { it.isInvulnerable }
            set { holder, value -> holder.isInvulnerable = value }
        }
        registrar.registerSharedFlag(Keys.IS_ON_FIRE, 0)
        registrar.register(Keys.IS_ON_GROUND) {
            get { it.isOnGround }
            set { holder, value -> holder.isOnGround = value }
        }
        registrar.registerSharedFlag(Keys.IS_SNEAKING, 1)
        registrar.registerSharedFlag(Keys.IS_SPRINTING, 3)
        registrar.registerSharedFlag(Keys.IS_SWIMMING, 4)
        registrar.registerSharedFlag(Keys.IS_INVISIBLE, 5)
        registrar.registerSharedFlag(Keys.IS_GLOWING, 6)
        registrar.registerSharedFlag(Keys.IS_GLIDING, 7)
        registrar.registerMetadata(Keys.IS_SILENT, MetadataKeys.SILENT)
        registrar.register(Keys.HAS_GRAVITY) {
            get { !it.data[MetadataKeys.NO_GRAVITY] }
            set { holder, value -> holder.data[MetadataKeys.NO_GRAVITY] = !value }
        }
        registrar.register(Keys.TICKS_EXISTED) {
            get { it.ticksExisted }
        }
        registrar.registerMetadata(Keys.AIR, MetadataKeys.AIR_TICKS)
        registrar.register(Keys.AIR) {
            get { it.fireTicks }
            set { holder, value -> holder.fireTicks = value }
        }
        registrar.registerMetadata(Keys.FROZEN_TICKS, MetadataKeys.FROZEN_TICKS)
        registrar.register(Keys.IN_WATER) {
            get { it.inWater }
        }
        registrar.register(Keys.IN_LAVA) {
            get { it.inLava }
        }
        registrar.register(Keys.UNDERWATER) {
            get { it.underwater }
        }
    }
}

private fun <E> DataProviderRegistrar.register(key: Key<E>, builder: DataProvider.MutableBuilder<KryptonEntity, E>.() -> Unit) {
    registerMutable(key, builder)
}

private fun DataProviderRegistrar.registerSharedFlag(key: Key<Boolean>, flag: Int) {
    register(key) {
        get { it.getSharedFlag(flag) }
        set { holder, value -> holder.setSharedFlag(flag, value) }
    }
}

private fun <E> DataProviderRegistrar.registerMetadata(key: Key<E>, metadataKey: MetadataKey<E>) {
    register(key) {
        get { it.data[metadataKey] }
        set { holder, value -> holder.data[metadataKey] = value }
    }
}
