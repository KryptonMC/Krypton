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
package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.asLong
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeOptional
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i
import java.util.OptionalInt
import java.util.UUID

@Catalogue(MetadataSerializer::class)
object MetadataSerializers {

    @JvmField
    val BYTE: MetadataSerializer<Byte> = create(0) { buf, item -> buf.writeByte(item.toInt()) }
    @JvmField
    val VAR_INT: MetadataSerializer<Int> = create(1, ByteBuf::writeVarInt)
    @JvmField
    val FLOAT: MetadataSerializer<Float> = create(2, ByteBuf::writeFloat)
    @JvmField
    val STRING: MetadataSerializer<String> = create(3, ByteBuf::writeString)
    @JvmField
    val COMPONENT: MetadataSerializer<Component> = create(4, ByteBuf::writeChat)
    @JvmField
    val OPTIONAL_COMPONENT: MetadataSerializer<Component?> = create(5) { buf, item -> buf.writeOptional(item, buf::writeChat) }
    @JvmField
    val SLOT: MetadataSerializer<KryptonItemStack> = create(6, ByteBuf::writeItem)
    @JvmField
    val BOOLEAN: MetadataSerializer<Boolean> = create(7, ByteBuf::writeBoolean)
    @JvmField
    val ROTATION: MetadataSerializer<Vector3f> = create(8) { buf, item ->
        buf.writeFloat(item.x())
        buf.writeFloat(item.y())
        buf.writeFloat(item.z())
    }
    @JvmField
    val POSITION: MetadataSerializer<Vector3i> = create(9) { buf, item -> buf.writeLong(item.asLong()) }
    @JvmField
    val OPTIONAL_POSITION: MetadataSerializer<Vector3i?> = create(10) { buf, item ->
        buf.writeOptional(item) { buf.writeLong(it.asLong()) }
    }
    @JvmField
    val DIRECTION: MetadataSerializer<Direction> = create(11, ByteBuf::writeEnum)
    @JvmField
    val OPTIONAL_UUID: MetadataSerializer<UUID?> = create(12) { buf, item -> buf.writeOptional(item, buf::writeUUID) }
    @JvmField
    val OPTIONAL_BLOCK: MetadataSerializer<Block?> = create(13) { buf, item -> buf.writeVarInt(item?.stateId ?: 0) }
    @JvmField
    val NBT: MetadataSerializer<CompoundTag> = create(14, ByteBuf::writeNBT)
    @JvmField
    val PARTICLE: MetadataSerializer<ParticleEffect> = create(15) { buf, item ->
        buf.writeVarInt(Registries.PARTICLE_TYPE.idOf(item.type))
        val data = item.data
        if (data is Writable) data.write(buf)
    }
    @JvmField
    val VILLAGER_DATA: MetadataSerializer<VillagerData> = create(16) { buf, item ->
        buf.writeVarInt(item.type.ordinal)
        buf.writeVarInt(item.profession.ordinal)
        buf.writeVarInt(item.level)
    }
    @JvmField
    val OPTIONAL_VAR_INT: MetadataSerializer<OptionalInt> = create(17) { buf, item -> buf.writeVarInt(item.orElse(-1) + 1) }
    @JvmField
    val POSE: MetadataSerializer<Pose> = create(18, ByteBuf::writeEnum)

    @JvmStatic
    private inline fun <T> create(id: Int, crossinline writer: (ByteBuf, T) -> Unit): MetadataSerializer<T> = object : MetadataSerializer<T>(id) {
        override fun write(buf: ByteBuf, item: T) {
            writer(buf, item)
        }
    }
}
