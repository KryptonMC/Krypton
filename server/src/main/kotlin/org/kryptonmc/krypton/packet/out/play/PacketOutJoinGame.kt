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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.encode
import org.kryptonmc.krypton.util.encode
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.nbt.compound

@JvmRecord
data class PacketOutJoinGame(
    override val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: GameMode,
    val oldGameMode: GameMode?,
    val worlds: Set<ResourceKey<World>>,
    val dimensionType: KryptonDimensionType,
    val dimension: ResourceKey<World>,
    val seed: Long,
    val maxPlayers: Int,
    val viewDistance: Int,
    val simulationDistance: Int,
    val reducedDebugInfo: Boolean,
    val doImmediateRespawn: Boolean,
    val isDebug: Boolean,
    val isFlat: Boolean
) : EntityPacket {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeBoolean(isHardcore)
        buf.writeByte(gameMode.ordinal)
        buf.writeByte(oldGameMode?.ordinal ?: -1)
        buf.writeCollection(worlds) { buf.writeKey(it.location) }
        buf.writeNBT(compound {
            put(
                ResourceKeys.DIMENSION_TYPE.location.asString(),
                Registries.DIMENSION_TYPE.encode(KryptonDimensionType.ENCODER)
            )
            put(
                ResourceKeys.BIOME.location.asString(),
                Registries.BIOME.encode(KryptonBiome.ENCODER)
            )
        })
        buf.encode(KryptonDimensionType.ENCODER, dimensionType)
        buf.writeKey(dimension.location)
        buf.writeLong(seed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)
        buf.writeVarInt(simulationDistance)
        buf.writeBoolean(reducedDebugInfo)
        buf.writeBoolean(!doImmediateRespawn)
        buf.writeBoolean(isDebug)
        buf.writeBoolean(isFlat)
    }
}
