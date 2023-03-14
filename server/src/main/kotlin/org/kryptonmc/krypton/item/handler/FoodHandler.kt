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
package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.UseItemResult

object FoodHandler : ItemTimedHandler {

    private const val DUMMY_FOOD_LEVEL_INCREASE = 8
    private const val DUMMY_SATURATION_LEVEL_INCREASE = 12.8F

    override fun finishUse(player: KryptonPlayer, hand: Hand): UseItemResult {
        // TODO: Remove hardcoded values and add tick system
        player.inventory.setHeldItem(hand, player.inventory.getHeldItem(hand).shrink(1))
        // These are dummy values for testing, until saturation and food level values
        // can be pulled from item definitions, and once more thought is in put into
        // fleshing out the handling of food consumption, etc.
        player.foodLevel += DUMMY_FOOD_LEVEL_INCREASE
        player.foodSaturationLevel += DUMMY_SATURATION_LEVEL_INCREASE
        return UseItemResult(InteractionResult.PASS, player.inventory.getHeldItem(hand))
    }
}
