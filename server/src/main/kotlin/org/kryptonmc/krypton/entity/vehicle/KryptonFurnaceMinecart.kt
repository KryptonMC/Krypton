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
package org.kryptonmc.krypton.entity.vehicle

import org.kryptonmc.api.entity.vehicle.FurnaceMinecart
import org.kryptonmc.api.entity.vehicle.MinecartVariant
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.FurnaceMinecartSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonFurnaceMinecart(world: KryptonWorld) : KryptonMinecartLike(world), FurnaceMinecart {

    override val type: KryptonEntityType<KryptonFurnaceMinecart>
        get() = KryptonEntityTypes.FURNACE_MINECART
    override val serializer: EntitySerializer<KryptonFurnaceMinecart>
        get() = FurnaceMinecartSerializer

    override val variant: MinecartVariant
        get() = MinecartVariant.FURNACE
    private var remainingFuel = 0

    override val fuel: Int
        get() = remainingFuel

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.FurnaceMinecart.HAS_FUEL, false)
    }

    override fun hasFuel(): Boolean = data.get(MetadataKeys.FurnaceMinecart.HAS_FUEL)

    private fun setHasFuel(has: Boolean) {
        data.set(MetadataKeys.FurnaceMinecart.HAS_FUEL, has)
    }

    fun setFuel(fuel: Int) {
        remainingFuel = fuel
        if (fuel != 0) setHasFuel(true)
    }

    override fun addFuel(amount: Int) {
        remainingFuel += amount
        if (amount != 0) setHasFuel(true)
    }

    override fun removeFuel(amount: Int) {
        remainingFuel -= amount
        if (remainingFuel <= 0) resetFuel()
    }

    override fun resetFuel() {
        remainingFuel = 0
        setHasFuel(false)
    }
}
