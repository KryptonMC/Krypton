package me.bristermitten.minekraft.extension

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import me.bristermitten.minekraft.entity.Particle
import me.bristermitten.minekraft.entity.Slot
import me.bristermitten.minekraft.entity.cardinal.Direction
import me.bristermitten.minekraft.entity.cardinal.Position
import me.bristermitten.minekraft.entity.cardinal.Rotation
import me.bristermitten.minekraft.entity.entities.villager.VillagerData
import me.bristermitten.minekraft.entity.metadata.MetadataType
import me.bristermitten.minekraft.entity.metadata.Pose
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
        writeVarInt(MetadataType.VARINT.id)
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
        writeVarInt(direction.id)
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

fun ByteBuf.writeOptionalMetadata(index: UByte, optionalChat: Component? = null) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_CHAT.id)
        writeOptionalChat(optionalChat)
    }
}

fun ByteBuf.writeOptionalMetadata(index: UByte, optionalPosition: Position? = null) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_POSITION.id)
        writeOptionalPosition(optionalPosition)
    }
}

fun ByteBuf.writeOptionalMetadata(index: UByte, optionalUUID: UUID? = null) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(MetadataType.OPTIONAL_UUID.id)
        writeOptionalUUID(optionalUUID)
    }
}

fun ByteBuf.writeOptionalMetadata(index: UByte, optionalVarInt: Int = 0, type: MetadataType = MetadataType.OPTIONAL_VARINT) {
    writeUByte(index)
    if (index != 0xFF.toUByte()) {
        writeVarInt(type.id)
        writeVarInt(optionalVarInt)
    }
}