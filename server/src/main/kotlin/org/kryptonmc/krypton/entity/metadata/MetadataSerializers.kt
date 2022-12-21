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
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Rotations
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.data.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.GlobalPosition
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readBlockPos
import org.kryptonmc.krypton.util.readRotations
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeBlockPos
import org.kryptonmc.krypton.util.writeRotations
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.nbt.CompoundTag
import java.util.OptionalInt
import java.util.UUID

@Catalogue(MetadataSerializer::class)
object MetadataSerializers {

    private val SERIALIZERS = IntIdentityHashBiMap.create<MetadataSerializer<*>>(16)

    @JvmField
    val BYTE: MetadataSerializer<Byte> = MetadataSerializer.simple(ByteBuf::readByte) { buf, item -> buf.writeByte(item.toInt()) }
    @JvmField
    val INT: MetadataSerializer<Int> = MetadataSerializer.simple(ByteBuf::readVarInt, ByteBuf::writeVarInt)
    @JvmField
    val FLOAT: MetadataSerializer<Float> = MetadataSerializer.simple(ByteBuf::readFloat, ByteBuf::writeFloat)
    @JvmField
    val STRING: MetadataSerializer<String> = MetadataSerializer.simple(ByteBuf::readString, ByteBuf::writeString)
    @JvmField
    val COMPONENT: MetadataSerializer<Component> = MetadataSerializer.simple(ByteBuf::readComponent, ByteBuf::writeComponent)
    @JvmField
    val OPTIONAL_COMPONENT: MetadataSerializer<Component?> = MetadataSerializer.nullable(ByteBuf::readComponent, ByteBuf::writeComponent)
    @JvmField
    val ITEM_STACK: MetadataSerializer<KryptonItemStack> = MetadataSerializer.simple(ByteBuf::readItem, ByteBuf::writeItem)
    @JvmField
    val BOOLEAN: MetadataSerializer<Boolean> = MetadataSerializer.simple(ByteBuf::readBoolean, ByteBuf::writeBoolean)
    @JvmField
    val ROTATION: MetadataSerializer<Rotations> = MetadataSerializer.simple(ByteBuf::readRotations, ByteBuf::writeRotations)
    @JvmField
    val BLOCK_POS: MetadataSerializer<BlockPos> = MetadataSerializer.simple(ByteBuf::readBlockPos, ByteBuf::writeBlockPos)
    @JvmField
    val OPTIONAL_BLOCK_POS: MetadataSerializer<BlockPos?> = MetadataSerializer.nullable(ByteBuf::readBlockPos, ByteBuf::writeBlockPos)
    @JvmField
    val DIRECTION: MetadataSerializer<Direction> = MetadataSerializer.simpleEnum()
    @JvmField
    val OPTIONAL_UUID: MetadataSerializer<UUID?> = MetadataSerializer.nullable(ByteBuf::readUUID, ByteBuf::writeUUID)
    @JvmField
    val OPTIONAL_BLOCK: MetadataSerializer<KryptonBlockState?> = MetadataSerializer.simple(
        { buf -> buf.readVarInt().let { if (it == 0) null else KryptonBlock.stateFromId(it) } },
        { buf, item -> if (item != null) buf.writeVarInt(KryptonBlock.idOf(item)) else buf.writeVarInt(0) }
    )
    @JvmField
    val NBT: MetadataSerializer<CompoundTag> = MetadataSerializer.simple(ByteBuf::readNBT, ByteBuf::writeNBT)
    @JvmField
    val PARTICLE: MetadataSerializer<ParticleOptions> = MetadataSerializer.simple(::ParticleOptions) { buf, item ->
        buf.writeId(KryptonRegistries.PARTICLE_TYPE, item.type)
        item.write(buf)
    }
    @JvmField
    val VILLAGER_DATA: MetadataSerializer<VillagerData> = MetadataSerializer.simple(::VillagerData) { buf, item -> item.write(buf) }
    @JvmField
    val OPTIONAL_INT: MetadataSerializer<OptionalInt> = MetadataSerializer.simple(
        { buf -> buf.readVarInt().let { if (it == 0) OptionalInt.empty() else OptionalInt.of(it - 1) } },
        { buf, item -> buf.writeVarInt(item.orElse(-1) + 1) }
    )
    @JvmField
    val POSE: MetadataSerializer<Pose> = MetadataSerializer.simpleEnum()
    @JvmField
    val CAT_VARIANT: MetadataSerializer<CatVariant> = MetadataSerializer.simpleEnum()
    @JvmField
    val FROG_VARIANT: MetadataSerializer<FrogVariant> = MetadataSerializer.simpleEnum()
    @JvmField
    val OPTIONAL_GLOBAL_POSITION: MetadataSerializer<GlobalPosition?> = MetadataSerializer.nullable(::GlobalPosition) { buf, position ->
        position.write(buf)
    }
    @JvmField
    val PAINTING_VARIANT: MetadataSerializer<PaintingVariant> = MetadataSerializer.simpleId(KryptonRegistries.PAINTING_VARIANT)

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
        SERIALIZERS.add(BLOCK_POS)
        SERIALIZERS.add(OPTIONAL_BLOCK_POS)
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
        SERIALIZERS.add(PAINTING_VARIANT)
    }

    @JvmStatic
    fun getById(id: Int): MetadataSerializer<*>? = SERIALIZERS.get(id)

    @JvmStatic
    fun getId(serializer: MetadataSerializer<*>): Int = SERIALIZERS.getId(serializer)
}
