package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.extension.writeNBTCompound
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.extension.writeShort
import org.kryptonmc.krypton.extension.writeUByte
import org.kryptonmc.krypton.world.chunk.Chunk
import kotlin.math.ceil

class PacketOutChunkData(
    val chunk: Chunk
) : PlayPacket(0x20) {

    override fun write(buf: ByteBuf) {
//        buf.writeInt(0) // chunk x
//        buf.writeInt(0) // chunk z
//        buf.writeBoolean(true) // full chunk
//        buf.writeVarInt(1) // primary bit mask
//
//        // height maps NBT
//        buf.writeNBTCompound(CompoundBinaryTag.builder()
//            .put("", CompoundBinaryTag.builder()
//                .put("MOTION_BLOCKING", LongArrayBinaryTag.of(
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    537921540L
//                ))
//                .put("WORLD_SURFACE", LongArrayBinaryTag.of(
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    72198606942111748L,
//                    537921540L
//                ))
//                .build())
//            .build())
//
//        buf.writeVarInt(1024) // size of biomes array
//        for (i in 1..1024) buf.writeVarInt(127) // write biome ID 127 (void)
//
//        buf.writeVarInt(2056) // size of data in bytes
//
//        buf.writeShort(4096) // block count
//        buf.writeByte(4) // bits per block
//        buf.writeVarInt(2) // palette length
//        buf.writeVarInt(1) // palette[0] = stone (PID 1)
//        buf.writeVarInt(9) // palette[1] = grass_block not snowy (PID 9)
//        buf.writeVarInt(256) // data array length
//
//        // data array
//        for (i in 1..2048) buf.writeByte(0)
//
//        buf.writeVarInt(0) // block entities array length

        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)
        buf.writeBoolean(true)
        buf.writeVarInt(0b1111111111111111)

        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .put("", CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", LongArrayBinaryTag.of(*chunk.level.heightmaps[0].second.toLongArray()))
                .put("WORLD_SURFACE", LongArrayBinaryTag.of(*chunk.level.heightmaps[0].second.toLongArray()))
                .build())
            .build())

        buf.writeVarInt(1024)
        for (biome in chunk.level.biomes) buf.writeVarInt(biome.id)

        // TODO: Fix this
        buf.writeVarInt((2 * chunk.level.sections.size) + chunk.level.sections.size + chunk.level.sections.sumBy { it.blockStates.size })

        for (section in chunk.level.sections) {
            buf.writeShort(section.blockStates.size) // short - 2 bytes
            buf.writeUByte(14u) // ubyte - 1 byte

            buf.writeVarInt(section.blockStates.size) // arbitrary length
            for (blockState in section.blockStates) {
                buf.writeLong(blockState)
            }
        }

        buf.writeShort(chunk.level.sections[0].blockStates.size)
    }
}