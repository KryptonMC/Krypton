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
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.space.Rotation
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.util.asLong
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.registry.InternalRegistries
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

@Catalogue(MetadataSerializer::class)
object MetadataSerializers {

    @JvmField
    val BYTE = object : MetadataSerializer<Byte>(0) {
        override fun write(buf: ByteBuf, item: Byte) {
            buf.writeByte(item.toInt())
        }
    }

    @JvmField
    val VAR_INT = object : MetadataSerializer<Int>(1) {
        override fun write(buf: ByteBuf, item: Int) {
            buf.writeVarInt(item)
        }
    }

    @JvmField
    val FLOAT = object : MetadataSerializer<Float>(2) {
        override fun write(buf: ByteBuf, item: Float) {
            buf.writeFloat(item)
        }
    }

    @JvmField
    val STRING = object : MetadataSerializer<String>(3) {
        override fun write(buf: ByteBuf, item: String) {
            buf.writeString(item)
        }
    }

    @JvmField
    val COMPONENT = object : MetadataSerializer<Component>(4) {
        override fun write(buf: ByteBuf, item: Component) {
            buf.writeChat(item)
        }
    }

    @JvmField
    val OPTIONAL_COMPONENT = object : MetadataSerializer<Optional<Component>>(5) {
        override fun write(buf: ByteBuf, item: Optional<Component>) {
            buf.writeOptional(item) { buf.writeChat(it) }
        }
    }

    @JvmField
    val SLOT = object : MetadataSerializer<KryptonItemStack>(6) {
        override fun write(buf: ByteBuf, item: KryptonItemStack) {
            buf.writeItem(item)
        }

        override fun copy(item: KryptonItemStack) = item.copy()
    }

    @JvmField
    val BOOLEAN = object : MetadataSerializer<Boolean>(7) {
        override fun write(buf: ByteBuf, item: Boolean) {
            buf.writeBoolean(item)
        }
    }

    @JvmField
    val ROTATION = object : MetadataSerializer<Rotation>(8) {
        override fun write(buf: ByteBuf, item: Rotation) {
            buf.writeFloat(item.x)
            buf.writeFloat(item.y)
            buf.writeFloat(item.z)
        }
    }

    @JvmField
    val POSITION = object : MetadataSerializer<Vector3i>(9) {
        override fun write(buf: ByteBuf, item: Vector3i) {
            buf.writeLong(item.asLong())
        }
    }

    @JvmField
    val OPTIONAL_POSITION = object : MetadataSerializer<Optional<Vector3i>>(10) {
        override fun write(buf: ByteBuf, item: Optional<Vector3i>) {
            buf.writeOptional(item) { buf.writeLong(it.asLong()) }
        }
    }

    @JvmField
    val DIRECTION = object : MetadataSerializer<Direction>(11) {
        override fun write(buf: ByteBuf, item: Direction) {
            buf.writeEnum(item)
        }
    }

    @JvmField
    val OPTIONAL_UUID = object : MetadataSerializer<Optional<UUID>>(12) {
        override fun write(buf: ByteBuf, item: Optional<UUID>) {
            buf.writeOptional(item) { buf.writeUUID(it) }
        }
    }

    @JvmField
    val OPTIONAL_BLOCK_ID = object : MetadataSerializer<OptionalInt>(13) {
        override fun write(buf: ByteBuf, item: OptionalInt) {
            buf.writeBoolean(item.isPresent)
            if (item.isPresent) buf.writeVarInt(item.asInt)
        }
    }

    @JvmField
    val NBT = object : MetadataSerializer<CompoundTag>(14) {
        override fun write(buf: ByteBuf, item: CompoundTag) {
            buf.writeNBT(item)
        }
    }

    @JvmField
    val PARTICLE = object : MetadataSerializer<ParticleEffect>(15) {
        override fun write(buf: ByteBuf, item: ParticleEffect) {
            buf.writeVarInt(InternalRegistries.PARTICLE_TYPE.idOf(item.type))
            (item.data as? Writable)?.write(buf)
        }
    }

    @JvmField
    val VILLAGER_DATA = object : MetadataSerializer<VillagerData>(16) {
        override fun write(buf: ByteBuf, item: VillagerData) {
            buf.writeVarInt(item.type.id)
            buf.writeVarInt(item.profession.id)
            buf.writeVarInt(item.level)
        }
    }

    @JvmField
    val OPTIONAL_VAR_INT = object : MetadataSerializer<OptionalInt>(17) {
        override fun write(buf: ByteBuf, item: OptionalInt) {
            buf.writeVarInt(item.orElse(-1) + 1)
        }
    }

    @JvmField
    val POSE = object : MetadataSerializer<Pose>(18) {
        override fun write(buf: ByteBuf, item: Pose) {
            buf.writeEnum(item)
        }
    }
}
