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
package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.space.Rotation
import org.kryptonmc.api.util.asLong
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.write
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeOptional
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i
import java.util.Optional
import java.util.OptionalInt
import java.util.UUID

object MetadataSerializers {

    private val SERIALIZERS = IntIdentityHashBiMap<MetadataSerializer<*>>(16)

    @JvmField
    val BYTE = object : MetadataSerializer<Byte> {
        override fun write(buf: ByteBuf, item: Byte) {
            buf.writeByte(item.toInt())
        }
    }

    @JvmField
    val VAR_INT = object : MetadataSerializer<Int> {
        override fun write(buf: ByteBuf, item: Int) = buf.writeVarInt(item)
    }

    @JvmField
    val FLOAT = object : MetadataSerializer<Float> {
        override fun write(buf: ByteBuf, item: Float) {
            buf.writeFloat(item)
        }
    }

    @JvmField
    val STRING = object : MetadataSerializer<String> {
        override fun write(buf: ByteBuf, item: String) = buf.writeString(item)
    }

    @JvmField
    val COMPONENT = object : MetadataSerializer<Component> {
        override fun write(buf: ByteBuf, item: Component) = buf.writeChat(item)
    }

    @JvmField
    val OPTIONAL_COMPONENT = object : MetadataSerializer<Optional<Component>> {
        override fun write(buf: ByteBuf, item: Optional<Component>) = buf.writeOptional(item) { buf.writeChat(it) }
    }

    @JvmField
    val SLOT = object : MetadataSerializer<KryptonItemStack> {
        override fun write(buf: ByteBuf, item: KryptonItemStack) = buf.writeItem(item)

        override fun copy(item: KryptonItemStack) = item.copy()
    }

    @JvmField
    val BOOLEAN = object : MetadataSerializer<Boolean> {
        override fun write(buf: ByteBuf, item: Boolean) {
            buf.writeBoolean(item)
        }
    }

    @JvmField
    val ROTATION = object : MetadataSerializer<Rotation> {
        override fun write(buf: ByteBuf, item: Rotation) {
            buf.writeFloat(item.x)
            buf.writeFloat(item.y)
            buf.writeFloat(item.z)
        }
    }

    @JvmField
    val POSITION = object : MetadataSerializer<Vector3i> {
        override fun write(buf: ByteBuf, item: Vector3i) {
            buf.writeLong(item.asLong())
        }
    }

    @JvmField
    val OPTIONAL_POSITION = object : MetadataSerializer<Optional<Vector3i>> {
        override fun write(buf: ByteBuf, item: Optional<Vector3i>) = buf.writeOptional(item) { buf.writeLong(it.asLong()) }
    }

    @JvmField
    val DIRECTION = object : MetadataSerializer<Direction> {
        override fun write(buf: ByteBuf, item: Direction) = buf.writeEnum(item)
    }

    @JvmField
    val OPTIONAL_UUID = object : MetadataSerializer<Optional<UUID>> {
        override fun write(buf: ByteBuf, item: Optional<UUID>) = buf.writeOptional(item) { buf.writeUUID(it) }
    }

    @JvmField
    val OPTIONAL_BLOCK_ID = object : MetadataSerializer<OptionalInt> {
        override fun write(buf: ByteBuf, item: OptionalInt) {
            buf.writeBoolean(item.isPresent)
            if (item.isPresent) buf.writeVarInt(item.asInt)
        }
    }

    @JvmField
    val NBT = object : MetadataSerializer<CompoundTag> {
        override fun write(buf: ByteBuf, item: CompoundTag) = buf.writeNBT(item)
    }

    @JvmField
    val PARTICLE = object : MetadataSerializer<ParticleEffect> {
        override fun write(buf: ByteBuf, item: ParticleEffect) {
            buf.writeVarInt(Registries.PARTICLE_TYPE.idOf(item.type))
            item.write(buf)
        }
    }

    @JvmField
    val VILLAGER_DATA = object : MetadataSerializer<VillagerData> {
        override fun write(buf: ByteBuf, item: VillagerData) {
            buf.writeVarInt(item.type.id)
            buf.writeVarInt(item.profession.id)
            buf.writeVarInt(item.level)
        }
    }

    @JvmField
    val OPTIONAL_VAR_INT = object : MetadataSerializer<OptionalInt> {
        override fun write(buf: ByteBuf, item: OptionalInt) = buf.writeVarInt(item.orElse(-1) + 1)
    }

    @JvmField
    val POSE = object : MetadataSerializer<Pose> {
        override fun write(buf: ByteBuf, item: Pose) = buf.writeEnum(item)
    }

    init {
        SERIALIZERS.add(BYTE)
        SERIALIZERS.add(VAR_INT)
        SERIALIZERS.add(FLOAT)
        SERIALIZERS.add(STRING)
        SERIALIZERS.add(COMPONENT)
        SERIALIZERS.add(OPTIONAL_COMPONENT)
        SERIALIZERS.add(SLOT)
        SERIALIZERS.add(BOOLEAN)
        SERIALIZERS.add(ROTATION)
        SERIALIZERS.add(POSITION)
        SERIALIZERS.add(OPTIONAL_POSITION)
        SERIALIZERS.add(DIRECTION)
        SERIALIZERS.add(OPTIONAL_UUID)
        SERIALIZERS.add(OPTIONAL_BLOCK_ID)
        SERIALIZERS.add(NBT)
        SERIALIZERS.add(PARTICLE)
        SERIALIZERS.add(VILLAGER_DATA)
        SERIALIZERS.add(OPTIONAL_VAR_INT)
        SERIALIZERS.add(POSE)
    }

    @JvmStatic
    operator fun get(id: Int) = SERIALIZERS[id]

    @JvmStatic
    fun idOf(serializer: MetadataSerializer<*>) = SERIALIZERS.idOf(serializer)
}
