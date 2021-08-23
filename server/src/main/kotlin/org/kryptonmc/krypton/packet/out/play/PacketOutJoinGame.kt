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

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.encode
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType

class PacketOutJoinGame(
    private val id: Int,
    private val isHardcore: Boolean,
    private val gamemode: Gamemode,
    private val oldGamemode: Gamemode?,
    private val worlds: Set<ResourceKey<World>>,
    private val dimensionType: KryptonDimensionType,
    private val dimension: ResourceKey<World>,
    private val seed: Long,
    private val maxPlayers: Int,
    private val viewDistance: Int,
    private val reducedDebugInfo: Boolean,
    private val doImmediateRespawn: Boolean,
    private val isDebug: Boolean,
    private val isFlat: Boolean
) : PlayPacket(0x26) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(id)
        buf.writeBoolean(isHardcore)
        buf.writeByte(gamemode.ordinal)
        buf.writeByte(oldGamemode?.ordinal ?: -1)
        buf.writeCollection(worlds) { buf.writeKey(it.location) }
        // TODO: Replace this
//        buf.encode(RegistryHolder.NETWORK_CODEC, registryHolder)
        buf.encode(KryptonDimensionType.CODEC, dimensionType)
        buf.writeKey(dimension.location)
        buf.writeLong(seed)
        buf.writeVarInt(maxPlayers)
        buf.writeVarInt(viewDistance)
        buf.writeBoolean(reducedDebugInfo)
        buf.writeBoolean(!doImmediateRespawn)
        buf.writeBoolean(isDebug)
        buf.writeBoolean(isFlat)
    }
}
