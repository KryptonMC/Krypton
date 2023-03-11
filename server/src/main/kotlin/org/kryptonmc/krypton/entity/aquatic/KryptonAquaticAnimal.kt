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
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.api.entity.aquatic.AquaticAnimal
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

abstract class KryptonAquaticAnimal(world: KryptonWorld) : KryptonMob(world), AquaticAnimal {

    override fun tick() {
        val air = airSupply
        super.tick()
        handleAir(air)
    }

    protected open fun handleAir(amount: Int) {
        if (isAlive() && !waterPhysicsSystem.isInWaterOrBubbleColumn()) {
            // Aquatic creatures must be underwater to breathe. If they are out of water, they start to run out of air,
            // and eventually suffocate.
            airSupply = amount - 1
            if (airSupply == DROWNING_THRESHOLD) {
                // If the creature is out of air, it takes 2 points of drowning damage every tick.
                airSupply = 0
                damage(KryptonDamageSource(DamageTypes.DROWNING), DROWNING_DAMAGE)
            }
        } else {
            airSupply = AIR_RESET_AMOUNT
        }
    }

    final override fun isPushedByFluid(): Boolean = false

    companion object {

        private const val DROWNING_THRESHOLD = -20
        private const val DROWNING_DAMAGE = 2F
        private const val AIR_RESET_AMOUNT = 300
    }
}
