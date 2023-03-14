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
