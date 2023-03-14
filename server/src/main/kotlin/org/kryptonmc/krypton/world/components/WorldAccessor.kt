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

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.ChunkManager
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.redstone.NeighbourUpdater

interface WorldAccessor : ReadOnlyWorld, WriteOnlyWorld, WorldTimeAccessor {

    val data: WorldData
    val chunkManager: ChunkManager
    val server: KryptonServer
    val random: RandomSource

    val difficulty: Difficulty
        get() = data.difficulty
    override val dayTime: Long
        get() = data.dayTime

    fun blockUpdated(pos: Vec3i, block: KryptonBlock)

    fun neighbourShapeChanged(direction: Direction, state: KryptonBlockState, pos: Vec3i, neighbourPos: Vec3i, flags: Int,
                              recursionLeft: Int) {
        NeighbourUpdater.executeShapeUpdate(this, direction, state, pos, neighbourPos, flags, recursionLeft)
    }

    fun playSound(pos: Vec3i, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float, except: KryptonPlayer?)

    fun playSound(pos: Vec3i, event: SoundEvent, source: Sound.Source, volume: Float, pitch: Float) {
        playSound(pos, event, source, volume, pitch, null)
    }

    fun worldEvent(pos: Vec3i, event: Int, data: Int, except: KryptonPlayer?)

    fun worldEvent(pos: Vec3i, event: Int, data: Int) {
        worldEvent(pos, event, data, null)
    }

    override fun hasChunk(x: Int, z: Int): Boolean = chunkManager.getChunk(x, z) != null
}
