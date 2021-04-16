package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.extension.*
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ChunkSection

/**
 * This packet is very strange and really weird to compute, so don't expect to understand it straight away.
 *
 * I recommend reading [wiki.vg](https://wiki.vg/Chunk_Format) for more information on this, as they can do
 * a much better job at explaining it than I can.
 *
 * @param chunk the chunk to send the data of
 *
 * @author Callum Seabrook
 */
class PacketOutChunkData(private val chunk: KryptonChunk) : PlayPacket(0x20) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)

        val isFullChunk = chunk.biomes.isNotEmpty()
        buf.writeBoolean(isFullChunk) // if the chunk is full
        buf.writeVarInt(calculateBitMask(chunk.sections)) // the primary bit mask

        buf.writeNBTCompound(CompoundBinaryTag.builder() // heightmaps
            .put("", CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", chunk.heightmaps.motionBlocking)
                .put("WORLD_SURFACE", chunk.heightmaps.worldSurface)
                .build())
            .build())

        if (isFullChunk) { // respect full chunk setting
            buf.writeVarInt(chunk.biomes.size)
            for (biome in chunk.biomes) buf.writeVarInt(biome.id)
        }

        val sections = chunk.sections.filter { it.blockStates.isNotEmpty() }

        // calculate the size of the chunk data
        var bytesLength = 0
        sections.forEach { section ->
            bytesLength += 2
            bytesLength += 1

            val paletteSize = section.palette.size
            val bitsPerBlock = paletteSize.calculateBits()
            if (bitsPerBlock < 9) {
                bytesLength += section.palette.size.varIntSize()
                bytesLength += section.palette.sumBy { entry ->
                    GlobalPalette[entry.name].states.first { it.properties == entry.properties }.id.varIntSize()
                }
            }

            bytesLength += section.blockStates.size.varIntSize()
            bytesLength += section.blockStates.data.sumBy { Long.SIZE_BYTES }
        }
        buf.writeVarInt(bytesLength)

        // write the chunk data
        sections.forEach { section ->
            buf.writeShort(section.blockStates.data.size)

            val paletteSize = section.palette.size
            val bitsPerBlock = paletteSize.calculateBits()
            buf.writeUByte(bitsPerBlock.toUByte())

            if (bitsPerBlock < 9) { // write palette
                buf.writeVarInt(paletteSize)
                section.palette.forEach { block ->
                    buf.writeVarInt(GlobalPalette[block.name].states.first { it.properties == block.properties }.id)
                }
            }

            buf.writeLongArray(section.blockStates.data)
        }

        // TODO: When block entities are added, make use of this here
        buf.writeVarInt(0) // number of block entities
    }

    private fun calculateBitMask(sections: List<ChunkSection>): Int {
        var result = 0
        repeat(16) { i ->
            if (sections.firstOrNull { it.y == (i - 1) && it.blockStates.isNotEmpty() } == null) return@repeat
            result = result or (1 shl (i - 1))
        }
        return result
    }
}