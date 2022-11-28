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

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.chunk.BlockChangeFlags
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.EntityManager
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.world.KryptonWorldBorder
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.ChunkPos
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import java.nio.file.Path

interface BaseWorld : World, WorldAccessor, PacketGroupingAudience {

    override val server: KryptonServer
    override val dimensionType: KryptonDimensionType
    override val border: KryptonWorldBorder
    val entityManager: EntityManager

    override val chunks: Collection<KryptonChunk>
        get() = chunkManager.chunkMap.values
    override val entities: Collection<KryptonEntity>
        get() = entityManager.entities

    override val name: String
        get() = data.name
    override val folder: Path
        get() = data.folder
    override val spawnLocation: BlockPos
        get() = BlockPos(data.spawnX, data.spawnY, data.spawnZ)
    override val difficulty: Difficulty
        get() = data.difficulty
    override val gameMode: GameMode
        get() = data.gameMode
    override val isHardcore: Boolean
        get() = data.isHardcore
    override val time: Long
        get() = data.time
    override val gameRules: KryptonGameRuleHolder
        get() = data.gameRules
    override val scoreboard: KryptonScoreboard
        get() = server.scoreboard

    override fun getBlock(x: Int, y: Int, z: Int): KryptonBlockState

    override fun getBlock(position: Vec3i): KryptonBlockState = getBlock(position.x, position.y, position.z)

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockState, flags: BlockChangeFlags): Boolean =
        setBlock(BlockPos(x, y, z), block.downcast(), flags.raw)

    override fun setBlock(position: Vec3i, block: BlockState, flags: BlockChangeFlags): Boolean =
        setBlock(BlockPos.from(position), block.downcast(), flags.raw)

    override fun getFluid(x: Int, y: Int, z: Int): KryptonFluidState

    override fun getFluid(position: Vec3i): KryptonFluidState = getFluid(position.x, position.y, position.z)

    override fun getBlockEntity(x: Int, y: Int, z: Int): KryptonBlockEntity? = null

    override fun getChunkAt(x: Int, z: Int): KryptonChunk? = chunkManager.get(x, z)

    override fun getChunk(x: Int, y: Int, z: Int): KryptonChunk? = getChunkAt(SectionPos.blockToSection(x), SectionPos.blockToSection(z))

    override fun getChunk(position: Vec3i): KryptonChunk? = getChunk(position.x, position.y, position.z)

    override fun loadChunk(x: Int, z: Int): KryptonChunk? = chunkManager.load(x, z, Ticket(TicketTypes.API_LOAD, 31, ChunkPos.pack(x, z)))

    override fun unloadChunk(x: Int, z: Int, force: Boolean) {
        chunkManager.unload(x, z, TicketTypes.API_LOAD, force)
    }

    override fun sendGroupedPacket(players: Collection<KryptonPlayer>, packet: Packet) {
        server.sessionManager.sendGrouped(players, packet)
    }
}
