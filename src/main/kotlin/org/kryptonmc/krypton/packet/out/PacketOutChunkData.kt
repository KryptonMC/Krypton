package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.krypton.extension.*
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.chunk.Chunk
import org.kryptonmc.krypton.world.chunk.HeightmapType

class PacketOutChunkData(
    val chunk: Chunk
) : PlayPacket(0x20) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)
        buf.writeBoolean(true)
        buf.writeVarInt(0b1111111111111111)

        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .put("", CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", LongArrayBinaryTag.of(*chunk.level.heightmaps.single { it.first == HeightmapType.MOTION_BLOCKING }.second.toLongArray()))
                .put("WORLD_SURFACE", LongArrayBinaryTag.of(*chunk.level.heightmaps.single { it.first == HeightmapType.MOTION_BLOCKING }.second.toLongArray()))
                .build())
            .build())

        buf.writeVarInt(1024)
        for (biome in chunk.level.biomes) buf.writeVarInt(biome.id)

        val sections = chunk.level.sections.filter { it.blockStates.isNotEmpty() }

        var bytesLength = 0
        for (section in sections) {
            bytesLength += 2
            bytesLength += 1

            bytesLength += section.palette.size.varIntSize()
            bytesLength += section.palette.sumBy {
                if (it.name.value == "tall_seagrass") return@sumBy 2
                GlobalPalette.PALETTE.getValue(it.name).varIntSize()
            }

            bytesLength += section.blockStates.size.varIntSize()
            bytesLength += section.blockStates.sumBy { Long.SIZE_BYTES }
        }
        buf.writeVarInt(bytesLength)

        for (section in sections) {
            buf.writeShort(section.blockStates.size) // short - 2 bytes
            buf.writeUByte(4u) // ubyte - 1 byte

            buf.writeVarInt(section.palette.size)
            for (block in section.palette) {
                if (block.name.value == "tall_seagrass") {
                    when ((block.properties.getValue("half") as StringBinaryTag).value()) {
                        "lower" -> buf.writeVarInt(1347)
                        "upper" -> buf.writeVarInt(1346)
                    }
                    continue
                }
                buf.writeVarInt(GlobalPalette.PALETTE.getValue(block.name))
            }

            buf.writeVarInt(section.blockStates.size) // arbitrary length
            for (blockState in section.blockStates) {
                buf.writeLong(blockState)
            }
        }

        buf.writeVarInt(0)
    }
}