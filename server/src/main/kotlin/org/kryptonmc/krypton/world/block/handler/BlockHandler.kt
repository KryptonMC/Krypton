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

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.projectile.KryptonProjectile
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.util.math.Mirror
import org.kryptonmc.krypton.util.math.Rotation
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.WorldEvents
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.components.WorldAccessor

// TODO: Perhaps this could eventually be replaced with events?
interface BlockHandler {

    fun isApplicableBlockType(name: String): Boolean

    fun getDestroyProgress(state: KryptonBlockState, player: KryptonPlayer, world: BlockGetter, position: BlockPos): Float {
        val speed = state.block.properties.destroyTime
        if (speed == -1F) return 0F
        val correctToolBonus = if (player.hasCorrectTool(state)) 30 else 100
        return player.getDestroySpeed(state) / speed / correctToolBonus
    }

    fun onPlace(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, oldState: KryptonBlockState, isMoving: Boolean) {
        // Do nothing by default
    }

    fun setPlacedBy(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, placer: KryptonLivingEntity?, heldItem: KryptonItemStack) {
        // Do nothing by default
    }

    fun destroy(world: WorldAccessor, pos: BlockPos, state: KryptonBlockState) {
        // Do nothing by default
    }

    fun playerWillDestroy(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, player: KryptonPlayer) {
        spawnDestroyParticles(world, player, pos, state)
        // TODO: Anger nearby piglins if state is guarded by piglins and trigger game event for sculk sensors
    }

    fun playerDestroy(world: KryptonWorld, state: KryptonBlockState, pos: BlockPos, blockEntity: KryptonBlockEntity?, destroyer: KryptonPlayer,
                      heldItem: KryptonItemStack) {
        destroyer.statisticsTracker.incrementStatistic(StatisticTypes.BLOCK_MINED.get().getStatistic(state.block))
        // TODO: Cause exhaustion and drop items
    }

    fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, pos: BlockPos, state: KryptonBlockState) {
        world.worldEvent(pos, WorldEvents.DESTROY_BLOCK, KryptonBlock.idOf(state), player)
    }

    fun onRemove(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, newState: KryptonBlockState, isMoving: Boolean) {
        if (state.hasBlockEntity() && !state.eq(newState.block)) {
            // TODO: Remove block entity from world
        }
    }

    fun spawnAfterBreak(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, item: KryptonItemStack, dropItems: Boolean) {
        // Do nothing by default
    }

    fun use(state: KryptonBlockState, world: BlockGetter, pos: BlockPos, user: KryptonPlayer, usedHand: Hand,
            hitResult: BlockHitResult): InteractionResult {
        return InteractionResult.PASS
    }

    fun attack(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, player: KryptonPlayer) {
        // Do nothing by default
    }

    fun triggerEvent(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, id: Int, parameter: Int): Boolean {
        return false
    }

    fun updateIndirectNeighbourShapes(state: KryptonBlockState, world: BlockGetter, pos: BlockPos, flags: Int, recursionLeft: Int) {
        // Do nothing by default
    }

    fun updateShape(world: BlockGetter, state: KryptonBlockState, currentPos: BlockPos, neighbour: KryptonBlockState, neighbourPos: BlockPos,
                    direction: Direction): KryptonBlockState {
        return state
    }

    fun entityInside(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, entity: KryptonEntity) {
        // Do nothing by default
    }

    fun onProjectileHit(world: KryptonWorld, state: KryptonBlockState, hitResult: BlockHitResult, projectile: KryptonProjectile) {
        // Do nothing by default
    }

    fun rotate(state: KryptonBlockState, rotation: Rotation): KryptonBlockState {
        return state
    }

    fun mirror(state: KryptonBlockState, mirror: Mirror): KryptonBlockState {
        return state
    }

    fun neighbourChanged(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos,
                         moving: Boolean) {
        // Do nothing by default
    }

    fun stepOn(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, entity: KryptonEntity) {
        // Do nothing by default
    }

    fun fallOn(world: KryptonWorld, state: KryptonBlockState, pos: BlockPos, entity: KryptonEntity, fallDistance: Float) {
        // TODO: Cause fall damage to entity
    }

    fun handlePrecipitation(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, precipitation: Precipitation) {
        // Do nothing by default
    }
}
