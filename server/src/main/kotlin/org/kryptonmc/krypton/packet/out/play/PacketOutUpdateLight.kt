package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.chunk.KryptonChunk

class PacketOutUpdateLight(private val chunk: KryptonChunk) : PlayPacket(0x23) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(chunk.position.x.toInt())
        buf.writeVarInt(chunk.position.z.toInt())
        buf.writeBoolean(true)
        val sections = chunk.sections.sortedBy { it.y }

        var skyLightMask = 0
        var blockLightMask = 0
        var emptySkyLightMask = 0
        var emptyBlockLightMask = 0
        for (i in sections.indices) {
            val section = sections[i]

            val skyMask = if (section.skyLight.all { it == 0.toByte() }) 0 else 1
            emptySkyLightMask += (-skyMask + 1) shl i
            skyLightMask += skyMask shl i

            val blockMask = if (section.blockLight.all { it == 0.toByte() }) 0 else 1
            emptyBlockLightMask += (-blockMask + 1) shl i
            blockLightMask += blockMask shl i
        }
        buf.writeVarInt(skyLightMask)
        buf.writeVarInt(blockLightMask)
        buf.writeVarInt(emptySkyLightMask)
        buf.writeVarInt(emptyBlockLightMask)

        for (section in sections.filter { section -> !section.skyLight.all { it == 0.toByte() } }) {
            buf.writeVarInt(2048)
            buf.writeBytes(section.skyLight.toByteArray())
        }
        for (section in sections.filter { section -> !section.blockLight.all { it == 0.toByte() } }) {
            buf.writeVarInt(2048)
            buf.writeBytes(section.blockLight.toByteArray())
        }
    }
}