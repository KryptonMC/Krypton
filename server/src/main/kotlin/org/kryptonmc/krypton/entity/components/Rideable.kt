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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.downcastBase
import org.kryptonmc.krypton.entity.system.EntityVehicleSystem

interface Rideable : BaseDataHolder {

    val vehicleSystem: EntityVehicleSystem

    override val isPassenger: Boolean
        get() = vehicleSystem.isPassenger()
    override val passengers: List<Entity>
        get() = vehicleSystem.passengers()
    override val isVehicle: Boolean
        get() = vehicleSystem.isVehicle()
    override var vehicle: Entity?
        get() = vehicleSystem.vehicle
        set(value) = vehicleSystem.setVehicle(value)

    fun canAddPassenger(passenger: KryptonEntity): Boolean = vehicleSystem.passengers().isEmpty()

    fun startRiding(vehicle: KryptonEntity): Boolean = startRiding(vehicle, false)

    fun startRiding(vehicle: KryptonEntity, force: Boolean): Boolean {
        if (vehicle === vehicleSystem.vehicle) return false
        var rootVehicle = vehicle
        while (rootVehicle.vehicleSystem.vehicle != null) {
            if (rootVehicle.vehicleSystem.vehicle === this) return false
            rootVehicle = rootVehicle.vehicleSystem.vehicle!!
        }
        if (force || canRide(vehicle) && vehicle.canAddPassenger(this as KryptonEntity)) {
            if (vehicleSystem.isPassenger()) stopRiding()
            pose = Pose.STANDING
            vehicleSystem.setVehicle(vehicle)
            vehicle.vehicleSystem.addPassenger(this as KryptonEntity)
            return true
        }
        return false
    }

    fun stopRiding() {
        vehicleSystem.ejectVehicle()
    }

    fun canRide(vehicle: KryptonEntity): Boolean = !isSneaking

    override fun addPassenger(entity: Entity) {
        vehicleSystem.addPassenger(entity.downcastBase())
    }

    override fun removePassenger(entity: Entity) {
        vehicleSystem.removePassenger(entity.downcastBase())
    }

    override fun ejectPassengers() {
        vehicleSystem.ejectPassengers()
    }

    override fun ejectVehicle() {
        vehicleSystem.ejectVehicle()
    }
}
