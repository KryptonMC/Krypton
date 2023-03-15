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
package org.kryptonmc.krypton.packet.out.play

import com.google.common.collect.Sets
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.kryptonmc.krypton.coordinate.GlobalPos
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.registry.network.RegistrySerialization

@JvmRecord
data class PacketOutLogin(
    override val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: GameMode,
    val oldGameMode: GameMode?,
    val dimensions: Set<ResourceKey<World>>,
    val registryHolder: RegistryHolder,
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
    val deathLocation: GlobalPos?
) : EntityPacket {

    constructor(reader: BinaryReader) : this(
        reader.readInt(),
        reader.readBoolean(),
        GameModes.fromId(reader.readByte().toInt())!!,
        GameModes.fromId(reader.readByte().toInt())!!,
        reader.readCollection({ Sets.newHashSetWithExpectedSize(it) }) { ResourceKey.of(ResourceKeys.DIMENSION, reader.readKey()) },
        reader.decode(RegistrySerialization.NETWORK_CODEC),
        ResourceKey.of(ResourceKeys.DIMENSION_TYPE, reader.readKey()),
        ResourceKey.of(ResourceKeys.DIMENSION, reader.readKey()),
        reader.readLong(),
        reader.readVarInt(),
        reader.readVarInt(),
        reader.readVarInt(),
        reader.readBoolean(),
        reader.readBoolean(),
        reader.readBoolean(),
        reader.readBoolean(),
        reader.readNullable { GlobalPos(it) }
    )

    override fun write(writer: BinaryWriter) {
        writer.writeInt(entityId)
        writer.writeBoolean(isHardcore)
        writer.writeByte(gameMode.ordinal.toByte())
        val oldGameModeId = oldGameMode?.ordinal ?: -1
        writer.writeByte(oldGameModeId.toByte())
        writer.writeCollection(dimensions, writer::writeResourceKey)
        writer.encode(RegistrySerialization.NETWORK_CODEC, registryHolder)
        writer.writeResourceKey(dimensionType)
        writer.writeResourceKey(dimension)
        writer.writeLong(seed)
        writer.writeVarInt(maxPlayers)
        writer.writeVarInt(viewDistance)
        writer.writeVarInt(simulationDistance)
        writer.writeBoolean(reducedDebugInfo)
        writer.writeBoolean(enableRespawnScreen)
        writer.writeBoolean(isDebug)
        writer.writeBoolean(isFlat)
        writer.writeNullable(deathLocation) { _, pos -> pos.write(writer) }
    }
}
