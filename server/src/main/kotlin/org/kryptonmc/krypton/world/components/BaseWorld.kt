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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.chunk.BlockChangeFlags
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.EntityManager
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.world.KryptonWorldBorder
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.state.downcast
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.rule.WorldGameRules

interface BaseWorld : World, WorldAccessor, PacketGroupingAudience {

    override val server: KryptonServer
    override val dimensionType: KryptonDimensionType
    override val border: KryptonWorldBorder
    val entityManager: EntityManager

    override val chunks: Collection<KryptonChunk>
        get() = chunkManager.chunks()
    override val entities: Collection<KryptonEntity>
        get() = entityManager.entities()

    override val name: String
        get() = data.name
    override val spawnLocation: Vec3i
        get() = Vec3i(data.spawnX, data.spawnY, data.spawnZ)
    override val difficulty: Difficulty
        get() = data.difficulty
    override val gameMode: GameMode
        get() = data.gameMode
    override val time: Long
        get() = data.time
    override val seed: Long
        get() = data.generationSettings.seed

    override fun isHardcore(): Boolean = data.isHardcore

    @Suppress("UNCHECKED_CAST")
    override fun <V> getGameRule(rule: GameRule<V>): V = (rule as WorldGameRules.Key<*>).get(data.gameRules.getRule(rule)) as V

    override fun <V> setGameRule(rule: GameRule<V>, value: V & Any) {
        when (val ruleValue = data.gameRules.getRule(rule as WorldGameRules.Key<*>)) {
            is WorldGameRules.BooleanValue -> ruleValue.set(value as Boolean, server)
            is WorldGameRules.IntegerValue -> ruleValue.set(value as Int, server)
        }
    }

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockState, flags: BlockChangeFlags): Boolean {
        return setBlock(Vec3i(x, y, z), block.downcast(), flags.raw)
    }

    override fun setBlock(position: Vec3i, block: BlockState, flags: BlockChangeFlags): Boolean {
        return setBlock(position, block.downcast(), flags.raw)
    }

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun getBlockEntity(x: Int, y: Int, z: Int): KryptonBlockEntity? = null

    override fun getChunk(x: Int, z: Int): KryptonChunk? = chunkManager.getChunk(x, z)

    override fun loadChunk(x: Int, z: Int): KryptonChunk? = chunkManager.loadChunk(ChunkPos(x, z))

    override fun unloadChunk(x: Int, z: Int) {
        chunkManager.unloadChunk(x, z)
    }
}
