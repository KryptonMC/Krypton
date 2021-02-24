package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.kryptonmc.krypton.packet.state.PlayPacket
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import okhttp3.internal.toHexString
import org.kryptonmc.krypton.extension.*
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.chunk.Chunk
import org.kryptonmc.krypton.world.chunk.HeightmapType
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
                .put("MOTION_BLOCKING", LongArrayBinaryTag.of(*chunk.level.heightmaps.single { it.first == HeightmapType.MOTION_BLOCKING }.second.toLongArray()))
                .put("WORLD_SURFACE", LongArrayBinaryTag.of(*chunk.level.heightmaps.single { it.first == HeightmapType.MOTION_BLOCKING }.second.toLongArray()))
                .build())
            .build())

        buf.writeVarInt(1024)
        for (biome in chunk.level.biomes) buf.writeVarInt(biome.id)

        var bytesLength = 0
        for (section in chunk.level.sections) {
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

        for (section in chunk.level.sections) {
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

        buf.writeShort(chunk.level.sections[0].blockStates.size)
    }

    companion object {

        private val LOGGER = logger<PacketOutChunkData>()
    }
}