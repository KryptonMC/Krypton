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
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class PacketOutBlockEntityData(
    val x: Int,
    val y: Int,
    val z: Int,
    val type: Int,
    val nbt: CompoundTag?
) : Packet {

    constructor(
        position: Vector3i,
        type: BlockEntityType,
        nbt: CompoundTag?
    ) : this(position.x(), position.y(), position.z(), Registries.BLOCK_ENTITY_TYPE.idOf(type), nbt)

    constructor(entity: KryptonBlockEntity) : this(entity, entity::updateTag)

    constructor(entity: KryptonBlockEntity, nbtCreator: () -> CompoundTag?) : this(entity.position, entity.type, nbtCreator())

    override fun write(buf: ByteBuf) {
        buf.writeVector(x, y, z)
        buf.writeVarInt(type)
        buf.writeNBT(nbt)
    }
}
