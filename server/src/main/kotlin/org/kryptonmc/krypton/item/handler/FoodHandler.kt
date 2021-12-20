/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.UseItemResult

object FoodHandler : ItemTimedHandler {

    override fun finishUse(player: KryptonPlayer, hand: Hand): UseItemResult {
        // TODO: Remove hardcoded values and add tick system
        val stack = player.inventory.heldItem(hand).shrink(1)
        player.inventory.setHeldItem(hand, stack)
        // These are dummy values for testing, until saturation and food level values
        // can be pulled from item definitions, and once more thought is in put into
        // fleshing out the handling of food consumption, etc.
        player.foodLevel += 8
        player.foodSaturationLevel += 12.8f
        return UseItemResult(InteractionResult.PASS, player.inventory.heldItem(hand))
    }
}
