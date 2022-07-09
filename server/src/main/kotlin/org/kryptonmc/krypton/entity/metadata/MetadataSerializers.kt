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
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.entity.animal.type.FrogVariant
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.GlobalPosition
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readGlobalPosition
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVector
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeGlobalPosition
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i
import java.util.OptionalInt
import java.util.UUID

@Catalogue(MetadataSerializer::class)
object MetadataSerializers {

    private val SERIALIZERS = IntIdentityHashBiMap<MetadataSerializer<*>>(16)

    @JvmField
    val BYTE: MetadataSerializer<Byte> = MetadataSerializer.simple({ buf, item -> buf.writeByte(item.toInt()) }, ByteBuf::readByte)
    @JvmField
    val INT: MetadataSerializer<Int> = MetadataSerializer.simple(ByteBuf::writeVarInt, ByteBuf::readVarInt)
    @JvmField
    val FLOAT: MetadataSerializer<Float> = MetadataSerializer.simple(ByteBuf::writeFloat, ByteBuf::readFloat)
    @JvmField
    val STRING: MetadataSerializer<String> = MetadataSerializer.simple(ByteBuf::writeString, ByteBuf::readString)
    @JvmField
    val COMPONENT: MetadataSerializer<Component> = MetadataSerializer.simple(ByteBuf::writeComponent, ByteBuf::readComponent)
    @JvmField
    val OPTIONAL_COMPONENT: MetadataSerializer<Component?> = MetadataSerializer.optional(ByteBuf::writeComponent, ByteBuf::readComponent)
    @JvmField
    val ITEM_STACK: MetadataSerializer<KryptonItemStack> = MetadataSerializer.simple(ByteBuf::writeItem, ByteBuf::readItem)
    @JvmField
    val BOOLEAN: MetadataSerializer<Boolean> = MetadataSerializer.simple(ByteBuf::writeBoolean, ByteBuf::readBoolean)
    @JvmField
    val ROTATION: MetadataSerializer<Vector3f> = MetadataSerializer.simple({ buf, item ->
        buf.writeFloat(item.x())
        buf.writeFloat(item.y())
        buf.writeFloat(item.z())
    }, { Vector3f(it.readFloat(), it.readFloat(), it.readFloat()) })
    @JvmField
    val POSITION: MetadataSerializer<Vector3i> = MetadataSerializer.simple(ByteBuf::writeVector, ByteBuf::readVector)
    @JvmField
    val OPTIONAL_POSITION: MetadataSerializer<Vector3i?> = MetadataSerializer.optional(ByteBuf::writeVector, ByteBuf::readVector)
    @JvmField
    val DIRECTION: MetadataSerializer<Direction> = MetadataSerializer.simpleEnum(Direction::class.java)
    @JvmField
    val OPTIONAL_UUID: MetadataSerializer<UUID?> = MetadataSerializer.optional(ByteBuf::writeUUID, ByteBuf::readUUID)
    @JvmField
    val OPTIONAL_BLOCK: MetadataSerializer<KryptonBlock?> = MetadataSerializer.simple(
        { buf, item -> if (item != null) buf.writeVarInt(item.stateId) else buf.writeVarInt(0) },
        {
            val stateId = it.readVarInt()
            if (stateId == 0) null else BlockLoader.fromState(stateId)
        }
    )
    @JvmField
    val NBT: MetadataSerializer<CompoundTag> = MetadataSerializer.simple(ByteBuf::writeNBT, ByteBuf::readNBT)
    @JvmField
    val PARTICLE: MetadataSerializer<ParticleOptions> = MetadataSerializer.simple(
        { buf, item ->
            buf.writeId(Registries.PARTICLE_TYPE, item.type)
            item.write(buf)
        },
        ::ParticleOptions
    )
    @JvmField
    val VILLAGER_DATA: MetadataSerializer<VillagerData> = MetadataSerializer.simple({ buf, item -> item.write(buf) }, ::VillagerData)
    @JvmField
    val OPTIONAL_INT: MetadataSerializer<OptionalInt> = MetadataSerializer.simple(
        { buf, item -> buf.writeVarInt(item.orElse(-1) + 1) },
        {
            val value = it.readVarInt()
            if (value == 0) OptionalInt.empty() else OptionalInt.of(value - 1)
        }
    )
    @JvmField
    val POSE: MetadataSerializer<Pose> = MetadataSerializer.simpleEnum(Pose::class.java)
    @JvmField
    val CAT_VARIANT: MetadataSerializer<CatVariant> = MetadataSerializer.simpleId(Registries.CAT_VARIANT)
    @JvmField
    val FROG_VARIANT: MetadataSerializer<FrogVariant> = MetadataSerializer.simpleId(Registries.FROG_VARIANT)
    @JvmField
    val OPTIONAL_GLOBAL_POSITION: MetadataSerializer<GlobalPosition?> =
        MetadataSerializer.optional(ByteBuf::writeGlobalPosition, ByteBuf::readGlobalPosition)
    @JvmField
    val PICTURE: MetadataSerializer<Picture> = MetadataSerializer.simpleId(Registries.PICTURES)

    @JvmStatic
    fun get(id: Int): MetadataSerializer<*>? = SERIALIZERS[id]

    @JvmStatic
    fun idOf(serializer: MetadataSerializer<*>): Int = SERIALIZERS.idOf(serializer)

    init {
        SERIALIZERS.add(BYTE)
        SERIALIZERS.add(INT)
        SERIALIZERS.add(FLOAT)
        SERIALIZERS.add(STRING)
        SERIALIZERS.add(COMPONENT)
        SERIALIZERS.add(OPTIONAL_COMPONENT)
        SERIALIZERS.add(ITEM_STACK)
        SERIALIZERS.add(BOOLEAN)
        SERIALIZERS.add(ROTATION)
        SERIALIZERS.add(POSITION)
        SERIALIZERS.add(OPTIONAL_POSITION)
        SERIALIZERS.add(DIRECTION)
        SERIALIZERS.add(OPTIONAL_UUID)
        SERIALIZERS.add(OPTIONAL_BLOCK)
        SERIALIZERS.add(NBT)
        SERIALIZERS.add(PARTICLE)
        SERIALIZERS.add(VILLAGER_DATA)
        SERIALIZERS.add(OPTIONAL_INT)
        SERIALIZERS.add(POSE)
        SERIALIZERS.add(CAT_VARIANT)
        SERIALIZERS.add(FROG_VARIANT)
        SERIALIZERS.add(OPTIONAL_GLOBAL_POSITION)
        SERIALIZERS.add(PICTURE)
    }
}
