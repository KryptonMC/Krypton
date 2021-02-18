package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeNBTCompound
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag

class PacketOutChunkData : PlayPacket(0x20) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(0) // chunk x
        buf.writeInt(0) // chunk z
        buf.writeBoolean(true) // full chunk
        buf.writeVarInt(1) // primary bit mask

        // height maps NBT
        buf.writeNBTCompound(CompoundBinaryTag.builder()
            .put("", CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", LongArrayBinaryTag.of(
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    537921540L
                ))
                .put("WORLD_SURFACE", LongArrayBinaryTag.of(
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    72198606942111748L,
                    537921540L
                ))
                .build())
            .build())

        buf.writeVarInt(1024) // size of biomes array
        for (i in 1..1024) buf.writeVarInt(127) // write biome ID 127 (void)

        buf.writeVarInt(2056) // size of data in bytes

        buf.writeShort(4096) // block count - 2 bytes
        buf.writeByte(4) // bits per block - 1 byte
        buf.writeVarInt(2) // palette length - 1 byte
        buf.writeVarInt(1) // palette[0] = stone (PID 1) - 1 byte
        buf.writeVarInt(9) // palette[1] = grass_block not snowy (PID 9) - 1 byte
        buf.writeVarInt(256) // data array length - 2 bytes

        // data array
        for (i in 1..2048) buf.writeByte(0) // 2048 bytes

        buf.writeVarInt(0) // block entities array length
    }
}