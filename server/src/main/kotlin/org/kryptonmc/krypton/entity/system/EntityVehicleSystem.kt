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
package org.kryptonmc.krypton.entity.system

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.util.downcastBase
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.PacketGrouping
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
        PacketGrouping.sendGroupedPacket(entity.server, PacketOutSetPassengers.fromEntity(entity, passengers))
    }
}
