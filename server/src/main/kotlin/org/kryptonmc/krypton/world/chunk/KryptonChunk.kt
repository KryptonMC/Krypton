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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.tracking.EntityTypeTarget
import org.kryptonmc.krypton.ticking.Tickable
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
import org.kryptonmc.krypton.world.chunk.data.ChunkStatus
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.nbt.CompoundTag
import java.util.function.Predicate

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonChunk(
    override val world: KryptonWorld,
    position: ChunkPos,
    sections: Array<ChunkSection?>,
    override var lastUpdate: Long,
    inhabitedTime: Long,
    val carvingMasks: Pair<ByteArray, ByteArray>?,
    val structures: CompoundTag?
) : ChunkAccessor(position, world, inhabitedTime, sections), Chunk, Tickable {

    override val status: ChunkStatus = ChunkStatus.FULL

    override val x: Int
        get() = position.x
    override val z: Int
        get() = position.z

    val cachedPacket: CachedPacket = CachedPacket { PacketOutChunkDataAndLight.fromChunk(this, true) }

    override val entities: Collection<Entity>
        get() = world.entityTracker.entitiesInChunk(position)
    override val players: Collection<Player>
        get() = world.entityTracker.entitiesInChunkOfType(position, EntityTypeTarget.PLAYERS)

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState {
//        if (world.isDebug) {
//            var block: KryptonBlock? = null
//            if (y == 60) block = KryptonBlocks.BARRIER.defaultState
//            if (y == 70) block = DebugGenerator.blockAt(x, z)
//            return block ?: KryptonBlocks.AIR.defaultState
//        }
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections().size) {
            val section = sections()[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15)
        }
        return KryptonBlocks.AIR.defaultState
    }

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState {
        val sectionIndex = getSectionIndex(y)
        if (sectionIndex >= 0 && sectionIndex < sections().size) {
            val section = sections()[sectionIndex]
            if (!section.hasOnlyAir()) return section.getBlock(x and 15, y and 15, z and 15).asFluid()
        }
        return KryptonFluids.EMPTY.defaultState
    }

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun setBlock(pos: Vec3i, state: KryptonBlockState, moving: Boolean): KryptonBlockState? {
        val section = sections()[getSectionIndex(pos.y)]
        if (section.hasOnlyAir() && state.isAir()) return null

        // Get the local coordinates and set the new state in the section
        val localX = pos.x and 15
        val localY = pos.y and 15
        val localZ = pos.z and 15
        val oldState = section.setBlock(localX, localY, localZ, state)
        if (oldState === state) return null

        // Update the heightmaps
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.OCEAN_FLOOR).update(localX, pos.y, localZ, state)
        heightmaps.getValue(Heightmap.Type.WORLD_SURFACE).update(localX, pos.y, localZ, state)
        cachedPacket.invalidate()
        return oldState
    }

    override fun getBlockEntity(x: Int, y: Int, z: Int): KryptonBlockEntity? = null // TODO: Implement

    override fun getBiome(x: Int, y: Int, z: Int): Biome = getNoiseBiome(x, y, z)

    override fun getBiome(position: Vec3i): Biome = getBiome(position.x, position.y, position.z)

    override fun tick(time: Long) {
        // Nothing we really want to do here yet
    }

    private fun lightSectionCount(): Int = sectionCount() + 2

    fun minimumLightSection(): Int = minimumSection() - 1

    fun maximumLightSection(): Int = minimumLightSection() + lightSectionCount()

    override fun <E : Entity> getEntitiesOfType(type: Class<E>): Collection<E> {
        return world.entityTracker.entitiesInChunkOfType(position, type, null)
    }

    override fun <E : Entity> getEntitiesOfType(type: Class<E>, predicate: Predicate<E>): Collection<E> {
        return world.entityTracker.entitiesInChunkOfType(position, type, predicate)
    }
}
