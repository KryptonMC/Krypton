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
import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.effect.Effect
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

interface KryptonBlockHandler : BlockHandler {

    override fun calculateDestroyProgress(player: Player, world: World, block: Block, position: Vector3i): Float {
        val hardness = block.hardness
        if (hardness == -1.0) return 0F
        val factor = if (player.hasCorrectTool(block)) 30 else 100
        return player.destroySpeed(block) / hardness.toFloat() / factor.toFloat()
    }

    override fun attack(player: Player, world: World, block: Block, position: Vector3i) = Unit

    override fun interact(
        player: Player,
        world: World,
        block: Block,
        position: Vector3i,
        hand: Hand
    ) = InteractionResult.PASS

    override fun onPlace(player: Player, block: Block, position: Vector3i, face: BlockFace) = Unit

    override fun preDestroy(player: Player, world: World, block: Block, position: Vector3i) {
        if (player !is KryptonPlayer || world !is KryptonWorld) return
        spawnDestroyParticles(world, player, position, block)
    }

    open fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, position: Vector3i, block: Block) {
        world.playEffect(Effect.DESTROY_BLOCK, position, block.stateId, player)
    }

    override fun onDestroy(player: Player, block: Block, position: Vector3i, item: ItemStack) {
        // TODO: Award block mined statistic and drop items
        // 0.005/block is the vanilla food exhaustion per block to be added to the player
        // Source: https://minecraft.fandom.com/wiki/Hunger#Exhaustion_level_increase
        player.foodExhaustionLevel += 0.005f
    }

    override fun updateShape(
        block: Block,
        position: Vector3i,
        direction: Direction,
        neighbour: Block,
        neighbourPosition: Vector3i,
        world: World
    ): Block = block
}
