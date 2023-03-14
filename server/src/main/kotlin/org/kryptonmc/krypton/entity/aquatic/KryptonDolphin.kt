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

import org.kryptonmc.api.entity.aquatic.Dolphin
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.DolphinSerializer
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonDolphin(world: KryptonWorld) : KryptonAquaticAnimal(world), Dolphin {

    override val type: KryptonEntityType<KryptonDolphin>
        get() = KryptonEntityTypes.DOLPHIN
    override val serializer: EntitySerializer<KryptonDolphin>
        get() = DolphinSerializer

    override var treasurePosition: Vec3i
        get() = data.get(MetadataKeys.Dolphin.TREASURE_POSITION)
        set(value) = data.set(MetadataKeys.Dolphin.TREASURE_POSITION, value)
    override var hasGotFish: Boolean
        get() = data.get(MetadataKeys.Dolphin.GOT_FISH)
        set(value) = data.set(MetadataKeys.Dolphin.GOT_FISH, value)
    override var skinMoisture: Int
        get() = data.get(MetadataKeys.Dolphin.MOISTURE)
        set(value) = data.set(MetadataKeys.Dolphin.MOISTURE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Dolphin.TREASURE_POSITION, Vec3i.ZERO)
        data.define(MetadataKeys.Dolphin.GOT_FISH, false)
        data.define(MetadataKeys.Dolphin.MOISTURE, FULL_SKIN_MOISTURE)
    }

    override fun handleAir(amount: Int) {
        // Dolphins have special air handling logic that is in the tick function directly.
    }

    override fun tick() {
        super.tick()
        if (!hasAI) {
            airSupply = maxAirTicks()
            return
        }
        // Dolphins don't immediately start to suffocate out of water, as they can survive out of water for extended periods of time.
        // In real life, dolphins can't breathe underwater, so they have to resurface. In Minecraft, this behaviour isn't
        // simulated, but what is simulated is the ability for dolphins to survive for extended periods of time out of water.
        if (isInWater() || waterPhysicsSystem.isInBubbleColumn()) { // TODO: Also check for being in rain
            // If the dolphin is in water, rain, or a bubble column then it has full moisture.
            skinMoisture = FULL_SKIN_MOISTURE
            return
        }
        // For every tick the dolphin is not in water, its moisture decreases by 1.
        skinMoisture--
        // When the moisture reaches 0, the dolphin takes one damage every tick from dry out.
        if (skinMoisture <= 0) damage(KryptonDamageSource(DamageTypes.DRY_OUT), 1F)
        if (isOnGround) {
            velocity = velocity.add(randomVelocityModifier(random), GROUND_Y_VELOCITY_INCREASE, randomVelocityModifier(random))
            teleport(position.withPitch(random.nextFloat() * MAX_ANGLE))
            isOnGround = false
        }
    }

    override fun maxAirTicks(): Int = MAX_AIR

    companion object {

        private const val MAX_AIR = 4 * 60 * 20 // 4 minutes in ticks
        private const val FULL_SKIN_MOISTURE = 2 * 60 * 20 // 2 minutes in ticks
        private const val MAX_ANGLE = 360F
        private const val GROUND_Y_VELOCITY_INCREASE = 0.5

        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_MOVEMENT_SPEED = 1.2
        private const val DEFAULT_ATTACK_DAMAGE = 3.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)

        // this will always produce a value between -0.2 and 0.2
        // Broken down: Random.nextFloat() produces a value between 0 and 1, multiplied by 2 produces a value between 0 and 2
        // taking 1 away produces a value between -1 and 1, then multiplying by 0.2 produces a value between -0.2 and 0.2
        @JvmStatic
        @Suppress("MagicNumber") // Not magic if it's explained in a comment
        private fun randomVelocityModifier(random: RandomSource): Double = ((random.nextFloat() * 2F - 1F) * 0.2F).toDouble()
    }
}
