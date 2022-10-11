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
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.api.entity.aquatic.AquaticAnimal
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

abstract class KryptonAquaticAnimal(world: KryptonWorld) : KryptonMob(world), AquaticAnimal {

    final override val isPushedByFluid: Boolean
        get() = false

    override fun tick() {
        val air = air
        super.tick()
        handleAir(air)
    }

    protected open fun handleAir(amount: Int) {
        if (isAlive && !waterPhysicsSystem.isInWaterOrBubbleColumn()) {
            // Aquatic creatures must be underwater to breathe. If they are out of water, they start to run out of air,
            // and eventually suffocate.
            air = amount - 1
            if (air == DROWNING_THRESHOLD) {
                // If the creature is out of air, it takes 2 points of drowning damage every tick.
                air = 0
                damage(KryptonDamageSource(DamageTypes.DROWNING), DROWNING_DAMAGE)
            }
        } else {
            air = AIR_RESET_AMOUNT
        }
    }

    companion object {

        private const val DROWNING_THRESHOLD = -20
        private const val DROWNING_DAMAGE = 2F
        private const val AIR_RESET_AMOUNT = 300
    }
}
