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
import io.netty.buffer.ByteBuf
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.kryptonmc.krypton.coordinate.GlobalPos
import org.kryptonmc.krypton.registry.network.RegistrySerialization
import org.kryptonmc.krypton.util.decode
import org.kryptonmc.krypton.util.encode
import org.kryptonmc.krypton.util.readCollection
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeResourceKey
import org.kryptonmc.krypton.util.writeVarInt

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

    constructor(buf: ByteBuf) : this(
        buf.readInt(),
        buf.readBoolean(),
        GameModes.fromId(buf.readByte().toInt())!!,
        GameModes.fromId(buf.readByte().toInt())!!,
        buf.readCollection({ Sets.newHashSetWithExpectedSize(it) }) { ResourceKey.of(ResourceKeys.DIMENSION, buf.readKey()) },
        buf.decode(RegistrySerialization.NETWORK_CODEC),
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
        buf.readNullable { GlobalPos(it) }
    )

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeBoolean(isHardcore)
        buf.writeByte(gameMode.ordinal)
        buf.writeByte(oldGameMode?.ordinal ?: -1)
        buf.writeCollection(dimensions, buf::writeResourceKey)
        buf.encode(RegistrySerialization.NETWORK_CODEC, registryHolder)
        buf.writeResourceKey(dimensionType)
        buf.writeResourceKey(dimension)
        buf.writeLong(seed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)
        buf.writeVarInt(simulationDistance)
        buf.writeBoolean(reducedDebugInfo)
        buf.writeBoolean(enableRespawnScreen)
        buf.writeBoolean(isDebug)
        buf.writeBoolean(isFlat)
        buf.writeNullable(deathLocation) { _, pos -> pos.write(buf) }
    }
}
