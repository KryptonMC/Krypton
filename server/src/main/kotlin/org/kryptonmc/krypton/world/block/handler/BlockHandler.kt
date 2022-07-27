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
package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.krypton.world.WorldEvent
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.destroySpeed
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.shapes.CollisionContext
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock

/**
 * A handler for a block.
 *
 * This helps promote sharing, as these can be reused between multiple block
 * objects that share properties.
 */
interface BlockHandler {

    /**
     * Calculates the destroy progress of the block being broken by the player.
     */
    fun calculateDestroyProgress(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlock, x: Int, y: Int, z: Int): Float {
        val hardness = block.hardness
        if (hardness == -1.0) return 0F
        val factor = if (player.isCorrectTool(block)) 30 else 100
        return player.calculateDestroySpeed(block) / hardness.toFloat() / factor.toFloat()
    }

    /**
     * Called when the player is about to break the block but hasn't yet.
     */
    fun preDestroy(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlock, x: Int, y: Int, z: Int) {
        spawnDestroyParticles(world, player, x, y, z, block)
    }

    fun destroy(world: KryptonWorld, x: Int, y: Int, z: Int, block: KryptonBlock) {
        // do nothing by default
    }

    /**
     * Called when the block is destroyed by the player with the item.
     */
    fun onDestroy(player: KryptonPlayer, block: KryptonBlock, x: Int, y: Int, z: Int, item: ItemStack) {
        // TODO: drop items
        // 0.005/block is the vanilla food exhaustion per block to be added to the player
        // Source: https://minecraft.fandom.com/wiki/Hunger#Exhaustion_level_increase
        player.foodExhaustionLevel += 0.005f
        player.statistics.increment(StatisticTypes.BLOCK_MINED[block])
    }

    /**
     * Called when the player attacks the block.
     */
    fun attack(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlock, x: Int, y: Int, z: Int) {
        // all blocks do nothing when attacked by default, breaking isn't handled here
    }

    fun getShape(block: KryptonBlock, accessor: BlockAccessor, x: Int, y: Int, z: Int, context: CollisionContext): VoxelShape = Shapes.block()

    fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, x: Int, y: Int, z: Int, block: KryptonBlock) {
        world.worldEvent(WorldEvent.DESTROY_BLOCK, x, y, z, block.stateId, player)
    }
}

private fun KryptonPlayer.isCorrectTool(block: KryptonBlock): Boolean = !block.requiresCorrectTool ||
        inventory.mainHand.type.handler().isCorrectTool(block)

private fun KryptonPlayer.calculateDestroySpeed(block: KryptonBlock): Float {
    var speed = inventory.mainHand.destroySpeed(block)
    if (underFluid(FluidTags.WATER)) speed /= 5F
    if (!isOnGround) speed /= 5F
    return speed
}
