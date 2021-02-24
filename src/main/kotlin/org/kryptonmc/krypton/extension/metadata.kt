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

fun ByteBuf.writeMetadata(index: UByte, byte: Byte) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.BYTE.id)
        writeByte(byte)
    }
}

fun ByteBuf.writeMetadata(index: UByte, varInt: Int) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.VARINT.id)
        writeVarInt(varInt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, float: Float) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.FLOAT.id)
        writeFloat(float)
    }
}

fun ByteBuf.writeMetadata(index: UByte, string: String) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.STRING.id)
        writeString(string)
    }
}

fun ByteBuf.writeMetadata(index: UByte, chat: Component) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.CHAT.id)
        writeChat(chat)
    }
}

fun ByteBuf.writeMetadata(index: UByte, slot: Slot) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.SLOT.id)
        writeSlot(slot)
    }
}

fun ByteBuf.writeMetadata(index: UByte, boolean: Boolean) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.BOOLEAN.id)
        writeBoolean(boolean)
    }
}

fun ByteBuf.writeMetadata(index: UByte, rotation: Rotation) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.ROTATION.id)
        writeRotation(rotation)
    }
}

fun ByteBuf.writeMetadata(index: UByte, position: Position) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.POSITION.id)
        writePosition(position)
    }
}

fun ByteBuf.writeMetadata(index: UByte, direction: Direction) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.DIRECTION.id)
        writeVarInt(direction.value)
    }
}

fun ByteBuf.writeMetadata(index: UByte, nbt: CompoundBinaryTag) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.NBT.id)
        writeNBTCompound(nbt)
    }
}

fun ByteBuf.writeMetadata(index: UByte, particle: Particle) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.PARTICLE.id)
        writeParticle(particle)
    }
}

fun ByteBuf.writeMetadata(index: UByte, villagerData: VillagerData) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.VILLAGER_DATA.id)
        writeVillagerData(villagerData)
    }
}

fun ByteBuf.writeMetadata(index: UByte, pose: Pose) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.POSE.id)
        writeVarInt(pose.id)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalChatMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalChat: Optional<Component>) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_CHAT.id)
        writeOptionalChat(optionalChat)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalPositionMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalPosition: Optional<Position>) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_POSITION.id)
        writeOptionalPosition(optionalPosition)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalUUIDMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalUUID: Optional<UUID>) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_UUID.id)
        writeOptionalUUID(optionalUUID)
    }
}

@Suppress("INAPPLICABLE_JVM_NAME")
@JvmName("writeOptionalVarIntMetadata")
fun ByteBuf.writeOptionalMetadata(index: UByte, optionalVarInt: Optional<Int>, type: MetadataType = MetadataType.OPTIONAL_VARINT) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(type.id)
        writeOptionalVarInt(optionalVarInt)
    }
}