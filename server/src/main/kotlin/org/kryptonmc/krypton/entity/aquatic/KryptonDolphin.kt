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

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.aquatic.Dolphin
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3i
import kotlin.random.Random

class KryptonDolphin(world: KryptonWorld) : KryptonAquaticAnimal(world, EntityTypes.DOLPHIN, ATTRIBUTES), Dolphin {

    override var treasurePosition: Vector3i
        get() = data[MetadataKeys.DOLPHIN.TREASURE_POSITION]
        set(value) = data.set(MetadataKeys.DOLPHIN.TREASURE_POSITION, value)
    override var gotFish: Boolean
        get() = data[MetadataKeys.DOLPHIN.GOT_FISH]
        set(value) = data.set(MetadataKeys.DOLPHIN.GOT_FISH, value)
    override var skinMoisture: Int
        get() = data[MetadataKeys.DOLPHIN.MOISTURE]
        set(value) = data.set(MetadataKeys.DOLPHIN.MOISTURE, value)

    override val maxAirTicks: Int
        get() = 4800
    override val swimSound: SoundEvent
        get() = SoundEvents.DOLPHIN_SWIM
    override val splashSound: SoundEvent
        get() = SoundEvents.DOLPHIN_SPLASH

    init {
        data.add(MetadataKeys.DOLPHIN.TREASURE_POSITION)
        data.add(MetadataKeys.DOLPHIN.GOT_FISH)
        data.add(MetadataKeys.DOLPHIN.MOISTURE)
    }

    override fun handleAir(amount: Int) {
        // Dolphins apparently don't handle air in the same way other water animals do
    }

    override fun tick() {
        super.tick()
        if (!hasAI) {
            air = maxAirTicks
            return
        }
        if (inWater || inBubbleColumn) {
            skinMoisture = 2400
            return
        }
        skinMoisture--
        if (skinMoisture <= 0) damage(KryptonDamageSource(DamageTypes.DRY_OUT), 0F)
        if (isOnGround) {
            velocity = velocity.add(((Random.nextFloat() * 2F - 1F) * 0.2F).toDouble(), 0.5, ((Random.nextFloat() * 2F - 1F) * 0.2F).toDouble())
            rotation = Vector2f(rotation.x(), Random.nextFloat() * 360F)
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

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 1.2)
            .add(AttributeTypes.ATTACK_DAMAGE, 3.0)
            .build()
    }
}
