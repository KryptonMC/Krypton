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

import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.nbt.CompoundTag
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class PlayerHungerSystem(private val player: KryptonPlayer) {

    // Sources for vanilla hunger system values:
    //      -> Minecraft Wiki https://minecraft.wiki/w/Hunger
    // 20 is the default vanilla food level
    var foodLevel: Int = 20
    private var tickTimer = 0

    // 0 is the default vanilla food exhaustion level
    var exhaustionLevel: Float = 0F

    // 5 is the default vanilla food saturation level
    var saturationLevel: Float = 5F

    fun tick() {
        // TODO: More actions for exhaustion, add constants?
        if (player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE) return
        tickTimer++

        // Sources:
        //      -> Minecraft Wiki https://minecraft.wiki/w/Hunger
        //      -> 3 other implementations of this exact mechanic (lines up with wiki)

        // Food System
        // The food exhaustion level accumulates exhaustion from player actions
        // over time. Once the exhaustion level exceeds a threshold of 4, it
        // resets, and then deducts a food saturation level.
        // The food saturation level, in turn, once depleted (at zero), deducts a
        // a food level every tick, only once the exhaustion has been reset again.
        // Additionally, client side, a saturation level of zero is responsible for
        // triggering the shaking of the hunger bar.
        // TLDR: The exhaustion level deducts from the saturation. The saturation level
        // follows the food level and acts as a buffer before any food levels are deducted.
        // -> Player action
        // -> Exhaustion ↑
        // -> If Exhaustion Threshold of 4
        // -> Reset Exhaustion
        // -> Saturation ↓
        // -> If Saturation Threshold of 0
        // -> Food Level ↓
        if (exhaustionLevel > 4F) {
            exhaustionLevel -= 4F
            if (saturationLevel > 0) {
                saturationLevel = max(saturationLevel - 1, 0F)
            } else {
                foodLevel = max(foodLevel - 1, 0)
            }
        }

        // Starvation System
        // If the food level is zero, every 80 ticks, deduct a half-heart.
        // This system is conditional based on difficulty.
        //      -> Easy: Health must be greater than 10
        //      -> Normal: Health must be greater than 1
        //      -> Hard: There is no minimum health, good luck out there.
        // An exception to this system is in peaceful mode, where every 20 ticks,
        // the food level regenerates instead of deducting a half-heart.
        // NOTE: 80 are 20 ticks are the vanilla timings for hunger and
        // peaceful food regeneration respectively.
        @Suppress("MagicNumber") // The magic numbers here are all explained above
        when (player.world.difficulty) {
            Difficulty.EASY -> {
                if (getHealth() > 10 && foodLevel == 0 && tickTimer == 80) { // starving
                    setHealth(getHealth() - 1) // deduct half a heart
                }
            }
            Difficulty.NORMAL -> if (getHealth() > 1 && foodLevel == 0 && tickTimer == 80) setHealth(getHealth() - 1)
            Difficulty.HARD -> if (foodLevel == 0 && tickTimer == 80) setHealth(getHealth() - 1)
            Difficulty.PEACEFUL -> {
                if (foodLevel < 20 && tickTimer % 20 == 0) {
                    foodLevel++ // increase player food level
                }
            }
        }

        // Health Regeneration System
        // Every 80 ticks if the food level is greater than
        // or equal to 18, add a half-heart to the player.
        // Healing of course, comes at an expense to the player's
        // food level, so food exhaustion and saturation are adjusted
        // to bring equilibrium to the hunger system. This is to avoid
        // a player healing infinitely.
        @Suppress("MagicNumber") // The magic numbers here are all explained above
        if (tickTimer == 80) {
            if (foodLevel >= 18) {
                // Avoid exceeding max health
                if (player.maxHealth >= getHealth() + 1) {
                    // Regenerate health
                    setHealth(getHealth() + 1)
                    // Once a half-heart has been added, increase the exhaustion by 3,
                    // or if that operation were to exceed the threshold, instead, set it to the
                    // threshold value of 4.
                    exhaustionLevel = min(exhaustionLevel + 3F, 4F)
                    // Force the saturation level to deplete by 3.
                    // So as health comes up, the food "buffer" comes down,
                    // eventually causing the food level to decrease, when saturation
                    // reaches zero.
                    saturationLevel -= 3
                } else {
                    setHealth(player.maxHealth)
                }
            }
            tickTimer = 0 // reset tick timer
        }
    }

    private fun getHealth(): Float = player.data.get(MetadataKeys.LivingEntity.HEALTH)

    private fun setHealth(health: Float) {
        player.data.set(MetadataKeys.LivingEntity.HEALTH, health)
    }

    fun load(data: CompoundTag) {
        foodLevel = data.getInt(LEVEL_TAG)
        tickTimer = data.getInt(TICK_TIMER_TAG)
        exhaustionLevel = data.getFloat(EXHAUSTION_LEVEL)
        saturationLevel = data.getFloat(SATURATION_LEVEL)
    }

    fun save(builder: CompoundTag.Builder): CompoundTag.Builder = builder.apply {
        putInt(LEVEL_TAG, foodLevel)
        putInt(TICK_TIMER_TAG, tickTimer)
        putFloat(EXHAUSTION_LEVEL, exhaustionLevel)
        putFloat(SATURATION_LEVEL, saturationLevel)
    }

    fun updateMovementExhaustion(deltaX: Double, deltaY: Double, deltaZ: Double) {
        // Source: https://minecraft.wiki/w/Hunger#Exhaustion_level_increase
        if (player.isSwimming) {
            val value = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
            if (value > 0) exhaustionLevel += SWIMMING_EXHAUSTION_MODIFIER * value.toFloat()
            // 0.01/u is the vanilla level of exhaustion per unit for swimming
            return
        }
        if (player.isOnGround) {
            val value = sqrt(deltaX * deltaX + deltaZ * deltaZ)
            if (value > 0) {
                when {
                    player.isSprinting -> exhaustionLevel += SPRINTING_EXHAUSTION_MODIFIER * value.toFloat()
                    // 0.1/u is the vanilla level of exhaustion per unit for sprinting
                }
            }
            return
        }
    }

    companion object {

        private const val SWIMMING_EXHAUSTION_MODIFIER = 0.01F
        private const val SPRINTING_EXHAUSTION_MODIFIER = 0.1F

        private const val LEVEL_TAG = "foodLevel"
        private const val TICK_TIMER_TAG = "foodTickTimer"
        private const val EXHAUSTION_LEVEL = "foodExhaustionLevel"
        private const val SATURATION_LEVEL = "foodSaturationLevel"
    }
}
