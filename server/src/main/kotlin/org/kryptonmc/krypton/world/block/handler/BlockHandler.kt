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
package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.krypton.effect.Effect
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.destroySpeed
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

/**
 * A handler for a block.
 *
 * This helps promote sharing, as these can be reused between multiple block
 * objects that share properties.
 */
interface BlockHandler {

    /**
     * Calculates the destroy progress of the given [block] at the given
     * [position], being broken by the given [player] in the given [world].
     */
    fun calculateDestroyProgress(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i): Float {
        val hardness = block.hardness
        if (hardness == -1.0) return 0F
        val factor = if (hasCorrectTool(player, block)) 30 else 100
        return calculateDestroySpeed(player, block) / hardness.toFloat() / factor.toFloat()
    }

    /**
     * Called when the given [block] is placed by the given [player] at the
     * given [position], being placed on the given [face] (this will be facing
     * the player).
     */
    fun onPlace(player: KryptonPlayer, block: Block, position: Vector3i, face: BlockFace) {
        // This isn't what it may seem like. This is for reacting to block placements, which
        // most blocks don't need to do.
    }

    /**
     * Called when the given [player] is about to break the given [block] at
     * the given [position] in the given [world], but hasn't yet.
     */
    fun preDestroy(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i) {
        spawnDestroyParticles(world, player, position, block)
    }

    /**
     * Called when the given [block] is destroyed by the given [player] at the
     * given [position], with the given [item].
     */
    fun onDestroy(player: KryptonPlayer, block: Block, position: Vector3i, item: ItemStack) {
        // TODO: drop items
        // 0.005/block is the vanilla food exhaustion per block to be added to the player
        // Source: https://minecraft.fandom.com/wiki/Hunger#Exhaustion_level_increase
        player.foodExhaustionLevel += 0.005f
        player.statistics.increment(StatisticTypes.BLOCK_MINED[block])
    }

    /**
     * Called when the given [player] interacts with the given [block] at the
     * given [position] in the given [world], using the given interaction
     * [hand].
     */
    fun interact(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i, hand: Hand): InteractionResult = InteractionResult.PASS

    /**
     * Called when the given [player] attacks the given [block] at the given
     * [position] in the given [world].
     */
    fun attack(player: KryptonPlayer, world: KryptonWorld, block: Block, position: Vector3i) {
        // all blocks do nothing when attacked by default, breaking isn't handled here
    }

    /**
     * Called when the shape of the given [block] at the given [position]
     * facing the given [direction] needs its face updated, with the given
     * [neighbour] at the given [neighbourPosition].
     */
    fun updateShape(
        block: Block,
        position: Vector3i,
        direction: Direction,
        neighbour: Block,
        neighbourPosition: Vector3i,
        world: KryptonWorld
    ): Block = block

    fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, position: Vector3i, block: Block) {
        world.playEffect(Effect.DESTROY_BLOCK, position, block.stateId, player)
    }

    companion object {

        private fun hasCorrectTool(
            player: KryptonPlayer,
            block: Block
        ): Boolean = !block.requiresCorrectTool || player.inventory.mainHand.type.handler().isCorrectTool(block)

        private fun calculateDestroySpeed(player: KryptonPlayer, block: Block): Float {
            var speed = player.inventory.mainHand.destroySpeed(block)
            if (!player.isOnGround) speed /= 5F
            return speed
        }
    }
}
