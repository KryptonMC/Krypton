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
package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Precipitation
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

    fun getDestroyProgress(state: KryptonBlockState, player: KryptonPlayer, world: BlockGetter, position: Vec3i): Float {
        val speed = state.block.properties.destroyTime
        if (speed == -1F) return 0F
        val correctToolBonus = if (player.hasCorrectTool(state)) 30 else 100
        return player.getDestroySpeed(state) / speed / correctToolBonus
    }

    fun onPlace(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, oldState: KryptonBlockState, isMoving: Boolean) {
        // Do nothing by default
    }

    fun setPlacedBy(world: KryptonWorld, pos: Vec3i, state: KryptonBlockState, placer: KryptonLivingEntity?, heldItem: KryptonItemStack) {
        // Do nothing by default
    }

    fun destroy(world: WorldAccessor, pos: Vec3i, state: KryptonBlockState) {
        // Do nothing by default
    }

    fun playerWillDestroy(world: KryptonWorld, pos: Vec3i, state: KryptonBlockState, player: KryptonPlayer) {
        spawnDestroyParticles(world, player, pos, state)
        // TODO: Anger nearby piglins if state is guarded by piglins and trigger game event for sculk sensors
    }

    fun playerDestroy(world: KryptonWorld, state: KryptonBlockState, pos: Vec3i, blockEntity: KryptonBlockEntity?, destroyer: KryptonPlayer,
                      heldItem: KryptonItemStack) {
        destroyer.statisticsTracker.incrementStatistic(StatisticTypes.BLOCK_MINED.get().getStatistic(state.block))
        // TODO: Cause exhaustion and drop items
    }

    fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, pos: Vec3i, state: KryptonBlockState) {
        world.worldEvent(pos, WorldEvents.DESTROY_BLOCK, KryptonBlock.idOf(state), player)
    }

    fun onRemove(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, newState: KryptonBlockState, isMoving: Boolean) {
        if (state.hasBlockEntity() && !state.eq(newState.block)) {
            // TODO: Remove block entity from world
        }
    }

    fun spawnAfterBreak(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, item: KryptonItemStack, dropItems: Boolean) {
        // Do nothing by default
    }

    fun use(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, user: KryptonPlayer, usedHand: Hand,
            hitResult: BlockHitResult): InteractionResult {
        return InteractionResult.PASS
    }

    fun attack(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, player: KryptonPlayer) {
        // Do nothing by default
    }

    fun triggerEvent(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, id: Int, parameter: Int): Boolean {
        return false
    }

    fun updateIndirectNeighbourShapes(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, flags: Int, recursionLeft: Int) {
        // Do nothing by default
    }

    fun updateShape(world: BlockGetter, state: KryptonBlockState, currentPos: Vec3i, neighbour: KryptonBlockState, neighbourPos: Vec3i,
                    direction: Direction): KryptonBlockState {
        return state
    }

    fun entityInside(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, entity: KryptonEntity) {
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

    fun neighbourChanged(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i,
                         moving: Boolean) {
        // Do nothing by default
    }

    fun stepOn(world: KryptonWorld, pos: Vec3i, state: KryptonBlockState, entity: KryptonEntity) {
        // Do nothing by default
    }

    fun fallOn(world: KryptonWorld, state: KryptonBlockState, pos: Vec3i, entity: KryptonEntity, fallDistance: Float) {
        // TODO: Cause fall damage to entity
    }

    fun handlePrecipitation(state: KryptonBlockState, world: KryptonWorld, pos: Vec3i, precipitation: Precipitation) {
        // Do nothing by default
    }
}
