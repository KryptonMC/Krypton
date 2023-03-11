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
package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.entity.animal.type.FrogVariant
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Rotation
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.villagerdata.VillagerData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.GlobalPos
import org.kryptonmc.krypton.util.map.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readBlockPos
import org.kryptonmc.krypton.util.readRotation
import org.kryptonmc.krypton.util.writeRotation
import org.kryptonmc.krypton.util.readVarLong
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeBlockPos
import org.kryptonmc.krypton.util.writeVarLong
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
    val LONG: MetadataSerializer<Long> = MetadataSerializer.simple(ByteBuf::readVarLong, ByteBuf::writeVarLong)
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
    val BLOCK_STATE: MetadataSerializer<KryptonBlockState?> = MetadataSerializer.simple(
        { buf -> buf.readVarInt().let { if (it == 0) null else KryptonBlock.stateFromId(it) } },
        { buf, block -> if (block != null) buf.writeVarInt(KryptonBlock.idOf(block)) else buf.writeVarInt(0) }
    )
    @JvmField
    val BOOLEAN: MetadataSerializer<Boolean> = MetadataSerializer.simple(ByteBuf::readBoolean, ByteBuf::writeBoolean)
    @JvmField
    val PARTICLE: MetadataSerializer<ParticleOptions> = MetadataSerializer.simple(::ParticleOptions) { buf, item ->
        buf.writeId(KryptonRegistries.PARTICLE_TYPE, item.type)
        item.write(buf)
    }
    @JvmField
    val ROTATION: MetadataSerializer<Rotation> = MetadataSerializer.simple(ByteBuf::readRotation, ByteBuf::writeRotation)
    @JvmField
    val BLOCK_POS: MetadataSerializer<Vec3i> = MetadataSerializer.simple(ByteBuf::readBlockPos, ByteBuf::writeBlockPos)
    @JvmField
    val OPTIONAL_BLOCK_POS: MetadataSerializer<Vec3i?> = MetadataSerializer.nullable(ByteBuf::readBlockPos, ByteBuf::writeBlockPos)
    @JvmField
    val DIRECTION: MetadataSerializer<Direction> = MetadataSerializer.simpleEnum()
    @JvmField
    val OPTIONAL_UUID: MetadataSerializer<UUID?> = MetadataSerializer.nullable(ByteBuf::readUUID, ByteBuf::writeUUID)
    @JvmField
    val OPTIONAL_GLOBAL_POS: MetadataSerializer<GlobalPos?> = MetadataSerializer.nullable(::GlobalPos) { buf, position ->
        position.write(buf)
    }
    @JvmField
    val COMPOUND_TAG: MetadataSerializer<CompoundTag> = MetadataSerializer.simple(ByteBuf::readNBT, ByteBuf::writeNBT)
    @JvmField
    val VILLAGER_DATA: MetadataSerializer<VillagerData> = MetadataSerializer.simple(::VillagerData) { buf, item -> item.write(buf) }
    @JvmField
    val OPTIONAL_UNSIGNED_INT: MetadataSerializer<OptionalInt> = MetadataSerializer.simple(
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
    val PAINTING_VARIANT: MetadataSerializer<PaintingVariant> = MetadataSerializer.simpleId(KryptonRegistries.PAINTING_VARIANT)

    init {
        SERIALIZERS.add(BYTE)
        SERIALIZERS.add(INT)
        SERIALIZERS.add(LONG)
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
        SERIALIZERS.add(BLOCK_STATE)
        SERIALIZERS.add(COMPOUND_TAG)
        SERIALIZERS.add(PARTICLE)
        SERIALIZERS.add(VILLAGER_DATA)
        SERIALIZERS.add(OPTIONAL_UNSIGNED_INT)
        SERIALIZERS.add(POSE)
        SERIALIZERS.add(CAT_VARIANT)
        SERIALIZERS.add(FROG_VARIANT)
        SERIALIZERS.add(OPTIONAL_GLOBAL_POS)
        SERIALIZERS.add(PAINTING_VARIANT)
    }

    @JvmStatic
    fun getById(id: Int): MetadataSerializer<*>? = SERIALIZERS.get(id)

    @JvmStatic
    fun getId(serializer: MetadataSerializer<*>): Int = SERIALIZERS.getId(serializer)
}
