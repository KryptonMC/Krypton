package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.calculateBits
import org.kryptonmc.krypton.util.varIntSize
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.krypton.util.writeNBTCompound
import org.kryptonmc.krypton.util.writeUByte
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.chunk.ChunkSection
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.transform

/**
 * This packet is very strange and really weird to compute, so don't expect to understand it straight away.
 *
 * I recommend reading [wiki.vg](https://wiki.vg/Chunk_Format) for more information on this, as they can do
 * a much better job at explaining it than I can.
 *
 * @param chunk the chunk to send the data of
 */
class PacketOutChunkData(private val chunk: KryptonChunk) : PlayPacket(0x20) {

    private val heightmaps = CompoundBinaryTag.from(chunk.heightmaps.transform {
        it.key.name to LongArrayBinaryTag.of(*it.value.data.data)
    })

    override fun write(buf: ByteBuf) {
        buf.writeInt(chunk.position.x)
        buf.writeInt(chunk.position.z)

        val isFullChunk = chunk.biomes.isNotEmpty()
        buf.writeBoolean(isFullChunk) // if the chunk is full
        buf.writeVarInt(calculateBitMask(chunk.sections)) // the primary bit mask

        buf.writeNBTCompound(heightmaps)

        if (isFullChunk) { // respect full chunk setting
            buf.writeVarInt(chunk.biomes.size)
            chunk.biomes.forEach { buf.writeVarInt(it.id) }
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
            buf.writeShort(section.nonEmptyBlockCount)

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
