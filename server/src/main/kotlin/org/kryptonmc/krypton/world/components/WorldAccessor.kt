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

interface WorldAccessor : EntityGetter, ReadOnlyWorld, WriteOnlyWorld, WorldTimeAccessor {

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
