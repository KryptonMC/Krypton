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
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.space.Direction
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.Rotation
import org.kryptonmc.krypton.util.asLong
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeNBTCompound
import org.kryptonmc.krypton.util.writeOptional
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.spongepowered.math.vector.Vector3i
import java.util.Optional
import java.util.OptionalInt
import java.util.UUID

object EntityDataSerializers {

    private val SERIALIZERS = IntIdentityHashBiMap(EntityDataSerializer::class.java, 16)

    @JvmField
    val BYTE = object : EntityDataSerializer<Byte> {
        override fun write(buf: ByteBuf, item: Byte) {
            buf.writeByte(item.toInt())
        }
    }

    @JvmField
    val INT = object : EntityDataSerializer<Int> {
        override fun write(buf: ByteBuf, item: Int) = buf.writeVarInt(item)
    }

    @JvmField
    val FLOAT = object : EntityDataSerializer<Float> {
        override fun write(buf: ByteBuf, item: Float) {
            buf.writeFloat(item)
        }
    }

    @JvmField
    val STRING = object : EntityDataSerializer<String> {
        override fun write(buf: ByteBuf, item: String) = buf.writeString(item)
    }

    @JvmField
    val COMPONENT = object : EntityDataSerializer<Component> {
        override fun write(buf: ByteBuf, item: Component) = buf.writeChat(item)
    }

    @JvmField
    val OPTIONAL_COMPONENT = object : EntityDataSerializer<Optional<Component>> {
        override fun write(buf: ByteBuf, item: Optional<Component>) = buf.writeOptional(item) { buf.writeChat(it) }
    }

    @JvmField
    val ITEM_STACK = object : EntityDataSerializer<ItemStack> {
        override fun write(buf: ByteBuf, item: ItemStack) = Unit // TODO: Support item metadata writing
    }

    @JvmField
    val BLOCK_STATE = object : EntityDataSerializer<Optional<KryptonBlock>> {
        override fun write(buf: ByteBuf, item: Optional<KryptonBlock>) = buf.writeOptional(item) {
            // TODO: Support block state metadata writing
        }
    }

    @JvmField
    val BOOLEAN = object : EntityDataSerializer<Boolean> {
        override fun write(buf: ByteBuf, item: Boolean) {
            buf.writeBoolean(item)
        }
    }

    val PARTICLE = object : EntityDataSerializer<Particle> {
        override fun write(buf: ByteBuf, item: Particle) = Unit // TODO: Support particle metadata writing
    }

    @JvmField
    val ROTATIONS = object : EntityDataSerializer<Rotation> {
        override fun write(buf: ByteBuf, item: Rotation) {
            buf.writeFloat(item.x)
            buf.writeFloat(item.y)
            buf.writeFloat(item.z)
        }
    }

    @JvmField
    val BLOCK_POS = object : EntityDataSerializer<Vector3i> {
        override fun write(buf: ByteBuf, item: Vector3i) {
            buf.writeLong(item.asLong())
        }
    }

    @JvmField
    val OPTIONAL_BLOCK_POS = object : EntityDataSerializer<Optional<Vector3i>> {
        override fun write(buf: ByteBuf, item: Optional<Vector3i>) = buf.writeOptional(item) { buf.writeLong(it.asLong()) }
    }

    @JvmField
    val DIRECTION = object : EntityDataSerializer<Direction> {
        override fun write(buf: ByteBuf, item: Direction) = buf.writeVarInt(item.id)
    }

    @JvmField
    val OPTIONAL_UUID = object : EntityDataSerializer<Optional<UUID>> {
        override fun write(buf: ByteBuf, item: Optional<UUID>) = buf.writeOptional(item) { buf.writeUUID(it) }
    }

    @JvmField
    val COMPOUND_TAG = object : EntityDataSerializer<CompoundBinaryTag> {
        override fun write(buf: ByteBuf, item: CompoundBinaryTag) = buf.writeNBTCompound(item)
    }

    @JvmField
    val VILLAGER_DATA = object : EntityDataSerializer<VillagerData> {
        override fun write(buf: ByteBuf, item: VillagerData) {
            buf.writeVarInt(item.type.id)
            buf.writeVarInt(item.profession.id)
            buf.writeVarInt(item.level)
        }
    }

    @JvmField
    val OPTIONAL_UNSIGNED_INT = object : EntityDataSerializer<OptionalInt> {
        override fun write(buf: ByteBuf, item: OptionalInt) = buf.writeVarInt(item.orElse(-1) + 1)
    }

    @JvmField
    val POSE = object : EntityDataSerializer<Pose> {
        override fun write(buf: ByteBuf, item: Pose) = buf.writeEnum(item)
    }

    init {
        SERIALIZERS.add(BYTE)
        SERIALIZERS.add(INT)
        SERIALIZERS.add(FLOAT)
        SERIALIZERS.add(STRING)
        SERIALIZERS.add(COMPONENT)
        SERIALIZERS.add(OPTIONAL_COMPONENT)
        SERIALIZERS.add(ITEM_STACK)
        SERIALIZERS.add(BOOLEAN)
        SERIALIZERS.add(ROTATIONS)
        SERIALIZERS.add(BLOCK_POS)
        SERIALIZERS.add(OPTIONAL_BLOCK_POS)
        SERIALIZERS.add(DIRECTION)
        SERIALIZERS.add(OPTIONAL_UUID)
        SERIALIZERS.add(BLOCK_STATE)
        SERIALIZERS.add(COMPOUND_TAG)
        SERIALIZERS.add(PARTICLE)
        SERIALIZERS.add(VILLAGER_DATA)
        SERIALIZERS.add(OPTIONAL_UNSIGNED_INT)
        SERIALIZERS.add(POSE)
    }

    @JvmStatic
    operator fun get(id: Int) = SERIALIZERS[id]

    @JvmStatic
    fun idOf(serializer: EntityDataSerializer<*>) = SERIALIZERS.idOf(serializer)
}
