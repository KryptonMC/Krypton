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
package org.kryptonmc.krypton.entity.system

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.downcastBase
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import java.util.Collections
import java.util.stream.Stream

class EntityVehicleSystem(private val entity: KryptonEntity) {

    private val passengers = ArrayList<KryptonEntity>()
    private val passengersView = Collections.unmodifiableList(passengers)
    private var vehicle: KryptonEntity? = null

    fun isVehicle(): Boolean = passengers.isNotEmpty()

    fun isPassenger(): Boolean = vehicle != null

    fun passengers(): List<KryptonEntity> = passengersView

    fun rootVehicle(): KryptonEntity {
        var root: KryptonEntity = entity
        while (root.vehicleSystem.isPassenger()) {
            root = root.vehicleSystem.vehicle!!
        }
        return root
    }

    fun vehicle(): KryptonEntity? = vehicle

    fun setVehicle(vehicle: Entity?) {
        this.vehicle = vehicle?.downcastBase()
    }

    private fun hasPassenger(passenger: KryptonEntity): Boolean = passengers.contains(passenger)

    fun addPassenger(passenger: KryptonEntity) {
        if (hasPassenger(passenger) || passenger.vehicleSystem.hasPassenger(entity)) return
        passenger.vehicleSystem.setVehicle(entity)
        passengers.add(passenger)
        updatePassengers()
    }

    fun removePassenger(passenger: KryptonEntity) {
        if (passenger.vehicleSystem.vehicle === entity || !hasPassenger(passenger)) return
        passenger.vehicleSystem.setVehicle(null)
        passengers.remove(passenger)
        updatePassengers()
    }

    fun ejectPassengers() {
        passengers.forEach { it.vehicleSystem.ejectVehicle() }
    }

    fun ejectVehicle() {
        if (vehicle != null) {
            val vehicle = vehicle!!
            this.vehicle = null
            vehicle.removePassenger(entity)
        }
    }

    fun hasExactlyOnePlayerPassenger(): Boolean = getIndirectPassengersStream().filter { it is KryptonPlayer }.count() == 1L

    private fun getSelfAndPassengers(): Stream<KryptonEntity> = Stream.concat(Stream.of(entity), getIndirectPassengersStream())

    private fun getIndirectPassengersStream(): Stream<KryptonEntity> = passengers.stream().flatMap { it.vehicleSystem.getSelfAndPassengers() }

    private fun updatePassengers() {
        entity.server.sessionManager.sendGrouped(PacketOutSetPassengers.fromEntity(entity, passengers))
    }
}
