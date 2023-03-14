/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.util.downcastBase
import org.kryptonmc.krypton.entity.system.EntityVehicleSystem

interface Rideable : BaseDataHolder {

    val vehicleSystem: EntityVehicleSystem

    override val passengers: List<Entity>
        get() = vehicleSystem.passengers()
    override var vehicle: Entity?
        get() = vehicleSystem.vehicle()
        set(value) = vehicleSystem.setVehicle(value)

    override fun isPassenger(): Boolean = vehicleSystem.isPassenger()

    override fun isVehicle(): Boolean = vehicleSystem.isVehicle()

    fun canAddPassenger(passenger: KryptonEntity): Boolean = vehicleSystem.passengers().isEmpty()

    fun startRiding(vehicle: KryptonEntity): Boolean = startRiding(vehicle, false)

    fun startRiding(vehicle: KryptonEntity, force: Boolean): Boolean {
        if (vehicle === vehicleSystem.vehicle()) return false
        var rootVehicle = vehicle
        while (rootVehicle.vehicleSystem.vehicle() != null) {
            if (rootVehicle.vehicleSystem.vehicle() === this) return false
            rootVehicle = rootVehicle.vehicleSystem.vehicle()!!
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
