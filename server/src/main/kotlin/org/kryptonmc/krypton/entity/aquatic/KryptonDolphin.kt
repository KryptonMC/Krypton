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
import org.kryptonmc.krypton.coordinate.BlockPos
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
        set(value) = data.set(MetadataKeys.Dolphin.TREASURE_POSITION, BlockPos.from(value))
    override var hasGotFish: Boolean
        get() = data.get(MetadataKeys.Dolphin.GOT_FISH)
        set(value) = data.set(MetadataKeys.Dolphin.GOT_FISH, value)
    override var skinMoisture: Int
        get() = data.get(MetadataKeys.Dolphin.MOISTURE)
        set(value) = data.set(MetadataKeys.Dolphin.MOISTURE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Dolphin.TREASURE_POSITION, BlockPos.ZERO)
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
        if (isInWater || waterPhysicsSystem.isInBubbleColumn()) { // TODO: Also check for being in rain
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
            pitch = random.nextFloat() * MAX_ANGLE
            isOnGround = false
        }
    }

    /*
    override fun mobInteract(player: KryptonPlayer, hand: Hand): InteractionResult {
        val heldItem = player.heldItem(hand)
        if (heldItem.isEmpty() || !ItemTags.FISHES.contains(heldItem.type)) return super.mobInteract(player, hand)
        playSound(SoundEvents.DOLPHIN_EAT, 1F, 1F)
        gotFish = true
        if (!player.canInstantlyBuild) player.setHeldItem(hand, heldItem.shrink(1))
        return InteractionResult.CONSUME
    }
    */

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
