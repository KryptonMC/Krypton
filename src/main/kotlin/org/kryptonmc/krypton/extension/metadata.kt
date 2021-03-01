package org.kryptonmc.krypton.extension

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import org.kryptonmc.krypton.entity.Particle
import org.kryptonmc.krypton.entity.Slot
import org.kryptonmc.krypton.space.Direction
import org.kryptonmc.krypton.space.Position
import org.kryptonmc.krypton.space.Rotation
import org.kryptonmc.krypton.entity.entities.data.VillagerData
import org.kryptonmc.krypton.entity.metadata.MetadataType
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.Pose
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.util.*

fun ByteBuf.writeMetadata(index: UByte, byte: Byte?) {
    if (byte == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.BYTE.id)
        writeByte(byte)
    }
}

fun ByteBuf.writeMetadata(index: UByte, varInt: Int?) {
    if (varInt == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.VARINT.id)
        writeVarInt(varInt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, float: Float?) {
    if (float == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.FLOAT.id)
        writeFloat(float)
    }
}

fun ByteBuf.writeMetadata(index: UByte, string: String?) {
    if (string == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.STRING.id)
        writeString(string)
    }
}

fun ByteBuf.writeMetadata(index: UByte, chat: Component?) {
    if (chat == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.CHAT.id)
        writeChat(chat)
    }
}

fun ByteBuf.writeMetadata(index: UByte, slot: Slot?) {
    if (slot == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.SLOT.id)
        writeSlot(slot)
    }
}

fun ByteBuf.writeMetadata(index: UByte, boolean: Boolean?) {
    if (boolean == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.BOOLEAN.id)
        writeBoolean(boolean)
    }
}

fun ByteBuf.writeMetadata(index: UByte, rotation: Rotation?) {
    if (rotation == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.ROTATION.id)
        writeRotation(rotation)
    }
}

fun ByteBuf.writeMetadata(index: UByte, position: Position?) {
    if (position == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.POSITION.id)
        writePosition(position)
    }
}

fun ByteBuf.writeMetadata(index: UByte, direction: Direction?) {
    if (direction == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.DIRECTION.id)
        writeVarInt(direction.id)
    }
}

fun ByteBuf.writeMetadata(index: UByte, nbt: CompoundBinaryTag?) {
    if (nbt == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.NBT.id)
        writeNBTCompound(nbt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, particle: Particle?) {
    if (particle == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.PARTICLE.id)
        writeParticle(particle)
    }
}

fun ByteBuf.writeMetadata(index: UByte, villagerData: VillagerData?) {
    if (villagerData == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.VILLAGER_DATA.id)
        writeVillagerData(villagerData)
    }
}

fun ByteBuf.writeMetadata(index: UByte, pose: Pose?) {
    if (pose == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.POSE.id)
        writeVarInt(pose.id)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalChatMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalChat: Optional<Component>?) {
    if (optionalChat == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_CHAT.id)
        writeOptionalChat(optionalChat)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalPositionMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalPosition: Optional<Position>?) {
    if (optionalPosition == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_POSITION.id)
        writeOptionalPosition(optionalPosition)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalUUIDMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalUUID: Optional<UUID>?) {
    if (optionalUUID == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_UUID.id)
        writeOptionalUUID(optionalUUID)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalVarIntMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalVarInt: Optional<Int>?, type: MetadataType = MetadataType.OPTIONAL_VARINT) {
    if (optionalVarInt == null) return

    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(type.id)
        writeOptionalVarInt(optionalVarInt)
    }
}