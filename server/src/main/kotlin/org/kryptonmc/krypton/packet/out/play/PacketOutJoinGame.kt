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
package org.kryptonmc.krypton.packet.out.play

import com.google.common.hash.Hashing
import io.netty.buffer.ByteBuf
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.util.encode
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.generation.DebugGenerator
import org.kryptonmc.krypton.world.generation.FlatGenerator
import java.util.function.Supplier

/**
 * This packet is used to initialise some things that the client needs to know so that it can join the game
 * properly.
 *
 * @param entityId the entity ID of the client
 * @param isHardcore if the server is in hardcore mode
 * @param world the world the client is spawning into
 * @param gamemode the client's current gamemode
 * @param previousGamemode the client's previous gamemode
 * @param dimension the key of the dimension the client is spawning into (e.g. "minecraft:overworld" for the overworld)
 * @param maxPlayers the maximum players allowed on the server
 * @param viewDistance the server's maximum render distance
 */
class PacketOutJoinGame(
    private val entityId: Int,
    private val isHardcore: Boolean = false,
    private val world: KryptonWorld,
    private val registryHolder: RegistryHolder,
    private val gamemode: Gamemode,
    private val previousGamemode: Gamemode? = null,
    private val dimension: DimensionType,
    private val maxPlayers: Int = 20,
    private val viewDistance: Int = 10
) : PlayPacket(0x26) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeBoolean(isHardcore)
        buf.writeByte(gamemode.ordinal)
        buf.writeByte(previousGamemode?.ordinal ?: -1)

        // worlds that exist
        // FIXME: Use a key-based system for indexing levels
        buf.writeVarInt(3)
        buf.writeKey(DimensionTypes.OVERWORLD_KEY.location)
        buf.writeKey(DimensionTypes.NETHER_KEY.location)
        buf.writeKey(DimensionTypes.END_KEY.location)

        // network codec (dimension type and worldgen/biome resources) and info about current dimension type
        buf.encode(registryHolder, RegistryHolder.NETWORK_CODEC)
        buf.encode({ dimension }, DimensionTypes.CODEC)

        val hashedSeed = Hashing.sha256().hashLong(world.generationSettings.seed).asLong()
        val dimensionKey = world.dimension.location

        buf.writeKey(dimensionKey) // world spawning into
        buf.writeLong(hashedSeed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)

        buf.writeBoolean(world.gameRules[GameRules.REDUCED_DEBUG_INFO])
        buf.writeBoolean(world.gameRules[GameRules.DO_IMMEDIATE_RESPAWN])

        val generator = world.generationSettings.dimensions[Dimension.OVERWORLD]!!.generator
        buf.writeBoolean(generator is DebugGenerator) // is debug world
        buf.writeBoolean(generator is FlatGenerator) // is flat world
    }
}
