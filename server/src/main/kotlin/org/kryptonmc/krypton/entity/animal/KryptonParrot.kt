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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Parrot
import org.kryptonmc.api.entity.animal.type.ParrotVariant
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import kotlin.random.Random

class KryptonParrot(world: KryptonWorld) : KryptonTamable(world, EntityTypes.PARROT, ATTRIBUTES), Parrot {

    override var variant: ParrotVariant
        get() = TYPES.getOrNull(data.get(MetadataKeys.Parrot.TYPE)) ?: ParrotVariant.RED_AND_BLUE
        set(value) = data.set(MetadataKeys.Parrot.TYPE, value.ordinal)

    override val soundSource: Sound.Source
        get() = Sound.Source.NEUTRAL
    override val voicePitch: Float
        get() = (Random.nextFloat() - Random.nextFloat()) * 0.2F + 1F

    init {
        data.add(MetadataKeys.Parrot.TYPE, ParrotVariant.RED_AND_BLUE.ordinal)
    }

    override fun isFood(item: ItemStack): Boolean = false

    override fun canMate(target: Animal): Boolean = false

    /*
    override fun mobInteract(player: KryptonPlayer, hand: Hand): InteractionResult {
        val heldItem = player.heldItem(hand)
        if (!isTame && TAME_FOOD.contains(heldItem.type)) {
            if (!player.canInstantlyBuild) player.setHeldItem(hand, heldItem.shrink(1))
            if (!isSilent) world.playSound(location, SoundEvents.PARROT_EAT, soundSource, 1F, 1F + (Random.nextFloat() - Random.nextFloat()) * 0.2F)
            // TODO: Send taming result
            if (Random.nextInt(10) == 0) tame(player)
            return InteractionResult.CONSUME
        }
        if (heldItem.type === POISONOUS_FOOD) {
            if (!player.canInstantlyBuild) player.setHeldItem(hand, heldItem.shrink(1))
            // TODO: Add poison potion effect
            if (player.gameMode == GameMode.CREATIVE || !isInvulnerable) {
                damage(KryptonEntityDamageSource(DamageTypes.PLAYER_ATTACK, player), 3.4028235E38F)
            }
            return InteractionResult.CONSUME
        }
        if (isOnGround && isTame && owner === player) {
            isOrderedToSit = !isOrderedToSit
            return InteractionResult.CONSUME
        }
        return super.mobInteract(player, hand)
    }
    */

    companion object {

        private val TYPES = ParrotVariant.values()
        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 6.0)
            .add(AttributeTypes.FLYING_SPEED, 0.4)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.2)
            .build()
        //private val TAME_FOOD = setOf(ItemTypes.WHEAT_SEEDS, ItemTypes.MELON_SEEDS, ItemTypes.PUMPKIN_SEEDS, ItemTypes.BEETROOT_SEEDS)
        //private val POISONOUS_FOOD = ItemTypes.COOKIE
    }
}
