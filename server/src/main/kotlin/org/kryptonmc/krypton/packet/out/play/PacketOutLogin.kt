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

import com.google.common.collect.Sets
import io.netty.buffer.ByteBuf
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.GlobalPosition
import org.kryptonmc.krypton.util.readCollection
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeResourceKey
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

@JvmRecord
data class PacketOutLogin(
    override val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: GameMode,
    val oldGameMode: GameMode?,
    val dimensions: Set<ResourceKey<World>>,
    val registryCodec: CompoundTag,
    val dimensionType: ResourceKey<DimensionType>,
    val dimension: ResourceKey<World>,
    val seed: Long,
    val maxPlayers: Int,
    val viewDistance: Int,
    val simulationDistance: Int,
    val reducedDebugInfo: Boolean,
    val enableRespawnScreen: Boolean,
    val isDebug: Boolean,
    val isFlat: Boolean,
    val deathLocation: GlobalPosition?
) : EntityPacket {

    constructor(buf: ByteBuf) : this(
        buf.readInt(),
        buf.readBoolean(),
        GameModes.fromId(buf.readByte().toInt())!!,
        GameModes.fromId(buf.readByte().toInt())!!,
        buf.readCollection({ Sets.newHashSetWithExpectedSize(it) }) { ResourceKey.of(ResourceKeys.DIMENSION, buf.readKey()) },
        buf.readNBT(),
        ResourceKey.of(ResourceKeys.DIMENSION_TYPE, buf.readKey()),
        ResourceKey.of(ResourceKeys.DIMENSION, buf.readKey()),
        buf.readLong(),
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readVarInt(),
        buf.readBoolean(),
        buf.readBoolean(),
        buf.readBoolean(),
        buf.readBoolean(),
        buf.readNullable { GlobalPosition(it) }
    )

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeBoolean(isHardcore)
        buf.writeByte(gameMode.ordinal)
        buf.writeByte(oldGameMode?.ordinal ?: -1)
        buf.writeCollection(dimensions, buf::writeResourceKey)
        buf.writeNBT(registryCodec)
        buf.writeResourceKey(dimensionType)
        buf.writeResourceKey(dimension)
        buf.writeLong(seed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)
        buf.writeVarInt(simulationDistance)
        buf.writeBoolean(reducedDebugInfo)
        buf.writeBoolean(!enableRespawnScreen)
        buf.writeBoolean(isDebug)
        buf.writeBoolean(isFlat)
        buf.writeNullable(deathLocation) { _, pos -> pos.write(buf) }
    }

    companion object {

        @JvmStatic
        fun createRegistryCodec(): CompoundTag = compound {
            /* FIXME: We need to rewrite this based on RegistryAccess
            put(ResourceKeys.DIMENSION_TYPE.location.asString(), KryptonRegistries.DIMENSION_TYPE.encode(KryptonDimensionType.DIRECT_CODEC))
            put(ResourceKeys.BIOME.location.asString(), KryptonRegistries.BIOME.encode(KryptonBiome.DIRECT_CODEC))
             */
        }
    }
}
