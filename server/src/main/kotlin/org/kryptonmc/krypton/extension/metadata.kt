package org.kryptonmc.krypton.extension

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.entity.Slot
import org.kryptonmc.krypton.entity.entities.data.VillagerData
import org.kryptonmc.krypton.entity.metadata.MetadataType
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.Pose
import org.kryptonmc.krypton.api.space.Direction
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.space.Rotation
import java.util.*

private const val ENDING_INDEX: UByte = 0xFFu

fun ByteBuf.writeMetadata(index: UByte, byte: Byte?) {
    if (byte == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.BYTE.ordinal)
        writeByte(byte)
    }
}

fun ByteBuf.writeMetadata(index: UByte, varInt: Int?) {
    if (varInt == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.VARINT.ordinal)
        writeVarInt(varInt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, float: Float?) {
    if (float == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.FLOAT.ordinal)
        writeFloat(float)
    }
}

fun ByteBuf.writeMetadata(index: UByte, string: String?) {
    if (string == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.STRING.ordinal)
        writeString(string)
    }
}

fun ByteBuf.writeMetadata(index: UByte, chat: Component?) {
    if (chat == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.CHAT.ordinal)
        writeChat(chat)
    }
}

fun ByteBuf.writeMetadata(index: UByte, slot: Slot?) {
    if (slot == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.SLOT.ordinal)
        writeSlot(slot)
    }
}

fun ByteBuf.writeMetadata(index: UByte, boolean: Boolean?) {
    if (boolean == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.BOOLEAN.ordinal)
        writeBoolean(boolean)
    }
}

fun ByteBuf.writeMetadata(index: UByte, rotation: Rotation?) {
    if (rotation == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.ROTATION.ordinal)
        writeRotation(rotation)
    }
}

fun ByteBuf.writeMetadata(index: UByte, position: Vector?) {
    if (position == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.POSITION.ordinal)
        writePosition(position)
    }
}

fun ByteBuf.writeMetadata(index: UByte, direction: Direction?) {
    if (direction == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.DIRECTION.ordinal)
        writeVarInt(direction.id)
    }
}

fun ByteBuf.writeMetadata(index: UByte, nbt: CompoundBinaryTag?) {
    if (nbt == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.NBT.ordinal)
        writeNBTCompound(nbt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, particle: ParticleEffect?, location: Location?) {
    if (particle == null || location == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.PARTICLE.ordinal)
        writeParticle(particle, location)
    }
}

fun ByteBuf.writeMetadata(index: UByte, villagerData: VillagerData?) {
    if (villagerData == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.VILLAGER_DATA.ordinal)
        writeVillagerData(villagerData)
    }
}

fun ByteBuf.writeMetadata(index: UByte, pose: Pose?) {
    if (pose == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.POSE.ordinal)
        writeVarInt(pose.ordinal)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalChatMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalChat: Optional<Component>?) {
    if (optionalChat == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.OPTIONAL_CHAT.ordinal)
        writeOptionalChat(optionalChat)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalPositionMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalPosition: Optional<Vector>?) {
    if (optionalPosition == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.OPTIONAL_POSITION.ordinal)
        writeOptionalPosition(optionalPosition)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalUUIDMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalUUID: Optional<UUID>?) {
    if (optionalUUID == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(MetadataType.OPTIONAL_UUID.ordinal)
        writeOptionalUUID(optionalUUID)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalVarIntMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalVarInt: Optional<Int>?, type: MetadataType = MetadataType.OPTIONAL_VARINT) {
    if (optionalVarInt == null) return

    writeUByte(index)
    if (index != ENDING_INDEX) {
        writeVarInt(type.ordinal)
        writeOptionalVarInt(optionalVarInt)
    }
}